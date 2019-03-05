package com.site.mobile.mobilesite.pushNotifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.site.mobile.mobilesite.MainActivity
import com.site.mobile.mobilesite.R


class SimpleNotification(private val context: Context,
                         private val data: Map<String, String>) : PushNotificationItem {

    override fun channel() = PushNotificationChannel.SimpleChannel()

    override fun notificationStyle(): String = data["notificationStyle"] ?: ""

    override fun smallIcon(): Int = R.drawable.ic_launcher

    override fun title(): String = data["title"] ?: ""

    override fun message(): String = data["message"] ?: ""

    override fun imageUrl(): String {
        return getValidatedUrl(data["imgUrl"] ?: "")
    }

    override fun pendingIntent(): PendingIntent {
        var resultIntent = Intent()
        resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP


        val actionString = data["action"] ?: ""

        if (actionString.isNotEmpty()) {
            try {
                val action = JsonParser().parse(actionString).asJsonObject
                val actionType = action["type"].asString
                val parameters = action["parameters"].asJsonObject
                resultIntent = getIntentFromAction(actionType, parameters)
            } catch (exception: Exception) {

            }

        }

        //resultIntent.putExtra(SelectFundActivity.FRIEND_SUGGESTION, content)
        val requestID = System.currentTimeMillis().toInt()
        return PendingIntent.getActivity(context, requestID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    //filter only https pictures, because Glide crash with http in Android 8+
    private fun getValidatedUrl(url: String): String {
        return if (url.contains("https", true)) url
        else ""
    }

    private fun getIntentFromAction(actionType: String, param: JsonObject): Intent {
        var resultIntent = Intent()

        when (actionType) {
            "openUrl" -> {
                val url = param["url"].asString ?: ""
                if (url.isNotEmpty()) {
                    resultIntent = Intent(context, MainActivity::class.java)
                    resultIntent.putExtra("url", url)
                }
            }
        }

        return resultIntent
    }
}