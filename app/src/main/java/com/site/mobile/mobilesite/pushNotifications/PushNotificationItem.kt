package com.site.mobile.mobilesite.pushNotifications

import android.app.PendingIntent

interface PushNotificationItem {

    fun channel(): PushNotificationChannel

    fun notificationStyle(): String

    fun title(): String

    fun message(): String

    fun smallIcon(): Int

    fun imageUrl(): String

    fun pendingIntent(): PendingIntent
}

sealed class PushNotificationChannel(val channelId: String, val titleResource: String) {

    class SimpleChannel : PushNotificationChannel("simple", "simpleNotifications")

    class Empty : PushNotificationChannel("simple", "Emptry")
}