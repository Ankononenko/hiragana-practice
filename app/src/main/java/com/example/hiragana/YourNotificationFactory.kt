package com.example.hiragana

import android.app.Notification
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
        if (customNotification && getLargeIcon(pushMessage) != null) {
            val largeIcon = getLargeIcon(pushMessage)
            // Make a RemoteView by using the largeIcon from PW control panel
            val remoteViews = RemoteViews(getApplicationContext()?.packageName ?: "com.example.hiragana", R.layout.custom_notification_layout)
            remoteViews.setImageViewBitmap(R.id.your_image_view_id, largeIcon)
            Log.d("YourNotificationFactory", "Using custom notification view")
            val channelId = addChannel(pushMessage)
            val builder = getApplicationContext()?.let { context ->
                NotificationCompat.Builder(context, channelId)
                    .setContentTitle(pushMessage.header)
                    .setContentText(pushMessage.message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContent(remoteViews)
            }
            // bigPicture is displayed when notification is tapped
            if (pushMessage.bigPictureUrl != null) {
                Log.d("bigPictureUrl", "bigPucture set")
                val bigPicture = getBigPicture(pushMessage)
                if (builder != null) {
                    builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bigPicture))
                }
            }
            // Return the created notification
            return builder?.build()
        }
        // Return default logic
        Log.d("Default logic", "Default logic used")
        return super.onGenerateNotification(pushMessage)
    }
}