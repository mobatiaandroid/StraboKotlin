package com.vkc.strabo.fcmservice

import com.google.firebase.messaging.FirebaseMessagingService
import android.content.Intent
import android.graphics.Bitmap
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import android.annotation.TargetApi
import com.vkc.strabo.activity.HomeActivity
import android.graphics.BitmapFactory
import com.vkc.strabo.R
import com.vkc.strabo.fcmservice.NotificationID
import android.app.PendingIntent
import android.media.RingtoneManager
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import android.os.Build
import android.app.NotificationChannel
import android.app.TaskStackBuilder
import android.util.Log
import com.vkc.strabo.fcmservice.MyFirebaseMessagingService
import com.vkc.strabo.manager.AppPrefenceManager
import org.json.JSONException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Bibin Johnson
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {
    var intent: Intent? = null
    var bitmap: Bitmap? = null
    var mType: String? = null

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        // Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
//            String questionTitle = data.get("questionTitle").toString();
            try {
                val json = JSONObject(remoteMessage.data.toString().replace("=".toRegex(), ":"))
                handleDataMessage(json)
            } catch (e: Exception) {
                //     Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
//            sendNotification(remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.notification!!.body)
            //   Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @TargetApi(26)
    private fun sendNotification(message: String?) {
        var intent: Intent? = null
        //   LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent);
        intent = Intent(this, HomeActivity::class.java)
        intent.action = java.lang.Long.toString(System.currentTimeMillis())

// Create the TaskStackBuilder and add the intent, which inflates the back stack
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntentWithParentStack(intent)
        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.noti_icon)
        val notId = NotificationID.iD
        val pendingIntent = stackBuilder.getPendingIntent(notId, PendingIntent.FLAG_UPDATE_CURRENT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this)
            .setContentTitle(resources.getString(R.string.app_name))
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val CHANNEL_ID = getString(R.string.app_name) + "_01" // The id of the channel.
            val name: CharSequence =
                getString(R.string.app_name) // The user-visible name of the channel.
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            builder.setChannelId(mChannel.id)
            mChannel.setShowBadge(true)
            mChannel.canShowBadge()
            mChannel.enableLights(true)
            mChannel.lightColor = resources.getColor(R.color.colorPrimary)
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(mChannel)
        }
        if (Build.VERSION.SDK_INT >= 23) {
            builder.setLargeIcon(largeIcon)
            builder.setSmallIcon(R.drawable.noti_icon)
            builder.color = resources.getColor(R.color.colorPrimary)
        } else {
            builder.setSmallIcon(R.drawable.noti_icon)
        }
        notificationManager.notify(notId, builder.build())
    }

    private fun handleDataMessage(json: JSONObject) {
        // Log.e(TAG, "push json: " + json.toString());
        try {
            val data = json.getJSONObject("body")
            val message = data.optString("message")
            val title = data.optString("title")
            val image = data.optString("image")
            if (image.length > 0) {
                bitmap = getBitmapfromUrl(image)
            }
            sendNotification(message)
        } catch (e: JSONException) {
            Log.e(TAG, "Json Exception: " + e.message)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }
    }

    fun getBitmapfromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            null
        }
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        AppPrefenceManager.saveToken(applicationContext, s)
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        const val FCM_PARAM = "picture"
        private const val CHANNEL_NAME = "FCM"
        private const val CHANNEL_DESC = "Firebase Cloud Messaging"
    }
}