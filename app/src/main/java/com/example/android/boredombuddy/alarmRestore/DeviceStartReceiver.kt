package com.example.android.boredombuddy.alarmRestore

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class DeviceStartReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == Intent.ACTION_BOOT_COMPLETED){
            Log.d("DeviceStartReceiver", "Boot completed event caught")
            try{
                Log.d("DeviceStartReceiver", "Launching AlarmRestoreService")
                val workRequest = OneTimeWorkRequestBuilder<AlarmRestoreWorker>().build()
                WorkManager.getInstance(context!!).enqueue(workRequest)
            } catch (e: Exception){
                Log.d("DeviceStartReceiver", "Something went wrong/n${e.printStackTrace()}")
            }
        }
    }

}