package com.kringleimagesearch.data.remote.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kringleimagesearch.data.database.ImageSearchDatabase
import com.kringleimagesearch.data.database.entities.ImageResultModel
import com.kringleimagesearch.data.remote.services.ImageSearchService
import com.kringleimagesearch.paging.ImageSearchMediator
import kotlinx.coroutines.flow.Flow

class ImageSearchRepository(private val imageSearchService: ImageSearchService, private val appDatabase:ImageSearchDatabase) : BaseRepository() {

    @ExperimentalPagingApi
    fun letDoggoImagesFlowDb(pagingConfig: PagingConfig = getDefaultPageConfig(), query: String): Flow<PagingData<ImageResultModel>> {

        val pagingSourceFactory = { appDatabase.imageResultDao.getImageResult(query) }
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = ImageSearchMediator(query,imageSearchService, appDatabase)
        ).flow
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = 50, prefetchDistance = 2, enablePlaceholders = false)
    }

}