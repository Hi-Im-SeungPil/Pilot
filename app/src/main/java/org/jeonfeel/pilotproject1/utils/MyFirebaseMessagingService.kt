package org.jeonfeel.pilotproject1.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.jeonfeel.pilotproject1.R
import org.jeonfeel.pilotproject1.view.activity.FcmBoxActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = MyFirebaseMessagingService::class.java.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.e(TAG, "remoteMessage.data[\"title\"] => ${remoteMessage.data["title"].toString()}")
        Log.e(TAG, "remoteMessage.data[\"body\"] => ${remoteMessage.data["body"].toString()}")

        sendNotification(remoteMessage)
    }

    override fun onNewToken(str: String) {
        super.onNewToken(str)

        Log.e(TAG, "token => $str")
//        sendSever()
    }

//    private fun getToken() {
//        FirebaseMessaging.getInstance().token.addOnCompleteListener OnCompleteListener@{ task ->
//            if (!task.isSuccessful) {
//                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//            val token = task.result
//            Log.d(TAG, token)
//        }
//    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val messageTitle = remoteMessage.data["title"]
        val messageBody = remoteMessage.data["body"]

//        startActivity(intent)
//        val intent = Intent(this,FcmBoxActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val intent = Intent(this, Class.forName(remoteMessage.data["link"]!!))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

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