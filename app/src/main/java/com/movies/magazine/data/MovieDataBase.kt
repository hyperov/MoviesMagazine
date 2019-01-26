package com.movies.magazine.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [Movie::class], version = 3, exportSchema = false)
abstract class MovieDataBase : RoomDatabase() {
    abstract fun movieDAO(): MovieDAO
}