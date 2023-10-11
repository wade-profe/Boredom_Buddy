package com.example.android.boredombuddy.data

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.android.boredombuddy.data.local.DatabaseSuggestion
import com.example.android.boredombuddy.data.local.SuggestionDao
import com.example.android.boredombuddy.data.local.toDomainModel
import com.example.android.boredombuddy.data.network.BoredAPI
import com.example.android.boredombuddy.data.network.PexelAPI
import com.example.android.boredombuddy.data.network.provideImageUrl
import com.example.android.boredombuddy.data.network.toDatabaseModel
import com.example.android.boredombuddy.utils.Result
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

    val favouritesList: LiveData<List<Suggestion>?> =
        suggestionDao.getFavourites().map {
            it?.toDomainModel()
        }

    val latestSuggestion: LiveData<Suggestion?> = suggestionDao.getLatestSuggestion().map {
        it?.toDomainModel()
    }

    suspend fun getNewSuggestion(): Result<DatabaseSuggestion> {
        return try {
            withContext(dispatcher) {
                val result = BoredAPI.callApi.getSuggestion()
                if (result.isSuccessful) {
                    val newSuggestion = result.body()?.toDatabaseModel()
                    suggestionDao.deleteMostRecent()
                    suggestionDao.insertSuggestion(newSuggestion!!)
                    Result.Success(newSuggestion)
                } else {
                    Log.e(TAG, "${result.code()}: ${result.errorBody()}")
                    Result.Error(result.errorBody().toString(), result.code())
                }
            }
        } catch (e: Exception) {
            if (e is SQLiteConstraintException) {
                Log.e(TAG, "Duplicate suggestion found, trying again")
                getNewSuggestion()
            } else {
                Log.e(TAG, e.stackTraceToString())
                Result.Error(e.localizedMessage)
            }
        }
    }

    suspend fun getSuggestionImage(id: Long, query: String): Result<String> {
        return try {
            withContext(dispatcher) {
                val result = PexelAPI.callApi.getImage(query)
                if (result.isSuccessful) {
                    val url = result.body()?.provideImageUrl()
                    suggestionDao.setSugestionImageUrl(id, url!!)
                    Result.Success(url)
                } else {
                    Log.e(TAG, "${result.code()}: ${result.errorBody()}")
                    Result.Error(result.errorBody().toString(), result.code())
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.stackTraceToString())
            Result.Error(e.localizedMessage)
        }
    }

    suspend fun saveSuggestionToFavourites() {
        withContext(dispatcher) {
            suggestionDao.saveSuggestion()
        }
    }

    suspend fun deleteSuggestionFromFavourites(id: Long){
        withContext(dispatcher){
            suggestionDao.deleteSuggestion(id)
        }
    }
}