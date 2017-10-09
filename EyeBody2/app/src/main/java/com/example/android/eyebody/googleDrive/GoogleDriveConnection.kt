package com.example.android.eyebody.googleDrive

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient

/**
 * Created by YOON on 2017-10-09.
 */
@Suppress("UNREACHABLE_CODE") // TODO 이거 뭐지
class GoogleDriveConnection : GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionSuspended(p0: Int) {
        Log.d("mydbg_googledrive", "구글드라이브 해제 콜백")
        TODO("not implemented")
    }

    override fun onConnected(p0: Bundle?) {
        Log.d("mydbg_googledrive", "구글드라이브 연결 콜백")
        TODO("not implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.d("mydbg_googledrive", "구글드라이브 connection failed 리스너")
        TODO("not implemented")
    }
}