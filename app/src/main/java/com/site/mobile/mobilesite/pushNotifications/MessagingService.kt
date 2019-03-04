package com.site.mobile.mobilesite.pushNotifications

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MessagingService : FirebaseMessagingService() {

    lateinit var pushNotification: PushNotifications

    override fun onCreate() {

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        pushNotification = PushNotifications(notificationManager, PushNotificationItemResolver(), DefaultNotificationBuilder())

        super.onCreate()
    }

    override fun onNewToken(token: String?) {
        token?.let {
            Log.d("pushToken", it)
        }
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        pushNotification.show(applicationContext, remoteMessage)
    }
}