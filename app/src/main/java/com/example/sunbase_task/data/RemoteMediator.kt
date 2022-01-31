package com.example.sunbase_task.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.sunbase_task.api.UnsplashApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class RemoteMediator(private val database: UnsplashDatabase,private val networkService:UnsplashApi): RemoteMediator<Int,UnsplashPhoto>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UnsplashPhoto>
    ): MediatorResult {

        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }



        return try {
            val response = networkService.searchPhotos("random",page,100)
            val isEndOfList = response.results.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.unsplashDao().deleteAllUnsplashPhoto()
                    database.remoteDao().deleteByQuery()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.results.map {
                    RemoteKey(it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteDao().insertOrReplace(keys)

                database.unsplashDao().insertUnsplashPhoto(response.results)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, UnsplashPhoto>): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                prevKey

            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, UnsplashPhoto>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                database.remoteDao().remoteKeyByQuery(repoId)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, UnsplashPhoto>): RemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { cat -> database.remoteDao().remoteKeyByQuery(cat.id) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, UnsplashPhoto>): RemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { cat -> database.remoteDao().remoteKeyByQuery(cat.id) }
    }
}