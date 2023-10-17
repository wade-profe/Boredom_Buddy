package com.example.android.boredombuddy.newSuggestion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.boredombuddy.data.Suggestion
import com.example.android.boredombuddy.data.SuggestionRepository
import com.example.android.boredombuddy.utils.Result
import kotlinx.coroutines.launch

enum class ResultMessage{
    DEFAULT,
    NEW_SUGGESTION_FAILURE,
    GET_IMAGE_FAILURE,
    CONNECTION_FAILURE
}

class NewSuggestionViewModel(private val repository: SuggestionRepository): ViewModel() {

    val latestSuggestion: LiveData<Suggestion?> = repository.latestSuggestion
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    var isConnected: Boolean? = null
    private var _resultMessage = MutableLiveData(ResultMessage.DEFAULT)
    val resultMessage: LiveData<ResultMessage>
        get() = _resultMessage

    fun getNewSuggestion() {
        if(isConnected == true){
            _isLoading.value = true
            viewModelScope.launch {
                when(val result = repository.getNewSuggestion()){
                    is Result.Error -> {
                        Log.e(NewSuggestionViewModel::class.simpleName, result.message.toString())
                        _resultMessage.value = ResultMessage.NEW_SUGGESTION_FAILURE
                        _isLoading.postValue(false)
                    }
                    else -> {
                        val successfulResult = result as Result.Success
                        getSuggestionImage(successfulResult.data.id, successfulResult.data.activity)
                    }
                }
            }
        } else {
            _resultMessage.value = ResultMessage.CONNECTION_FAILURE
        }

    }

    private fun getSuggestionImage(id: Long, query: String){
        viewModelScope.launch {
            when(val result = repository.getSuggestionImage(id, query)){
                is Result.Error -> {
                    Log.e(NewSuggestionViewModel::class.simpleName, result.message.toString())
                    _resultMessage.value = ResultMessage.GET_IMAGE_FAILURE
                }
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