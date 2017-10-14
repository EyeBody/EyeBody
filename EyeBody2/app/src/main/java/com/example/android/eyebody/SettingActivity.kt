package com.example.android.eyebody

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.drive.Drive
import com.google.android.gms.common.api.GoogleApiClient
import android.content.IntentSender
import android.view.MotionEvent
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.common.GoogleApiAvailability
import java.util.concurrent.TimeUnit


/*
   ******* 임시로 구글드라이브 설정을 여기서 함. *******
비밀번호 찾기, 알림 설정 등
 */
class SettingActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private var mGoogleApiClient: GoogleApiClient? = null
    private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
    private val REQUEST_CODE_RESOLUTION = 3
    var statusText : TextView? = null

    fun status(){
        if(mGoogleApiClient?.isConnected ?: false){
            statusText?.text = "연결 된 상태"
        } else if (mGoogleApiClient?.isConnecting ?: false) {
            statusText?.text = "연결 중"
        } else {
            statusText?.text = "연결 안 됨"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        statusText = findViewById<TextView>(R.id.googleDrive_status)

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()

        status()
    }

    override fun onStart() {
        super.onStart()
        /* 원래 connect 해야되는 곳 */
        status()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if(mGoogleApiClient?.isConnected ?: false){
            mGoogleApiClient?.clearDefaultAccountAndReconnect()
        } else if (mGoogleApiClient?.isConnecting ?: false){

        } else {
            mGoogleApiClient?.connect()
        }
        status()
        return super.onTouchEvent(event)
    }

    override fun onResume() {
        super.onResume()
        status()
        //일단은 연동만
    }

    override fun onConnected(p0: Bundle?) {
        Toast.makeText(this,"구글드라이브 연결되었음",Toast.LENGTH_LONG).show()
        status()
    }

    override fun onConnectionSuspended(p0: Int) {
        Toast.makeText(this,"구글드라이브 연결죽음..",Toast.LENGTH_LONG).show()
        status()
    }

    override fun onConnectionFailed(connResult: ConnectionResult) {
        status()
        if (connResult.hasResolution()) {
            try {
                connResult.startResolutionForResult(this, REQUEST_CODE_RESOLUTION)
            } catch (e: IntentSender.SendIntentException) {
                // Unable to resolve, message user appropriately
            }

        } else {
            GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, connResult.errorCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                    .show()
        }
    }

}
