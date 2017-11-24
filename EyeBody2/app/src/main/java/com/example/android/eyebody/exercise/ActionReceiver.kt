package com.example.android.eyebody.exercise

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Created by ytw11 on 2017-11-19.
 */
class ActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        /*
        if (MEAL.equals(action)) {
            Log.v("shuffTest", "Pressed YES")
        } else if (MAYBE_ACTION.equals(action)) {
            Log.v("shuffTest", "Pressed NO")
        } else if (NO_ACTION.equals(action)) {
            Log.v("shuffTest", "Pressed MAYBE")
        }*/
    }
}