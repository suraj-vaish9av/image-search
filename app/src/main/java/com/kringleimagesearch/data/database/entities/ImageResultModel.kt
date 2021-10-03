package com.kringleimagesearch.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kringleimagesearch.data.database.TABLE_IMAGES_RESULT
import com.squareup.moshi.Json

/**
 * This entity will keep the images result of the searched terms
 */
@Entity(tableName = TABLE_IMAGES_RESULT)
data class ImageResultModel(
    @PrimaryKey
    @Json(name = "imageId")
    val imageId:String,

    @Json(name="name")
    val name:String,

    @Json(name="contentUrl")
    val contentUrl:String,

    @Json(name="thumbnailUrl")
    val thumbnailUrl:String,

    var qry:String=""
)