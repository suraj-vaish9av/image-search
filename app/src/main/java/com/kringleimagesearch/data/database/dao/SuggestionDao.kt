package com.kringleimagesearch.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kringleimagesearch.data.database.entities.Suggestion

@Dao
interface SuggestionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSuggestion(suggestion: Suggestion)

    @Query("SELECT * FROM Suggestion")
    fun getAllSuggestions():LiveData<List<Suggestion>>
}