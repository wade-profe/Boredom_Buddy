package com.example.android.boredombuddy.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.example.android.boredombuddy.data.local.SuggestionDao
import com.example.android.boredombuddy.data.local.SuggestionDatabase
import com.example.android.boredombuddy.data.local.toDomainModel
import com.example.android.boredombuddy.data.network.BoredAPI
import com.example.android.boredombuddy.data.network.PexelAPI
import com.example.android.boredombuddy.data.network.provideImageUrl
import com.example.android.boredombuddy.data.network.toDatabaseModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SuggestionRepository(
    private val suggestionDao: SuggestionDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    companion object {
        val TAG = SuggestionRepository::class.simpleName
    }

//    val favouritesList: LiveData<List<Suggestion>?> =
//        database.suggestionDao.getFavourites().map {
//        it.toDomainModel()
//    }

    val latestSuggestion: LiveData<Suggestion?> = suggestionDao.getLatestSuggestion().map {
        it?.toDomainModel()
    }

    private val _apiError: MutableLiveData<String> = MutableLiveData<String>()
    val apiError: LiveData<String>
        get() = _apiError
    private val _isBusy: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isBusy: LiveData<Boolean>
        get() = _isBusy

    suspend fun getNewSuggestion() {
        _isBusy.value = true
        withContext(dispatcher) {
            try {
                val result = BoredAPI.callApi.getSuggestion()
                if (result.isSuccessful) {
                    result.body()?.let {
                        suggestionDao.deleteMostRecent()
                        val newSuggestion = it.toDatabaseModel()
                        suggestionDao.insertSuggestion(newSuggestion)
                        // _isBusy value set to false early to prevent excessive load spinner visibility for longer image operation
                        _isBusy.postValue(false)
                        getSuggestionImage(newSuggestion.id, newSuggestion.activity)
                    }
                } else {
                    _apiError.postValue(result.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e(TAG, e.stackTraceToString())
                _apiError.postValue("Error retrieving suggestion")
            } finally {
                _isBusy.postValue(false)
            }
        }
    }

    suspend fun getSuggestionImage(id: Long, query: String) {
        withContext(dispatcher) {
            try {
                val result = PexelAPI.callApi.getImage(query)
                if (result.isSuccessful) {
                    result.body()?.let {
                        val url = it.provideImageUrl()
                        suggestionDao.setSugestionImageUrl(id, url)
                    }
                } else {
                    _apiError.postValue(result.body().toString())
                }
            } catch (e: Exception) {
                Log.e(TAG, e.stackTraceToString())
                _apiError.postValue("Error loading image")
            }

        }
    }
}