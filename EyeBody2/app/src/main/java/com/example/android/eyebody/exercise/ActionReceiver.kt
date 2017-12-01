package com.example.android.eyebody.exercise

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG

/**
 * Created by ytw11 on 2017-11-19.
 */
class ActionReceiver : BroadcastReceiver() {
    var returnIntent=Intent()
    override fun onReceive(context:Context ,  intent:Intent) {
        //Toast.makeText(context,"recieved",Toast.LENGTH_SHORT).show();
        var action = intent.getStringExtra("action")
        when (action) {
            "meal" -> mealClicked(context)
            "beverage" -> beverageClicked(context)
            "dessert" -> dessertClicked(context)
            "cancel" ->cancelClicked(context)
        }
    }
    private fun mealClicked(context: Context) {
        Toast.makeText(context,"meal",LENGTH_LONG).show()
    }
    private fun beverageClicked(context: Context) {

        Toast.makeText(context,"beverage",LENGTH_LONG).show()

    }
    private fun dessertClicked(context: Context){

        Toast.makeText(context,"dessert",LENGTH_LONG).show()
    }
    private fun cancelClicked(context:Context){
        Toast.makeText(context,"cancel", LENGTH_LONG).show()
    }
}