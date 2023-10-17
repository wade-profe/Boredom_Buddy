package com.example.android.boredombuddy.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.android.boredombuddy.MainViewModel
import com.example.android.boredombuddy.data.Suggestion
import com.example.android.boredombuddy.data.SuggestionRepository
import kotlinx.coroutines.launch

class FavouritesViewModel(private val repository: SuggestionRepository,
                          private val baseViewModel: MainViewModel)
    : ViewModel() {

    val favouritesList: LiveData<List<Suggestion>?> = repository.favouritesList
    val showNoData: LiveData<Boolean> = favouritesList.switchMap {
        MutableLiveData<Boolean>(it.isNullOrEmpty())
    }

    fun deleteSuggestion(id: Long){
        viewModelScope.launch {
            repository.deleteSuggestionFromFavourites(id)
        }
    }

    fun postToast(message: String){
        baseViewModel.message.value = message
    }
}