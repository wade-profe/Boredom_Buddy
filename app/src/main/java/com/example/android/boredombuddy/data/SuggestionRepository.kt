package com.example.android.boredombuddy.data

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.android.boredombuddy.data.local.SuggestionDao
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

    private val _message: MutableLiveData<String> = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message
    private val _isBusy: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isBusy: LiveData<Boolean>
        get() = _isBusy

    suspend fun getNewSuggestion() {
        _isBusy.postValue(true)
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
                    _message.postValue(result.errorBody().toString())
                }
            } catch (e: Exception) {
                if(e is SQLiteConstraintException){
                    Log.d("WADE", "Duplicate found")
                    getNewSuggestion()
                } else {
                    Log.e(TAG, e.stackTraceToString())
                    _message.postValue("Error retrieving suggestion")
                }
            } finally {
                    _isBusy.postValue(false)
                }
            }
        }

    private suspend fun getSuggestionImage(id: Long, query: String) {
        withContext(dispatcher) {
            try {
                val result = PexelAPI.callApi.getImage(query)
                if (result.isSuccessful) {
                    result.body()?.let {
                        val url = it.provideImageUrl()
                        suggestionDao.setSugestionImageUrl(id, url)
                    }
                } else {
                    _message.postValue(result.body().toString())
                }
            } catch (e: Exception) {
                Log.e(TAG, e.stackTraceToString())
                _message.postValue("Error loading image")
            }

        }
    }

    suspend fun saveSuggestionToFavourites(){
        withContext(dispatcher){
            suggestionDao.saveSuggestion()
            _message.postValue("Suggestion saved!")
            getNewSuggestion()
        }
    }
}