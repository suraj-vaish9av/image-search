package com.kringleimagesearch.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kringleimagesearch.data.database.COLUMN_QUERY
import com.kringleimagesearch.data.database.TABLE_SUGGESTION

/**
 * Suggestion will store the search term entered by user
 */
@Entity(tableName = TABLE_SUGGESTION)
class Suggestion(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_QUERY)
    val qry:String
)