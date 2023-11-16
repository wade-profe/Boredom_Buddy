package com.example.android.boredombuddy.alarmRestore

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.getKoin
class DeviceStartReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == Intent.ACTION_BOOT_COMPLETED){
            val alarmRestoreIntent = Intent(context, AlarmRestoreService::class.java)
            context?.startService(alarmRestoreIntent)
        }
    }

}