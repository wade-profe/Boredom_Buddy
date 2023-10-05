package com.example.android.boredombuddy

import android.app.Application
import androidx.room.Room
import com.example.android.boredombuddy.data.SuggestionRepository
import com.example.android.boredombuddy.data.local.SuggestionDao
import com.example.android.boredombuddy.data.local.SuggestionDatabase
import com.example.android.boredombuddy.newSuggestion.NewSuggestionViewModel
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val module = module {

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

            single { SuggestionRepository(get() as SuggestionDao) }

        }

        startKoin {
            androidContext(this@MainApplication)
            modules(listOf(module))
        }
    }
}