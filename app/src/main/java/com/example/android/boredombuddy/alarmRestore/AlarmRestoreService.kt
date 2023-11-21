package com.example.android.boredombuddy.alarmRestore

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.boredombuddy.data.local.SuggestionDao
import com.example.android.boredombuddy.data.local.toDomainModel
import com.example.android.boredombuddy.utils.scheduleNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent

class AlarmRestoreWorker(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {

    private val suggestionDao: SuggestionDao by KoinJavaComponent.inject(SuggestionDao::class.java)

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        Log.d("AlarmRestoreWorker", "doWork method hit")

        try{
            val favouritesWithReminders =
                suggestionDao.getSuggestionsWithReminders()

            Log.d("AlarmRestoreWorker", "Favourites with reminders variable - $favouritesWithReminders")
            if (favouritesWithReminders.isNotEmpty()) {
                Log.d("AlarmRestoreWorker", "Query not empty, attempting to set reminders")
                favouritesWithReminders.forEach { suggestion ->
                    Log.d("AlarmRestoreWorker", "Resetting reminder for ${suggestion.activity}")
                    applicationContext.scheduleNotification(
                        suggestion.toDomainModel(),
                        suggestion.notificationTime!!
                    )
                }
            } else {
                Log.d("AlarmRestoreWorker", "Favourites with alarms list is empty")
            }
        } catch (e: Exception){
            Log.d("AlarmRestoreWorker", e.message.toString())
        }

        Result.success()
    }

}