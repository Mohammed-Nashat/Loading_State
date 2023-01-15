package com.udacity.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.DetailActivity
import com.udacity.R


fun NotificationManager.sendNotification(
    messageBody: String, applicationContext: Context,nameOfFile: String,status: String) {

    val intentOfDetailActivity = Intent(applicationContext,DetailActivity::class.java)
    intentOfDetailActivity.putExtra("nameOfFile",nameOfFile)
    intentOfDetailActivity.putExtra("status",status)

    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        Constants.REQUEST_CODE,
        intentOfDetailActivity,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder =
        NotificationCompat.Builder(applicationContext,  Constants.CHANNEL_ID)

        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setAutoCancel(true)

        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.notification_button),
            pendingIntent)

    notify(Constants.NOTIFICATION_ID, builder.build())
}




fun createNotificationChannel(channelId: String, channelName: String , context: Context){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val notificationChannel =
            NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_LOW)
            .apply {
                enableLights(true)
                enableVibration(true)
                lightColor = Color.RED
                description = "Description"
                setShowBadge(false)
            }

        val notificationManager = ContextCompat.getSystemService(context,NotificationManager::class.java)as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    }





