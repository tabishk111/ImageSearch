package com.example.sunbase_task.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UnsplashDao {

    @Query("SELECT * FROM photos")
    fun getAllUnsplashPhotos():PagingSource<Int, UnsplashPhoto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnsplashPhoto(photo: List<UnsplashPhoto>)

    @Query("DELETE FROM photos")
    suspend fun deleteAllUnsplashPhoto()
}