package com.example.android.eyebody.exercise

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.R
import android.widget.TextView
import android.content.Intent
import android.R.string.cancel
import android.app.NotificationManager
import android.content.Context


class NotificationCustomView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.android.eyebody.R.layout.activity_notification_custom_view)
        // Create Notification Manager
        val notificationmanager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Dismiss Notification
        notificationmanager.cancel(0)

        // Retrive the data from MainActivity.java
        val i = intent

        title = i.getStringExtra("title")
    }
}
