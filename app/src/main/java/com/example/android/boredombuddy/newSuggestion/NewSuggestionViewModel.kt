package com.example.android.boredombuddy.newSuggestion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.boredombuddy.data.Suggestion
import com.example.android.boredombuddy.data.SuggestionRepository
import com.example.android.boredombuddy.data.local.SuggestionDatabase
import com.example.android.boredombuddy.data.local.getDatabase
import kotlinx.coroutines.launch

class NewSuggestionViewModel(application: Application): AndroidViewModel(application) {

    private val database: SuggestionDatabase = getDatabase(application)
    private val repository: SuggestionRepository = SuggestionRepository(database)
    val latestSuggestion: LiveData<Suggestion?> = repository.latestSuggestion

    init {
        if(latestSuggestion.value == null){
            viewModelScope.launch{
                repository.getNewSuggestion()
            }
        }
    }
}