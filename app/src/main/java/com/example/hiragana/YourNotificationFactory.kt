package com.example.hiragana

import android.app.Notification
import android.graphics.Color
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.pushwoosh.notification.PushMessage
import com.pushwoosh.notification.PushwooshNotificationFactory


class YourNotificationFactory : PushwooshNotificationFactory() {
    override fun onGenerateNotification(pushMessage: PushMessage): Notification? {
        val customNotification = true
        if (customNotification) {
            val largeIcon = getLargeIcon(pushMessage)
            val remoteViews = RemoteViews(applicationContext!!.packageName, com.example.hiragana.R.layout.custom_notification_layout)
            // Get a picture from the server (not yet using)
            // remoteViews.setImageViewBitmap(R.id.rewards_image, largeIcon);
            remoteViews.setTextViewText(com.example.hiragana.R.id.rewards_message, pushMessage.message)
            Log.d("YourNotificationFactory", "Using custom notification view")
            val channelId = addChannel(pushMessage)
            Log.d("Channel ID", channelId)
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(
                applicationContext!!, channelId
            )
                .setContentTitle(pushMessage.header)
                .setContentText(pushMessage.message)
                .setSmallIcon(com.example.hiragana.R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setColor(Color.parseColor("#00000000"))
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(remoteViews)
            if (pushMessage.bigPictureUrl != null) {
                Log.d("bigPictureUrl", "bigPicture set")
                val bigPicture = getBigPicture(pushMessage)
                if (builder != null) {
                    builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bigPicture))
                }
            }
            if (builder != null) {
                return builder.build()
            }
        }
        Log.d("Default", "Default used")
        return super.onGenerateNotification(pushMessage)
    }
}