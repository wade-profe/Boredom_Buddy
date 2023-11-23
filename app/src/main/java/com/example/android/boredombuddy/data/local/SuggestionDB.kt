package com.example.android.boredombuddy.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
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
    var mostRecent: Boolean = true,
    var notificationTime: Long? = null
)

fun DatabaseSuggestion.toDomainModel(): Suggestion{
    return Suggestion(
        id = this.id,
        activity = this.activity,
        type = this.type,
        link = this.link,
        imageUrl = this.imageUrl,
        notificationTime = this.notificationTime
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

    @Query("DELETE FROM DatabaseSuggestion WHERE id = :id")
    suspend fun deleteSuggestion(id: Long)

    @Query("SELECT * FROM DatabaseSuggestion WHERE mostRecent = 0")
    fun getFavourites(): LiveData<List<DatabaseSuggestion>?>

    @Query("SELECT * FROM DatabaseSuggestion WHERE mostRecent=0 AND type IN (:selectedTypes)")
    fun getFilteredList(selectedTypes: List<String>): LiveData<List<DatabaseSuggestion>?>

    @Query("DELETE FROM DatabaseSuggestion WHERE mostRecent = 0")
    suspend fun deleteFavourites()

    @Query("UPDATE DatabaseSuggestion SET mostRecent = 0 WHERE mostRecent = 1")
    suspend fun saveSuggestion()

    @Query("DELETE FROM DatabaseSuggestion WHERE mostRecent = 1")
    suspend fun deleteMostRecent()

    @Query("UPDATE DatabaseSuggestion SET imageUrl = :url WHERE id = :id")
    suspend fun setSuggestionImageUrl(id: Long, url: String)

    @Query("SELECT * FROM DatabaseSuggestion WHERE mostRecent = 1")
    fun getLatestSuggestion(): LiveData<DatabaseSuggestion?>

    @Query("SELECT DISTINCT(type) FROM DatabaseSuggestion WHERE mostRecent=0")
    fun getDistinctCategories(): LiveData<List<String>?>

    @Query("SELECT * FROM DatabaseSuggestion WHERE mostRecent = 0 AND notificationTime is not null AND notificationTime > (strftime('%s', 'now') * 1000)")
    suspend fun getSuggestionsWithReminders(): List<DatabaseSuggestion>

    @Query("UPDATE DatabaseSuggestion SET notificationTime = :timeInMillis WHERE id = :id")
    suspend fun setReminderTime(id: Long, timeInMillis: Long)

}

@Database(entities = [DatabaseSuggestion::class],  version = 2)
abstract class SuggestionDatabase: RoomDatabase(){
    abstract fun getSuggestionDao(): SuggestionDao
}




