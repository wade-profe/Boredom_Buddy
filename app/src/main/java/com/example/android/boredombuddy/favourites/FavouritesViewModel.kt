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

    val favouritesList: LiveData<List<Suggestion>?> = repository.selectedFilters.switchMap {
        if(it.isNullOrEmpty()){
            repository.favouritesList
        } else {
            repository.getFilteredList()
        }
}
    val showNoData: LiveData<Boolean> = favouritesList.switchMap {
        MutableLiveData(it.isNullOrEmpty())
    }
    val uniqueFavouritesCategories: LiveData<List<String>?> = repository.uniqueFavouritesCategories

    fun deleteSuggestion(id: Long){
        viewModelScope.launch {
            repository.deleteSuggestionFromFavourites(id)
        }
    }

    fun deleteAllFavourites(){
        viewModelScope.launch {
            repository.deleteAllFavourites()
        }
    }

    fun addFilterValue(filter: String){
        repository.addFilterValue(filter)
    }

    fun removeFilterValue(filter: String){
        repository.removeFilterValue(filter)
    }

    fun refreshSelectedFilters(){
        repository.refreshFilters()
    }
}