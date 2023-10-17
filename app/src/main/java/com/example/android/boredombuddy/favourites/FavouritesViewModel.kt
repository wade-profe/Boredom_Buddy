package com.example.android.boredombuddy.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.android.boredombuddy.data.Suggestion
import com.example.android.boredombuddy.data.SuggestionRepository
import kotlinx.coroutines.launch

class FavouritesViewModel(private val repository: SuggestionRepository)
    : ViewModel() {

    val favouritesList: LiveData<List<Suggestion>?> = repository.favouritesList
    val showNoData: LiveData<Boolean> = favouritesList.switchMap {
        MutableLiveData(it.isNullOrEmpty())
    }

    fun deleteSuggestion(id: Long){
        viewModelScope.launch {
            repository.deleteSuggestionFromFavourites(id)
        }
    }
}