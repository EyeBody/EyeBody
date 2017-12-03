package com.example.android.eyebody.exercise

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT

/**
 * Created by ytw11 on 2017-11-19.
 */
class ActionReceiver : BroadcastReceiver() {

    var menu:String=""
    override fun onReceive(context:Context ,  intent:Intent) {
        val dbHelper = DbHelper(context, "bill.db", null, 1)
        val menu = intent.getStringExtra("menu")
        val time=intent.getStringExtra("time")
        val price=intent.getIntExtra("price",0)
        when (menu) {
            "meal" -> mealClicked(context)
            "beverage" -> beverageClicked(context)
            "dessert" -> dessertClicked(context)
            "cancel" ->cancelClicked(context)
        }
        putValuesInDb(dbHelper,time,menu,price)
    }
    private fun mealClicked(context: Context) {
        menu="meal"
        Toast.makeText(context,menu, LENGTH_SHORT).show()
    }
    private fun beverageClicked(context: Context) {
        menu="beverage"
        Toast.makeText(context,menu, LENGTH_SHORT).show()
    }
    private fun dessertClicked(context: Context){
        menu="dessert"
        Toast.makeText(context,menu, LENGTH_SHORT).show()
    }
    private fun cancelClicked(context:Context){
        menu="cancel"
        Toast.makeText(context,menu, LENGTH_SHORT).show()
    }
    private fun putValuesInDb(dbHelper: DbHelper,time:String,menu:String,price:Int){
        if(menu!="cancel"){
            dbHelper.insert(time,menu,price)
        }
    }
}