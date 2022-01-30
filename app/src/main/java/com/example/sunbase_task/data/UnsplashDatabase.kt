package com.example.sunbase_task.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [UnsplashPhoto::class, RemoteKey::class], version = 1)
@TypeConverters(Converters::class)
abstract class UnsplashDatabase : RoomDatabase(){

    abstract fun unsplashDao():UnsplashDao
    abstract fun remoteDao():RemoteDao
}