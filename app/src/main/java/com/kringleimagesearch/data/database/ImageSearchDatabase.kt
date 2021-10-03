package com.kringleimagesearch.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kringleimagesearch.data.database.dao.ImageResultDao
import com.kringleimagesearch.data.database.dao.RemoteKeysDao
import com.kringleimagesearch.data.database.entities.Suggestion
import com.kringleimagesearch.data.database.dao.SuggestionDao
import com.kringleimagesearch.data.database.entities.ImageResultModel
import com.kringleimagesearch.data.database.entities.RemoteKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [Suggestion::class, RemoteKeys::class, ImageResultModel::class],version = 1, exportSchema = false)
abstract class ImageSearchDatabase : RoomDatabase() {

    abstract val suggestionDao: SuggestionDao
    abstract val remoteKeyDao:RemoteKeysDao
    abstract val imageResultDao:ImageResultDao

    companion object{

        fun getSuggestionDao(context: Context) = getInstance(context).suggestionDao

        @Volatile
        private var INSTANCE: ImageSearchDatabase?= null

        fun getInstance(context: Context): ImageSearchDatabase {

            synchronized(this){

                var instance = INSTANCE
                if (instance==null){
                    instance = Room.databaseBuilder(context,ImageSearchDatabase::class.java,"image_search_database")
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance


                    GlobalScope.launch (Dispatchers.IO){

                        val listOfSuggestion = instance.suggestionDao.getAllSuggestions()

                        Log.d("getInstance","listOfSuggestion: ${listOfSuggestion.value?.size}")
                    }
                }
                return instance
            }

        }
    }
}