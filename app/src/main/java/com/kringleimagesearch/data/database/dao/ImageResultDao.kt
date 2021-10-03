package com.kringleimagesearch.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kringleimagesearch.data.database.entities.ImageResultModel

@Dao
interface ImageResultDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(list: List<ImageResultModel>)

    @Query("SELECT * FROM ImageResult WHERE qry=:query")
    fun getImageResult(query:String): PagingSource<Int, ImageResultModel>

    @Query("DELETE FROM ImageResult WHERE qry=:query OR name LIKE :query")
    suspend fun deleteImageResult(query: String)
}