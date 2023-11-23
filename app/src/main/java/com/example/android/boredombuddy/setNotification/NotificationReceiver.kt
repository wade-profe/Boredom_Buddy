package com.example.android.boredombuddy.setNotification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import com.example.android.boredombuddy.data.Suggestion
import com.example.android.boredombuddy.utils.getNotificationManager
import com.example.android.boredombuddy.utils.sendNotification

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if(intent?.action == "com.example.android.boredombuddy.FIRE_NOTIFICATION"){
            val requestCode = intent.getIntExtra("requestCode", 0)
            val suggestion = intent.parcelable<Suggestion>("suggestion")

            // TODO find a way to update the db for the suggestion so that notificationTime is reset to null
            context?.getNotificationManager()?.sendNotification(context, requestCode, suggestion!!)
        }
    }

    private inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(
            key,
            T::class.java
        )

        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }
}