package com.example.android.eyebody.exercise

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Created by ytw11 on 2017-11-19.
 */
class ActionReceiver : BroadcastReceiver() {
    override fun onReceive(context:Context ,  intent:Intent) {

        //Toast.makeText(context,"recieved",Toast.LENGTH_SHORT).show();
        var action = intent.getStringExtra("action")
        if (action.equals("action1")) {
            performAction1()
        } else if (action.equals("action2")) {
            performAction2()
        }
        //This is used to close the notification tray
        var it = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        context.sendBroadcast(it)
    }

    fun performAction1() {

    }

    fun performAction2() {

    }

}