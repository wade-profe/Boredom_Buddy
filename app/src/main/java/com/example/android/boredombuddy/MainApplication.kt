package com.example.android.boredombuddy

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.android.boredombuddy.data.SuggestionRepository
import com.example.android.boredombuddy.data.local.SuggestionDao
import com.example.android.boredombuddy.data.local.SuggestionDatabase
import com.example.android.boredombuddy.newSuggestion.NewSuggestionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin

private const val DATA_STORE_NAME = "Preferences"

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val module = module {

            single {
                    PreferenceDataStoreFactory.create(
                        corruptionHandler = ReplaceFileCorruptionHandler(
                            produceNewData = { emptyPreferences() }
                        ),
                        migrations = listOf(SharedPreferencesMigration(applicationContext, DATA_STORE_NAME)),
                        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
                        produceFile = { applicationContext.preferencesDataStoreFile(DATA_STORE_NAME) }
                    )
                }

            single {
                Room.databaseBuilder(
                    applicationContext,
                    SuggestionDatabase::class.java,
                    "suggestions_database"
                ).build()
            }

            single<SuggestionDao> {
                val database = get<SuggestionDatabase>()
                database.getSuggestionDao()
            }

            viewModel {
                NewSuggestionViewModel(
                    get() as SuggestionRepository
                )
            }

            viewModel {
                MainViewModel(
                    get() as SuggestionRepository
                )
            }

            single { SuggestionRepository(get() as SuggestionDao) }

        }

        startKoin {
            androidContext(this@MainApplication)
            modules(listOf(module))
        }
    }
}