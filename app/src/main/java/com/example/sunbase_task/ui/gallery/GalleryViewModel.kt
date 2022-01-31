package com.example.sunbase_task.ui.gallery

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.sunbase_task.data.UnsplashPhoto
import com.example.sunbase_task.data.UnsplashRepository
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class GalleryViewModel @ViewModelInject constructor(
    private val repository: UnsplashRepository,
    @Assisted state: SavedStateHandle
) : ViewModel() {

    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    val photos = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }

    val newsResult: Flow<PagingData<UnsplashPhoto>> = repository.getResult("random").cachedIn(viewModelScope)

    fun getModelNews():Flow<PagingData<UnsplashPhoto>>{
        return newsResult
    }

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = "random"
    }
}