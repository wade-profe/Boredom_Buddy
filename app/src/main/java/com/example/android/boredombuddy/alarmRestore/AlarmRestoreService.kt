package com.example.android.boredombuddy.alarmRestore

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.android.boredombuddy.data.SuggestionRepository
import com.example.android.boredombuddy.data.local.SuggestionDao


class AlarmRestoreService(private val suggestionDao: SuggestionDao): Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
        // TODO Use DB query to return all suggestions with future-dated reminders and then use AlarmService method to set reminders for each
        // Alternative: use repo and call repo method which retrieves future-dated reminders and creates them with AlarmManager
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}