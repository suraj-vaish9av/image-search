package com.kringleimagesearch.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.kringleimagesearch.data.database.ImageSearchDatabase
import com.kringleimagesearch.data.database.entities.ImageResultModel
import com.kringleimagesearch.data.database.entities.RemoteKeys
import com.kringleimagesearch.data.database.entities.Suggestion
import com.kringleimagesearch.data.remote.services.ImageSearchService
import retrofit2.HttpException
import java.io.IOException


const val DEFAULT_PAGE_INDEX = 1

@ExperimentalPagingApi
class ImageSearchMediator(val query:String, val imageSearchService: ImageSearchService, private val appDatabase: ImageSearchDatabase)
    : RemoteMediator<Int, ImageResultModel>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageResultModel>
    ): MediatorResult {

        if (query.isNotEmpty()) {
            appDatabase.suggestionDao.insertSuggestion(Suggestion(query))
            Log.d("Suggestion","suggestion inserted")
        }

        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try {
            val offset = (page - 1) * state.config.pageSize
            val response = imageSearchService.searchImagesAsync(query, offset, state.config.pageSize)
            var list= response.body()?.data
            list = list?.map { it.qry=query.trim()
                it
            }
            list?.forEach {
                Log.d("list",it.qry+":"+it.name)
            }

            val isEndOfList = list?.isEmpty()?:true
            appDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    appDatabase.imageResultDao.deleteImageResult(query)
                    //appDatabase.remoteKeyDao.clearRemoteKeys(query)
                }
                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = list?.map {
                    RemoteKeys(imageId = it.imageId, prevKey = prevKey, nextKey = nextKey,qry=query)
                }
                keys?.let { appDatabase.remoteKeyDao.insertAll(keys) }
                list?.let { appDatabase.imageResultDao.insertAll(it) }
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, ImageResultModel>): Any {

        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                    //?: throw InvalidObjectException("Remote key should not be null for $loadType")
                remoteKeys?.nextKey ?: DEFAULT_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                    //?: throw InvalidObjectException("Invalid state, key should not be null")
                //end of list condition reached
                remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }
    }


    private suspend fun getFirstRemoteKey(state: PagingState<Int, ImageResultModel>): RemoteKeys? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { imageResult ->
                appDatabase.remoteKeyDao.remoteKeysImageId(imageResult.imageId) }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, ImageResultModel>): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { imageResult -> appDatabase.remoteKeyDao.remoteKeysImageId(imageResult.imageId) }
    }

    private suspend fun getClosestRemoteKey(state: PagingState<Int, ImageResultModel>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.imageId?.let { imageId ->
                appDatabase.remoteKeyDao.remoteKeysImageId(imageId)
            }
        }
    }
}