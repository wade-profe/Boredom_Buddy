package com.example.android.boredombuddy.newSuggestion

import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.boredombuddy.data.Suggestion
import com.example.android.boredombuddy.data.SuggestionRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch



class NewSuggestionViewModel(private val repository: SuggestionRepository
): ViewModel() {

    val latestSuggestion: LiveData<Suggestion?> = repository.latestSuggestion
    val isLoading: LiveData<Boolean> = repository.isBusy

    fun getNewSuggestion() {
        viewModelScope.launch {
            repository.getNewSuggestion()
        }
    }

    fun saveSuggestionToFavourites(){
        viewModelScope.launch {
            repository.saveSuggestionToFavourites()
        }
    }
}