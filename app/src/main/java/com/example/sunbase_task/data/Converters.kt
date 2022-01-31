package com.example.sunbase_task.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromUnsplashPhotoUrls(unsplashPhotoUrls: UnsplashPhoto.UnsplashPhotoUrls):String{
        return unsplashPhotoUrls.regular
    }

    @TypeConverter
    fun toUnsplashPhotoUrls(name:String):UnsplashPhoto.UnsplashPhotoUrls{
        return UnsplashPhoto.UnsplashPhotoUrls(name,name,name,name,name)
    }

    @TypeConverter
    fun formUnsplashUser(unsplashUser: UnsplashPhoto.UnsplashUser):String{
        return unsplashUser.name
    }

    @TypeConverter
    fun toUnsplashUser(name:String):UnsplashPhoto.UnsplashUser{
        return UnsplashPhoto.UnsplashUser(name,name)
    }
}