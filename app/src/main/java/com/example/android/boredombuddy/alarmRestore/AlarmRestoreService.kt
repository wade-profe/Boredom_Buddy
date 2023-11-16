package com.example.android.boredombuddy.alarmRestore

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.android.boredombuddy.data.local.SuggestionDao
import com.example.android.boredombuddy.data.local.toDomainModel
import com.example.android.boredombuddy.utils.scheduleNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject


class AlarmRestoreService(private val serviceScope: CoroutineScope = CoroutineScope(Dispatchers.Main)): Service() {

    private val suggestionDao: SuggestionDao by inject()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        serviceScope.launch {
            val favouritesWithReminders = withContext(Dispatchers.IO){
                suggestionDao.getSuggestionsWithReminders()
            }
            if(!favouritesWithReminders.isNullOrEmpty()){
                favouritesWithReminders.forEach {suggestion ->
                    applicationContext.scheduleNotification(suggestion.toDomainModel(), suggestion.notificationTime!!)
                }
            }

            stopSelf()
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    // TODO regular alarms working, test alarm restore by settings alarm and restarting device, debug any issues

}