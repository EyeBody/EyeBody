package com.example.android.eyebody.exercise

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by ytw11 on 2017-11-19.
 */
class ActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        var menuSelect=intent.getStringExtra("action")
        if(menuSelect == "1"){

        }else if(menuSelect=="2"){

        }else if(menuSelect=="3"){

        }else if(menuSelect=="4"){

        }
        var it=Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        context.sendBroadcast(it)
    }
    //각각 버튼 눌릴때 함수 지정해줘야 한다.
}