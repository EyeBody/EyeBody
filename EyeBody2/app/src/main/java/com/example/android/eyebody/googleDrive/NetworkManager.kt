package com.example.android.eyebody.googleDrive

import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager

/**
 * Created by Yoon on 2017-11-19
 */
class NetworkManager {

    companion object {

        private fun networkInfo(context: Context)   //: NetworkInfo!
                = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo

        fun isConnected(context: Context)   //: Boolean
                = networkInfo(context).isConnected

        fun isConnectedAndConnecting(context: Context)  //: Boolean
                = networkInfo(context).isConnectedOrConnecting

        fun isConnectedWithWifi(context: Context): Boolean {
            val info = networkInfo(context)
            return info.isConnected && info.type == ConnectivityManager.TYPE_WIFI
        }

        fun isConnectedWithMobile(context: Context): Boolean {
            val info = networkInfo(context)
            return info.isConnected && info.type == ConnectivityManager.TYPE_MOBILE
        }
    }
}
