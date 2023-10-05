package com.example.android.boredombuddy.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.android.boredombuddy.data.local.SuggestionDatabase
import com.example.android.boredombuddy.data.local.toDomainModel
import com.example.android.boredombuddy.data.network.BoredAPI
import com.example.android.boredombuddy.data.network.PexelAPI
import com.example.android.boredombuddy.data.network.provideImageUrl
import com.example.android.boredombuddy.data.network.toDatabaseModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SuggestionRepository(val database: SuggestionDatabase, val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

//    val favouritesList: LiveData<List<Suggestion>?> =
//        database.suggestionDao.getFavourites().map {
//        it.toDomainModel()
//    }

    val latestSuggestion: LiveData<Suggestion?> =
        database.suggestionDao.getLatestSuggestion().map {
            it?.toDomainModel()
        }

    suspend fun getNewSuggestion(){
        withContext(dispatcher){
            val result = BoredAPI.callApi.getSuggestion()
            if(result.isSuccessful){
                result.body()?.let {
                    val newSuggestion = it.toDatabaseModel()
                    database.suggestionDao.insertSuggestion(newSuggestion)
                    getSuggestionImage(newSuggestion.id, newSuggestion.activity)
                }
            }
        }
    }

    private suspend fun getSuggestionImage(id: Long, query: String){
        withContext(dispatcher){
            val result = PexelAPI.callApi.getImage(query)
            if(result.isSuccessful){
                result.body()?.let {
                    val url = it.provideImageUrl()
                    database.suggestionDao.setSugestionImageUrl(id, url)
                }
            }
        }
    }
}