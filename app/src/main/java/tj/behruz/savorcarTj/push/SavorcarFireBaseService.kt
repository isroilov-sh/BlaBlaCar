
package tj.behruz.savorcarTj.push

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import tj.behruz.savorcarTj.HomeActivity
import tj.behruz.savorcarTj.R
import java.util.*

class SavorcarFireBaseService: FirebaseMessagingService() {

    companion object {
        private const val TAG = "SavorcarFireBaseService"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.notification!=null){
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body
            val type = remoteMessage.data["type"]
            val tripId = remoteMessage.data["id"]
            sendNotification(PushPayload(title.toString(), body.toString(), type.toString(), tripId.toString()))

        }else{
            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]
            val type = remoteMessage.data["type"]
            val tripId = remoteMessage.data["id"]
            sendNotification(PushPayload(title.toString(), body.toString(), type.toString(), tripId.toString()))

        }



    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("DEVELOPER","token $token")
    }


    @SuppressLint("ServiceCast")
    private fun sendNotification(pushMessages: PushPayload) {
        val groupName = "default"
        val channelId = "savorcar"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationId: Int = ((Date().time.toInt() / 1000) % Integer.MAX_VALUE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "my_channel"
            val description = "This is my channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(channelId, name, importance)
            mChannel.description = description
            mChannel.enableLights(true)
            mChannel.lightColor = resources.getColor(R.color.purple_700)
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            mChannel.setShowBadge(false)
            notificationManager.createNotificationChannel(mChannel)
        }

        try {
            //Child notification
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId).setDefaults(Notification.DEFAULT_SOUND).setContentTitle(pushMessages.title).setContentText(pushMessages.body).setAutoCancel(true).setSound(defaultSoundUri).setVibrate(longArrayOf(0, 1000, 500, 1000)).setContentIntent(getPendingIntent(this,
                pushMessages,
                notificationId)).setPriority(Notification.PRIORITY_MAX).setGroup(groupName).setOnlyAlertOnce(true).setStyle(NotificationCompat.BigTextStyle().setBigContentTitle(pushMessages.title).bigText(pushMessages.body))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.color = resources.getColor(R.color.purple_500)
                builder.setSmallIcon(R.drawable.app_logo)
            }

            notificationManager.notify(notificationId, builder.build())

            // Summary of group notification
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val summaryGroupBuilder = NotificationCompat.Builder(this, channelId).setPriority(Notification.PRIORITY_MAX).setAutoCancel(true).setOnlyAlertOnce(true).setGroup(groupName).setContentIntent(getPendingIntent(this, pushMessages)).setGroupSummary(true)  //set this notification as the summary for the group
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    summaryGroupBuilder.color = resources.getColor(R.color.purple_500)
                    summaryGroupBuilder.setSmallIcon(R.drawable.app_logo)
                }
                notificationManager.notify(0, summaryGroupBuilder.build())
            }

        } catch (se: SecurityException) {
            se.printStackTrace()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    private fun getPendingIntent(context: Context, pushNotificationMessages: PushPayload, id: Int = 0): PendingIntent {
        val intent = Intent(context, HomeActivity::class.java)
        intent.putExtra("notification", pushNotificationMessages)
        return PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }


}