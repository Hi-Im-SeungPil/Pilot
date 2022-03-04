package org.jeonfeel.pilotproject1.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.jeonfeel.pilotproject1.R
import org.jeonfeel.pilotproject1.view.activity.MainActivity

class FirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = FirebaseMessagingService::class.java.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.e(TAG, "remoteMessage.data[\"title\"] => ${remoteMessage.data["title"].toString()}")
        Log.e(TAG, "remoteMessage.data[\"body\"] => ${remoteMessage.data["body"].toString()}")

        sendNotification(remoteMessage)
    }

    override fun onNewToken(str: String) {
        super.onNewToken(str)

        Log.e(TAG, "token => $str")
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val messageTitle = remoteMessage.data["title"]
        val messageBody = remoteMessage.data["body"]
        val link = remoteMessage.data["link"] ?: ""

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("link",link)

        val pendingIntent = PendingIntent.getActivity(this, 11, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.img_circle_2x)
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setSound(defaultSoundUri)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Channel human readable title",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0, notificationBuilder.build())
    }
}