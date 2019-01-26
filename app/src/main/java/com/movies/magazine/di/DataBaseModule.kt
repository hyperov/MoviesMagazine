package com.movies.magazine.di

import android.arch.persistence.room.Room
import android.content.Context
import com.movies.magazine.data.MovieDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule {

    @Singleton
    @Provides
    fun getDatabase(context: Context): MovieDataBase {
        return Room.databaseBuilder(
                context,
                MovieDataBase::class.java, "database-movies"
        ).fallbackToDestructiveMigration().build()
    }
}