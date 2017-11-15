package com.example.android.eyebody.exercise

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import com.example.android.eyebody.MainActivity
import com.example.android.eyebody.R

class NotiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noti)
    }

    fun showBasicNotification() {
        var mBuilder = createNotification()
        mBuilder.setContentIntent(createPendingIntent())

        var mNotificationManager:NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(1, mBuilder.build())
    }

    fun notification() {
        val mBuilder: NotificationCompat.Builder = createNotification()

        var remoteViews = RemoteViews(packageName, R.layout.custom_notification)
        remoteViews.setImageViewResource(R.id.img, R.mipmap.ic_launcher)
        remoteViews.setTextViewText(R.id.title, "Title")
        remoteViews.setTextViewText(R.id.message, "message")

        mBuilder.setContent(remoteViews)
        mBuilder.setContentIntent(createPendingIntent())

        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(1, mBuilder.build())
    }

    private fun createPendingIntent(): PendingIntent {
        var resultIntent = Intent(this, NotiActivity::class.java)
        var stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)

        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun createNotification(): NotificationCompat.Builder
    {
        var icon: Bitmap = BitmapFactory . decodeResource (resources, R.mipmap.ic_launcher)
        var builder: NotificationCompat.Builder =  NotificationCompat.Builder(baseContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icon)
                .setContentTitle("StatusBar Title")
                .setContentText("StatusBar subTitle")
                .setSmallIcon(R.mipmap.ic_launcher/*스와이프 전 아이콘*/)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
        }
        return builder
    }
}