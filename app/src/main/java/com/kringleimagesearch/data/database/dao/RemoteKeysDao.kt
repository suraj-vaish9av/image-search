package com.kringleimagesearch.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kringleimagesearch.data.database.entities.RemoteKeys

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM RemoteKeys WHERE imageId = :id")
    suspend fun remoteKeysImageId(id: String): RemoteKeys?

    @Query("DELETE FROM RemoteKeys WHERE qry=:qry")
    suspend fun clearRemoteKeys(qry: String)
}