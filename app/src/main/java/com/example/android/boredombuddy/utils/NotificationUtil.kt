package com.example.android.boredombuddy.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.android.boredombuddy.BuildConfig
import com.example.android.boredombuddy.MainActivity
import com.example.android.boredombuddy.R
import com.example.android.boredombuddy.data.Suggestion
import com.example.android.boredombuddy.setNotification.NotificationReceiver
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext


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

    Log.d("WADE", "Send notification hit")


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        && getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null
    ) {
        Log.d("WADE", "Building notification channel")

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
            Log.d("WADE", "Trying to download image")
            Picasso.with(context).load(suggestion.imageUrl).resize(1440, 720).get()
        } catch (e: Exception) {
            Log.d("WADE", "Exception in image download attempt")
            e.printStackTrace()
            ContextCompat.getDrawable(context, R.drawable.error_image)!!.toBitmap(256, 256)
        }

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(context.getString(R.string.notification_channel_name))
            .setContentText(suggestion.activity)
            .setSmallIcon(R.drawable.notification_icon)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(image))
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .build()

        Log.d("WADE", "Sending notification")

        notify(requestCode, notification)
    }

}

fun AlarmManager.scheduleNotification(
    context: Context,
    suggestion: Suggestion,
    notificationTimeInMillis: Long
) {

    Log.d(
        "SN(alarmManager)",
        "Method hit. suggestion: ${suggestion.activity} and timeinMillis: $notificationTimeInMillis"
    )
    Log.d("SN(alarmManager)", "context variable: $context")


    val requestCode = suggestion.id.toInt()
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        Intent(context, NotificationReceiver::class.java).apply {
            putExtra("suggestion", suggestion)
            putExtra("requestCode", requestCode)
        },
        PendingIntent.FLAG_IMMUTABLE
    )

    try {
        Log.d("SN(alarmManager)", "calling set method")
        set(AlarmManager.RTC, notificationTimeInMillis, pendingIntent)
        Log.d("SN(alarmManager)", "Made it past set method")
    } catch (e: Exception) {
        Log.d("SN(alarmManager)", e.stackTrace.toString())
    }

}

