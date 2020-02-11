package ak.android.movieshighlight.notification

import ak.android.movieshighlight.R
import ak.android.movieshighlight.common.log
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import java.util.*

class NotificationReceiver : BroadcastReceiver() {
    companion object {
        private const val ID_REMINDER = 100
        private const val ID_NEW_RELEASE = 101
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
            val notificationId = intent.getIntExtra("id", 0)
            val notificationTitle = intent.getStringExtra("title")
            val notificationContent = intent.getStringExtra("content")

            showNotification(context, notificationTitle, notificationContent, notificationId)
        }
    }

    private fun showNotification(
        context: Context, title: String, content: String, notificationId: Int
    ) {
        val (channelId, channelName) = if (notificationId == ID_REMINDER) {
            "reminder" to "Daily Reminder (08.00)"
        } else {
            "release" to "New Release"
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, channelId).apply {
            color = ContextCompat.getColor(context, android.R.color.transparent)

            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(title)
            setContentText(content)
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT).also {
                    it.enableVibration(true)
                    notificationManager.createNotificationChannel(it)
                }
            }
        }.build()
        notificationManager.notify(notificationId, notification)
    }

    fun setDailyNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
            .putExtra("title", context.getString(R.string.notification_daily_title))
            .putExtra("content", context.getString(R.string.notification_daily_content))
            .putExtra("id", ID_REMINDER)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val pendingIntent =
            PendingIntent.getBroadcast(context, ID_REMINDER, intent, PendingIntent.FLAG_ONE_SHOT)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent
        )

        log("Daily notification set")
    }
}
