package com.example.sunbase_task.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import androidx.room.withTransaction
import com.example.sunbase_task.api.UnsplashApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalPagingApi
@Singleton
class UnsplashRepository @Inject constructor(private val unsplashApi: UnsplashApi, private val db:UnsplashDatabase) {

    //private val unsplashDao = db.unsplashDao()
    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UnsplashPagingSource(unsplashApi, query) }
        ).liveData

    fun getResult(query: String): Flow<PagingData<UnsplashPhoto>> {
        val pagingSourceFactory = { db.unsplashDao().getAllUnsplashPhotos() }
        return Pager(
            config = PagingConfig(pageSize = 10,enablePlaceholders = false),
            remoteMediator = RemoteMediator(db,unsplashApi),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }


}