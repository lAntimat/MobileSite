package com.site.mobile.mobilesite.pushNotifications

import android.app.Notification
import android.content.Context
import android.support.v4.app.NotificationCompat
import com.bumptech.glide.Glide
import com.site.mobile.mobilesite.STYLE_BIG_IMAGE
import com.site.mobile.mobilesite.STYLE_BIG_TEXT
import com.site.mobile.mobilesite.STYLE_SIMPLE

interface NotificationBuilder {

    fun build(context: Context, item: PushNotificationItem): Notification
}

class DefaultNotificationBuilder : NotificationBuilder {

    override fun build(context: Context, item: PushNotificationItem): Notification {
        return when (item.notificationStyle()) {
            STYLE_SIMPLE -> buildSimple(context, item)
            STYLE_BIG_TEXT -> buildWithBigText(context, item)
            STYLE_BIG_IMAGE -> buildWithBigImage(context, item)
            else -> buildWithBigText(context, item)
        }
    }

    private fun buildSimple(context: Context, item: PushNotificationItem): Notification {
        val builder = getBuilder(context, item)

        if (item.imageUrl().isNotEmpty()) {
            val futureTarget = Glide.with(context)
                    .asBitmap()
                    .load(item.imageUrl())
                    .submit()

            val bitmap = futureTarget.get()
            builder.setLargeIcon(bitmap)

            Glide.with(context).clear(futureTarget)
        }

        return builder.build()
    }

    private fun buildWithBigImage(context: Context, item: PushNotificationItem): Notification {
        val builder = getBuilder(context, item)

        if (item.imageUrl().isNotEmpty()) {
            val futureTarget = Glide.with(context)
                    .asBitmap()
                    .load(item.imageUrl())
                    .submit()

            val bitmap = futureTarget.get()

            builder.setLargeIcon(bitmap)
            builder.setStyle(NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .bigLargeIcon(null))

            Glide.with(context).clear(futureTarget)
        }

        return builder.build()
    }

    private fun buildWithBigText(context: Context, item: PushNotificationItem): Notification {

        val builder = getBuilder(context, item)


        if (item.imageUrl().isNotEmpty()) {
            val futureTarget = Glide.with(context)
                    .asBitmap()
                    .load(item.imageUrl())
                    .submit()

            val bitmap = futureTarget.get()
            builder.setLargeIcon(bitmap)
            Glide.with(context).clear(futureTarget)
        }

        builder.setStyle(NotificationCompat.BigTextStyle()
                .bigText(item.message())
        )

        return builder.build()
    }

    private fun getBuilder(context: Context, item: PushNotificationItem): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, item.channel().channelId)
                .setSmallIcon(item.smallIcon())
                .setContentTitle(item.title())
                .setContentText(item.message())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(item.pendingIntent())
    }

}