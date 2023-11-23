package com.example.android.boredombuddy.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.android.boredombuddy.BuildConfig
import com.example.android.boredombuddy.MainActivity
import com.example.android.boredombuddy.R
import com.example.android.boredombuddy.data.Suggestion
import com.example.android.boredombuddy.setNotification.NotificationReceiver
import com.squareup.picasso.Picasso
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID + ".channel"

fun Context.getNotificationManager(): NotificationManager {
    return getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}

fun Context.getAlarmManager(): AlarmManager {
    return getSystemService(Context.ALARM_SERVICE) as AlarmManager
}

@OptIn(DelicateCoroutinesApi::class)
fun NotificationManager.sendNotification(
    context: Context,
    requestCode: Int,
    suggestion: Suggestion
) {

    lateinit var image: Bitmap

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        && getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null
    ) {

        val name = context.getString(R.string.notification_channel_name)
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        createNotificationChannel(channel)
    }


    val contentIntent = PendingIntent.getActivity(
        context,
        0,
        Intent(context, MainActivity::class.java),
        PendingIntent.FLAG_IMMUTABLE
    )

    GlobalScope.launch(Dispatchers.IO) {
        image = try {
            Picasso.with(context).load(suggestion.imageUrl).resize(1440, 720).get()
        } catch (e: Exception) {
            e.printStackTrace()
            ContextCompat.getDrawable(context, R.drawable.error_image)!!.toBitmap(1440, 720)
        }

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(context.getString(R.string.notification_channel_name))
            .setContentText(suggestion.activity)
            .setSmallIcon(R.drawable.notification_icon)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(image)
            )
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .build()

        notify(requestCode, notification)
    }

}

fun Context.scheduleNotification(
    suggestion: Suggestion,
    notificationTimeInMillis: Long
) {
    val alarmManager = this.getAlarmManager()
    val pendingIntent = makePendingIntent(this, suggestion)
    alarmManager.set(AlarmManager.RTC, notificationTimeInMillis, pendingIntent)
}

fun makePendingIntent(context: Context, suggestion: Suggestion): PendingIntent {
    val requestCode = suggestion.id.toInt()
    return PendingIntent.getBroadcast(
        context,
        requestCode,
        Intent(context, NotificationReceiver::class.java).apply {
            action = "com.example.android.boredombuddy.FIRE_NOTIFICATION"
            putExtra("suggestion", suggestion)
            putExtra("requestCode", requestCode)
        },
        PendingIntent.FLAG_IMMUTABLE
    )
}

