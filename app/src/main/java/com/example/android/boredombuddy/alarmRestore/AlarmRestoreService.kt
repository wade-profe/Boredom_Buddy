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

        // TODO consider splitting this in two so that you get favouritesWithFUTUREReminders and also favourites with PAST reminders
        // then do the same thing as always for future reminders, and for past reminders reset their times to null

            val favouritesWithReminders =
                suggestionDao.getSuggestionsWithReminders()

            if (favouritesWithReminders.isNotEmpty()) {
                favouritesWithReminders.forEach { suggestion ->
                    applicationContext.scheduleNotification(
                        suggestion.toDomainModel(),
                        suggestion.notificationTime!!
                    )
                }
            }


        Result.success()
    }

}