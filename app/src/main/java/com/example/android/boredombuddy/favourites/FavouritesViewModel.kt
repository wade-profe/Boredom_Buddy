package com.example.android.boredombuddy.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.android.boredombuddy.data.Suggestion
import com.example.android.boredombuddy.data.SuggestionRepository

class FavouritesViewModel(repository: SuggestionRepository) : ViewModel() {

    val favouritesList: LiveData<List<Suggestion>?> = repository.favouritesList

}