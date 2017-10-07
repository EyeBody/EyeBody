package com.example.android.eyebody.googleDrive

import android.view.View
import android.widget.TextView
import com.example.android.eyebody.R
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.Drive

/**
 * Created by Yoon on 2017-10-07.
 */



class GoogleDriveManager {
/*    val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
    val REQUEST_CODE_RESOLUTION = 3
    val WIFI_MODE = 1
    val DATA_MODE = 2
    val SIGN_IN = 1
    val SIGN_OUT = 2*/

    var view : View? = null
    var mGoogleApiClient: GoogleApiClient? = null
    var statusText : TextView? = null
    var statusWifiOrData = WIFI_MODE
    var statusSignInOrOut = SIGN_OUT


    constructor(view : View) {

        this.view = view
        statusText = view.findViewById<TextView>(R.id.googleDrive_status)

        /*mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()*/

        status()
    }

    companion object {
        val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
        val REQUEST_CODE_RESOLUTION = 3
        val WIFI_MODE = 1
        val DATA_MODE = 2
        val SIGN_IN = 1
        val SIGN_OUT = 2
    }

    fun signIn(){

    }

    fun signOut(){

    }

    fun saveAllFile(){

    }

    fun loadAllFile(){

    }

    fun status(){
        if(mGoogleApiClient?.isConnected ?: false){
            statusText?.text = "연결 된 상태"
        } else if (mGoogleApiClient?.isConnecting ?: false) {
            statusText?.text = "연결 중"
        } else {
            statusText?.text = "연결 안 됨"
        }
    }

}