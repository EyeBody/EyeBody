package com.example.android.eyebody.googleDrive

import android.annotation.SuppressLint
import java.io.ByteArrayOutputStream
import java.io.IOException

import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.drive.MetadataChangeSet
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.Drive


/**
 * Created by YOON on 2017-10-14.
 *
 *
 * noted by YOON
 * manifests specified with down here for act it
uses-sdk
android:minSdkVersion="8"
android:targetSdkVersion="18"
meta-data
android:name="com.google.android.gms.version"
android:value="@integer/google_play_services_version"
 * and it is copyrighted 2013 google inc. all right reserved.
 */
@SuppressLint("Registered")
class ImageTransferDriveManager : Activity(), ConnectionCallbacks, OnConnectionFailedListener {

    companion object {
        val REQ_CAPTURE_IMAGE = 1
        val REQ_CREATOR = 2
        val REQ_RESOLUTION = 3
    }

    var myGoogleApiClient: GoogleApiClient? = null
    var myBitmapToSave: Bitmap? = null

    // 파일 저장
    // Local -> Drive
    private fun saveFileToDrive() {
        Log.i("GDManagerNew", "Drive 에서 new DriveContents 만들기 시도")
        val image = myBitmapToSave
        Drive.DriveApi.newDriveContents(myGoogleApiClient)
                .setResultCallback(ResultCallback { result ->
                    // If the operation was not successful, we cannot do anything
                    // and must
                    // fail.
                    if (!result.status.isSuccess) {
                        Log.i("GDManagerNew", "DriveContents 실패")
                        return@ResultCallback
                    }
                    // Otherwise, we can write our data to the new contents.
                    Log.i("GDManagerNew", "DriveContents 만들어짐")
                    // Get an output stream for the contents.
                    val outputStream = result.driveContents.outputStream
                    // Write the bitmap data from it.
                    val bitmapStream = ByteArrayOutputStream()
                    image?.compress(Bitmap.CompressFormat.PNG, 100, bitmapStream)
                    try {
                        outputStream.write(bitmapStream.toByteArray())
                    } catch (e1: IOException) {
                        Log.i("GDManagerNew", "쓸 수 없음 write exception")
                    }

                    // Create the initial metadata - MIME type and title.
                    // Note that the user will be able to change the title later.
                    val metadataChangeSet = MetadataChangeSet.Builder()
                            .setMimeType("image/jpeg").setTitle("Android Photo.png").build()
                    // Create an intent for the file chooser, and start it.
                    val intentSender = Drive.DriveApi
                            .newCreateFileActivityBuilder()
                            .setInitialMetadata(metadataChangeSet)
                            .setInitialDriveContents(result.driveContents)
                            .build(myGoogleApiClient)
                    try {
                        startIntentSenderForResult(
                                intentSender, REQ_CREATOR, null, 0, 0, 0)
                    } catch (e: SendIntentException) {
                        Log.i("GDManagerNew", "file chooser 예외")
                    }
                })
    }

    override fun onResume() {
        super.onResume()
        if (myGoogleApiClient == null) {
            // Create the API client and bind it to an instance variable.
            // We use this instance as the callback for connection and connection
            // failures.
            // Since no account name is passed, the user is prompted to choose.
            myGoogleApiClient = GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build()
        }
        // Connect the client. Once connected, the camera is launched.
        myGoogleApiClient?.connect()
    }

    override fun onPause() {
        myGoogleApiClient?.disconnect()
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            REQ_CAPTURE_IMAGE ->
                // Called after a photo has been taken.
                if (resultCode == Activity.RESULT_OK) {
                    // Store the image data as a bitmap for writing later.
                    myBitmapToSave = data.extras!!.get("data") as Bitmap
                }
            REQ_CREATOR ->
                // Called after a file is saved to Drive.
                if (resultCode == Activity.RESULT_OK) {
                    Log.i("GDManagerNew", "Image saved")
                    myBitmapToSave = null
                    // Just start the camera again for another photo.
                    startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                            REQ_CAPTURE_IMAGE)
                }
        }
    }


    //Connection Failed 이벤트에 대한 리스너
    override fun onConnectionFailed(result: ConnectionResult) {
        // Called whenever the API client fails to connect.
        Log.d("GDManagerNew", "연결실패: " + result.toString())
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.errorCode, 0).show()
            return
        }
        // The failure has a resolution. Resolve it.
        // Called typically when the app is not yet authorized, and an
        // authorization
        // dialog is displayed to the user.
        try {
            result.startResolutionForResult(this, REQ_RESOLUTION)
        } catch (e: SendIntentException) {
            Log.e("GDManagerNew", "start resolution for result exception", e)
        }

    }



    // 연결됐을때 콜백되는 함수
    override fun onConnected(connectionHint: Bundle?) {
        Log.i("GDManagerNew", "연결됨")
        if (myBitmapToSave == null) {
            // This activity has no UI of its own. Just start the camera.
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                    REQ_CAPTURE_IMAGE)
            return
        }
        saveFileToDrive()
    }

    // 연결 suspend 됐을 때 콜백되는 함수
    override fun onConnectionSuspended(cause: Int) {
        Log.i("GDManagerNew", "connectionSuspend")
    }
    

}