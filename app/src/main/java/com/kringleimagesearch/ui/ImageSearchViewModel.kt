package com.kringleimagesearch.ui

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kringleimagesearch.data.database.ImageSearchDatabase
import com.kringleimagesearch.data.remote.ApiClient
import com.kringleimagesearch.data.database.entities.ImageResultModel
import kotlinx.coroutines.flow.Flow

class ImageSearchViewModel(myApplication: Application) : AndroidViewModel(myApplication) {

    private val imagerSearchRepo = ApiClient.getImageSearchRepository(myApplication)

    private val suggestionDao = ImageSearchDatabase.getSuggestionDao(myApplication)

    val suggestionsLiveData = suggestionDao.getAllSuggestions()

    @ExperimentalPagingApi
    fun fetchImages(query: String): Flow<PagingData<ImageResultModel>> {
        return imagerSearchRepo.letDoggoImagesFlowDb(query = query).cachedIn(viewModelScope)
    }
}