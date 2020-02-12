package ak.android.movieshighlight.notification

import ak.android.movieshighlight.R
import ak.android.movieshighlight.common.log
import ak.android.movieshighlight.main.MainActivity
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
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.*

class NotificationReceiver : BroadcastReceiver() {
    companion object {
        private const val ID_REMINDER = 100
        const val ID_NEW_RELEASE = 101
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            // Set daily reminder
            setDailyNotification(context)
            // Set daily new release
            setDailyRelease(context)
        }

        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val notificationId = intent.getIntExtra("id", 0)
        val notificationTitle = intent.getStringExtra("title")
        val notificationContent = intent.getStringExtra("content")

        if (notificationId == ID_REMINDER) {
            showNotification(context, notificationTitle, notificationContent, notificationId)
        } else {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = OneTimeWorkRequestBuilder<NewReleaseWorker>()
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(context).enqueue(request)
        }
    }

    fun showNotification(
        context: Context, title: String, content: String, notificationId: Int
    ) {
        val (channelId, channelName) = if (notificationId == ID_REMINDER) {
            "reminder" to "Daily Reminder (08.00)"
        } else {
            "release" to "New Release"
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, channelId).apply {
            color = ContextCompat.getColor(context, android.R.color.transparent)

            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(title)
            setContentIntent(
                PendingIntent.getActivity(
                    context,
                    notificationId,
                    Intent(context, MainActivity::class.java),
                    PendingIntent.FLAG_ONE_SHOT
                )
            )
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            setAutoCancel(true)

            if (notificationId == ID_REMINDER) {
                setContentText(content)
            } else {
                val first2 = content.split("\n")

                if (first2.size > 2) {
                    setContentText("${first2[0]}, ${first2[1]}, and more...")
                    setStyle(NotificationCompat.BigTextStyle().bigText(content))
                } else {
                    if (first2.size == 2) {
                        setContentText("${first2[0]} and ${first2[1]}")
                    } else if (first2.size == 1) {
                        setContentText(first2[0])
                    }
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).also {
                    it.enableVibration(true)
                    notificationManager.createNotificationChannel(it)
                }
            }
        }.build()
        notificationManager.notify(notificationId, notification)

        log("showing notification $notificationId")
    }

    fun setDailyNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
            .putExtra("title", context.getString(R.string.notification_daily_title))
            .putExtra("content", context.getString(R.string.notification_daily_content))
            .putExtra("id", ID_REMINDER)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 7)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if (alarmIsNotActive(context, ID_REMINDER, intent)) {
            val pendingIntent =
                PendingIntent.getBroadcast(
                    context, ID_REMINDER, intent, PendingIntent.FLAG_ONE_SHOT
                )
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )

            log("Daily notification set")
        }
    }

    fun setDailyRelease(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if (alarmIsNotActive(context, ID_NEW_RELEASE, intent)) {
            val pendingIntent =
                PendingIntent.getBroadcast(
                    context, ID_NEW_RELEASE, intent, PendingIntent.FLAG_ONE_SHOT
                )
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
            log("Daily release set")
        }
    }

    private fun alarmIsNotActive(context: Context, notificationId: Int, intent: Intent): Boolean =
        PendingIntent
            .getBroadcast(context, notificationId, intent, PendingIntent.FLAG_NO_CREATE) != null
}
