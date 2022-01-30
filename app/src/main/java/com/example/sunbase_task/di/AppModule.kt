package com.example.sunbase_task.di

import android.app.Application
import androidx.room.Room
import com.example.sunbase_task.api.UnsplashApi
import com.example.sunbase_task.data.UnsplashDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(UnsplashApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideUnsplashApi(retrofit: Retrofit): UnsplashApi =
        retrofit.create(UnsplashApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app:Application) : UnsplashDatabase =
        Room.databaseBuilder(app,UnsplashDatabase::class.java,"unsplash_database")
            .build()

}