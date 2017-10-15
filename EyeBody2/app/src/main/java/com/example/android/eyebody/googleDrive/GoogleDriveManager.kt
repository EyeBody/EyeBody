package com.example.android.eyebody.googleDrive

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.MetadataChangeSet
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * @param context 권한을 얻기 위해.
 * @param activity 전송실패 등의 에러가 날 경우 해당 Activity에 Dialog를 띄움.
 */
open class GoogleDriveManager(val context: Context, val activity: Activity) : GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private val TAG = "mydbg_googleDrive"

    data class FileTransferData(val fileName: String, val fileLocale: Int)

    companion object {
        val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
        val REQUEST_CODE_RESOLUTION = 3
        val NETWORK_MODE_WIFI = 1
        val NETWORK_MODE_DATA = 2
        val FILE_LOCATION_SERVER = 1
        val FILE_LOCATION_LOCAL = -1
    }

    var mGoogleApiClient: GoogleApiClient? = null
    var mIntentSender : IntentSender? = null
    var networkStatus = NETWORK_MODE_WIFI


    init {
        // TODO if needed, check permission
        // context.checkPermission("???", 1, 1)
    }

    /**
     * 로그인을 요청합니다.
     * 로그인이 되어 있는 경우 : 무시
     * 설정된 계정이 없는 경우 : 계정 선택 후 로그인 요청
     * @see checkConnection
     */
    fun signIn() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(context)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build()
        }
        if (mGoogleApiClient!!.isConnected || mGoogleApiClient!!.isConnecting)
        else
            mGoogleApiClient?.connect() ?: Log.e(TAG, "mGoogleApiClient build null error")
    }

    /**
     * 로그아웃을 요청합니다.
     * @see checkConnection
     */
    fun signOut() {
        mGoogleApiClient?.disconnect()
        onConnectionStatusChanged()
    }

    /**
     * authorized 된 계정을 dis authorize 시키고
     * 새로운 계정으로 로그인하게 합니다.
     */
    fun changeAccount() {
        mGoogleApiClient?.clearDefaultAccountAndReconnect()
    }

    /**
     * 연결이 되었는지 반환합니다.
     * @return false : 연결 중이거나 연결이 되어있지 않은경우
     */
    fun checkConnection(): Boolean {
        val connection = mGoogleApiClient?.isConnected ?: false
        Log.i(TAG, "현재 연결상태 : $connection")
        return connection
    }

    /**
     * Local과 Server의 데이터를 비교한 뒤
     * Local에 없는 ServerFile을 강제로 모두 업로드함.
     * @see getSyncData
     * @see enforceFileSync
     * @return Boolean = {비교 한 번 더 해서 Server에 있는게 Local에 있으면 true}
     * 2. 로컬과 서버
     */
    fun saveAllFile(): Boolean {

        // 싱크 안 맞는 것 중 serverfile 만 골라서 싱크 강제로 맞춤.
        val fileTransferDataArray = getSyncData().filter { it.fileLocale == FILE_LOCATION_SERVER }.toTypedArray()
        enforceFileSync(fileTransferDataArray)

        // 싱크 중 serverfile 싱크미스가 있다면 실패
        if (getSyncData().filter { it.fileLocale == FILE_LOCATION_SERVER }.isNotEmpty())
        // 이 부분 제대로 짠 지 모르겠음
            return false
        return true
    }

    fun loadAllFile(): Boolean {

        // 싱크 안 맞는 것 중 localfile 만 골라서 싱크 강제로 맞춤.
        val fileTransferDataArray = getSyncData().filter { it.fileLocale == FILE_LOCATION_SERVER }.toTypedArray()
        enforceFileSync(fileTransferDataArray)

        // 싱크 중 localfile 싱크미스가 있다면 실패
        if (getSyncData().filter { it.fileLocale == FILE_LOCATION_LOCAL }.isNotEmpty())
        // 이 부분 제대로 짠 지 모르겠음
            return false
        return true
    }

    fun getSyncData(): Array<FileTransferData> {

        val fileNames = arrayOf(
                "171009~~.eyebody",
                "171010~~.eyebody"
                /* ...... */)
        val fileLocales = intArrayOf(
                FILE_LOCATION_SERVER,
                FILE_LOCATION_LOCAL
                /* ...... */)
        /*
        TODO 구글드라이브 싱크 구현
        GoogleDrive에서 싱크한 뒤
        위와 같은 데이터를 얻게 되면 이를 반환함.
         */

        // 두 array를 전송용 데이터로 만들고 반환
        val fileTransferDataArray = arrayOf<FileTransferData>()
        for (data in 1..fileNames.size)
            fileTransferDataArray[data] = FileTransferData(fileNames[data], fileLocales[data])

        return fileTransferDataArray
    }

    fun enforceFileSync(fileTransferDataArray: Array<FileTransferData>): Boolean {

        /*
        TODO 구글드라이브 업로드 구현
        싱크로 부터 얻거나 임의로 만든 전송용 string 데이터로 서버에 저장시키는 것.
         */
        //fileTransferDataArray

        return true //성공하면 true 실패하면 false
    }

    fun upload(photoURI: String) : IntentSender? {
        mIntentSender = null
        if (!checkConnection())
            return mIntentSender
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(ResultCallback{ result->
                    /*if (result.status.isSuccess) {
                        Log.d(TAG, "create newDriveContents 실패")
                        return@ResultCallback
                    }*/
                    Log.i(TAG, "driveContents를 만들었기 때문에 데이터를 구글에 쓸 수 있습니다.")
                    val outputStream = result.driveContents.outputStream
                    val bitmapStream = ByteArrayOutputStream()
                    BitmapFactory
                            .decodeStream(FileInputStream(File(photoURI)))
                            .compress(Bitmap.CompressFormat.PNG, 100, bitmapStream)
                    try {
                        outputStream.write(bitmapStream.toByteArray())
                    } catch (exception: IOException) {
                        Log.d(TAG, "IOException : bitmapStream -> driveContents.outputStream")
                        exception.printStackTrace()
                    }

                    val metadataChangeSet = MetadataChangeSet.Builder()
                            .setMimeType("image/jpeg").setTitle(photoURI).build()
                    val intentSender = Drive.DriveApi
                            .newCreateFileActivityBuilder()
                            .setInitialMetadata(metadataChangeSet)
                            .setInitialDriveContents(result.driveContents)
                            .build(mGoogleApiClient)
                    mIntentSender = intentSender
                })
        return mIntentSender
    }

    fun download(photoURI: String) {
        if (!checkConnection())
            return
        //Drive.DriveApi.query(mGoogleApiClient, )
    }


    open fun onConnectionStatusChanged(){

    }

    override fun onConnected(connectionHint: Bundle?) {
        onConnectionStatusChanged()
        Log.d(TAG, "구글드라이브 연결 콜백 (정상접속)")
    }

    override fun onConnectionSuspended(cause: Int) {
        onConnectionStatusChanged()
        Log.d(TAG, "구글드라이브 연결 콜백 (정상종료) : $cause")
    }

    override fun onConnectionFailed(result: ConnectionResult) {
        onConnectionStatusChanged()
        // 비정상적 종료에 대해 다시 시도할지, 또는 어떻게 할 지에 대해 작성하는 지역
        // 일반적으로는 에러 로그를 찍고, 해결가능한 실패에 대해서는 다시 시도한다.
        // 해당 연결이 중첩되서 발생할 수 있는가? 만약 그런다면 boolean 하나 만들어서 hasResoulution이 아니더라도 건너뛰는 걸 만들어야 할 것 같다.
        Log.d(TAG, "connectionFailed\n구글드라이브 에러종료 : $result")

        // 해결되지 않은 오류에 대한 후 처리
        if (!result.hasResolution()) {
            // 해결되지 않는 에러에 대해 다이얼로그를 띄워 사용자에게 보여준다.
            //GoogleApiAvailability.getInstance().getErrorDialog(activity, result.errorCode, 0).show()

            // 다이얼로그 대신 로그 띄움.
            Log.d("TAG", GoogleApiAvailability.getInstance().getErrorString(result.errorCode))
            return
        }

        // 해결된 오류에 대한 후 처리
        //   -> 보통 authorization을 다시 시도하면서 알아서 해결함.
        // 안되면 try catch로 에러 호출하고 앱 사망 방지
        try {
            result.startResolutionForResult(activity /*해결된 오류에 대해 다시 시도할 액티비티*/, REQUEST_CODE_RESOLUTION /*onActivityResult 로 보내는 request code*/)
        } catch (e: IntentSender.SendIntentException) {
            Log.e(TAG, "start resolution for result exception", e)
        }
    }
}