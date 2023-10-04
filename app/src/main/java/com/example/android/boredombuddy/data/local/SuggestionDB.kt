package com.example.android.boredombuddy.data.local

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android.boredombuddy.data.Suggestion

@Entity
data class DatabaseSuggestion constructor(
    @PrimaryKey
    val id: Long,
    val activity: String,
    val type: String,
    val link: String,
    var imageUrl: String?,
    var mostRecent: Boolean = true
)

fun DatabaseSuggestion.toDomainModel(): Suggestion{
    return Suggestion(
        id = this.id,
        activity = this.activity,
        type = this.type,
        link = this.link,
        imageUrl = this.imageUrl
    )
}

fun List<DatabaseSuggestion>.toDomainModel(): List<Suggestion>{
    return this.map{
        it.toDomainModel()
    }
}

@Dao
interface SuggestionDao{

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSuggestion(suggestion: DatabaseSuggestion)

    @Query("SELECT * FROM DatabaseSuggestion WHERE id = :id")
    suspend fun findSuggestionById(id: Long): DatabaseSuggestion

    @Query("DELETE FROM DatabaseSuggestion WHERE id = :id")
    suspend fun deleteSuggestion(id: Long)

    @Query("SELECT * FROM DatabaseSuggestion WHERE mostRecent = 0")
    fun getFavourites(): LiveData<List<DatabaseSuggestion>>

    @Query("UPDATE DatabaseSuggestion SET mostRecent = 0 WHERE mostRecent = 1")
    suspend fun saveSuggestion()

    @Query("DELETE FROM DatabaseSuggestion WHERE mostRecent = 1")
    suspend fun deleteMostRecent()

    @Query("UPDATE DatabaseSuggestion SET imageUrl = :url WHERE id = :id")
    suspend fun setSugestionImageUrl(id: Long, url: String)

    @Query("SELECT * FROM DatabaseSuggestion WHERE mostRecent = 1")
    fun getLatestSuggestion(): LiveData<DatabaseSuggestion>

}

@Database(entities = [DatabaseSuggestion::class],  version = 1)
abstract class SuggestionDatabase: RoomDatabase(){
    abstract val suggestionDao: SuggestionDao
}

private lateinit var INSTANCE: SuggestionDatabase

fun getDatabase(context: Context): SuggestionDatabase {
    synchronized(SuggestionDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                SuggestionDatabase::class.java,
                "suggestions").fallbackToDestructiveMigration().build()
        }
    }
    return INSTANCE
}




