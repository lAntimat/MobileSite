package com.site.mobile.mobilesite.pushNotifications


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class PushNotifications constructor(var notificationManager: NotificationManager,
                                    var resolver: NotificationItemResolver,
                                    var notificationBuilder: NotificationBuilder) {


    fun show(context: Context, remoteMessage: RemoteMessage) {
        //val id = remoteMessage.data["id"]?.hashCode() ?: 0
        val id = Date().time.toInt()
        val notification = resolver.resolve(context, remoteMessage.data)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!notificationChannelExists(notification.channel().channelId)) {
                createChannel(context, notification.channel())
            }
        }
        notificationManager.notify(id, notificationBuilder.build(context, notification))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(context: Context, channel: PushNotificationChannel) {
        val channelTitle = channel.titleResource
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(channel.channelId, channelTitle, importance)
        notificationChannel.setShowBadge(true)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(notificationChannel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun notificationChannelExists(channelId: String): Boolean =
            notificationManager.getNotificationChannel(channelId) != null
}