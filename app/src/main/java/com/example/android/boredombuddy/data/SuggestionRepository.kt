package com.example.android.boredombuddy.data

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

class SuggestionRepository(private val suggestionDao: SuggestionDao, val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

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

    suspend fun getNewSuggestion(){
        withContext(dispatcher){
            val result = BoredAPI.callApi.getSuggestion()
            if(result.isSuccessful){
                result.body()?.let {
                    suggestionDao.deleteMostRecent()
                    val newSuggestion = it.toDatabaseModel()
                    suggestionDao.insertSuggestion(newSuggestion)
                    getSuggestionImage(newSuggestion.id, newSuggestion.activity)
                }
            } else{
                _apiError.postValue(result.errorBody().toString())
            }
        }
    }

    private suspend fun getSuggestionImage(id: Long, query: String){
        withContext(dispatcher){
            val result = PexelAPI.callApi.getImage(query)
            if(result.isSuccessful){
                result.body()?.let {
                    val url = it.provideImageUrl()
                    suggestionDao.setSugestionImageUrl(id, url)
                }
            }
        }
    }
}