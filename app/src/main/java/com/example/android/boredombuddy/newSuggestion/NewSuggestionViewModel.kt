package com.example.android.boredombuddy.newSuggestion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.boredombuddy.MainViewModel
import com.example.android.boredombuddy.data.Suggestion
import com.example.android.boredombuddy.data.SuggestionRepository
import com.example.android.boredombuddy.utils.Result
import kotlinx.coroutines.launch

class NewSuggestionViewModel(private val repository: SuggestionRepository,
                             private val baseViewModel: MainViewModel
): ViewModel() {

    val latestSuggestion: LiveData<Suggestion?> = repository.latestSuggestion
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun getNewSuggestion() {
        _isLoading.value = true
        viewModelScope.launch {
            when(val result = repository.getNewSuggestion()){
                is Result.Error -> {
                    baseViewModel.message.postValue("Error retrieving suggestion")
                    _isLoading.postValue(false)
                }
                else -> {
                    val successfulResult = result as Result.Success
                    getSuggestionImage(successfulResult.data.id, successfulResult.data.activity)
                }
            }
        }
    }

    private fun getSuggestionImage(id: Long, query: String){
        viewModelScope.launch {
            when(repository.getSuggestionImage(id, query)){
                is Result.Error -> baseViewModel.message.postValue("Error retrieving image")
                else -> {}
            }
            _isLoading.postValue(false)
        }
    }

    fun saveSuggestionToFavourites(){
        viewModelScope.launch {
            repository.saveSuggestionToFavourites()
            getNewSuggestion()
        }
    }
}