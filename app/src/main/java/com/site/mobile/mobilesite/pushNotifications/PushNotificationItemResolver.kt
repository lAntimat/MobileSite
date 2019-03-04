package com.site.mobile.mobilesite.pushNotifications


import android.content.Context

interface NotificationItemResolver {

    fun resolve(context: Context, data: Map<String, String>): PushNotificationItem
}

class PushNotificationItemResolver : NotificationItemResolver {

    override fun resolve(context: Context, data: Map<String, String>): PushNotificationItem {
        return SimpleNotification(context, data)
    }

}