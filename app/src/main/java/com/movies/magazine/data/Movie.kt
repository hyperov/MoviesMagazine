package com.movies.magazine.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "movies", indices = [Index("movie_name", unique = true)])
data class Movie(@PrimaryKey(autoGenerate = true) var id: Long? = null,
                 @ColumnInfo(name = "movie_name") var movieName: String?,
                 var desc: String?,
                 var ingredients: String?,
                 var trivia: String?,
                 var image: Int?,
                 var category: String?,
                 var isFav: Boolean?,
                 var prepTime: String) : Parcelable