package com.example.hiragana

import android.app.Notification
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import com.pushwoosh.notification.PushMessage
import com.pushwoosh.notification.PushwooshNotificationFactory
import android.widget.RemoteViews

class YourNotificationFactory : PushwooshNotificationFactory() {
    override fun onGenerateNotification(pushMessage: PushMessage): Notification? {
        // customNotification condition can be changed to your logic
        // Currently customNotification is always true
        val customNotification = true
        if (customNotification) {
            // Make a RemoteView
            val remoteViews = RemoteViews(getApplicationContext()?.packageName ?: "com.example.hiragana", R.layout.custom_notification_layout)
            Log.d("YourNotificationFactory", "Using custom notification view")
            // Set the title text
            remoteViews.setTextViewText(R.id.notification_title, pushMessage.header)
            // Set the title color
            remoteViews.setTextColor(R.id.notification_title, Color.RED) // Change RED to your desired color
            val channelId = addChannel(pushMessage)
            val builder = getApplicationContext()?.let { context ->
                NotificationCompat.Builder(context, channelId)
                    .setContentTitle(pushMessage.header)
                    .setContentText(pushMessage.message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContent(remoteViews)
            }
            // Return the created notification
            return builder?.build()
        }
        // Return default logic
        Log.d("Default logic", "Default logic used")
        return super.onGenerateNotification(pushMessage)
    }
}