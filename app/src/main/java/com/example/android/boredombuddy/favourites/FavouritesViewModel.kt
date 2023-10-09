package com.example.android.boredombuddy.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.boredombuddy.data.Suggestion
import com.example.android.boredombuddy.data.SuggestionRepository
import kotlinx.coroutines.launch

class FavouritesViewModel(repository: SuggestionRepository) : ViewModel() {

    val favouritesList: LiveData<List<Suggestion>?> = repository.favouritesList

}