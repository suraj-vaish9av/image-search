package com.kringleimagesearch.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kringleimagesearch.data.database.TABLE_REMOTE_KEYS

@Entity(tableName = TABLE_REMOTE_KEYS)
data class RemoteKeys(@PrimaryKey val imageId: String, val prevKey: Int?, val nextKey: Int?, val qry:String)
