package com.movies.magazine.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao()
interface MovieDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAllMovies(vararg movies: Movie): List<Long>

    @Delete
    fun deleteMovies(vararg movies: Movie)

    @Query("SELECT * FROM movies")
    fun loadAllMovies(): LiveData<List<Movie>>

    @Query("UPDATE movies SET isFav = :isFavourite WHERE id = :movieId")
    fun updateMovies(movieId: Long, isFavourite: Boolean): Long

    @Query("SELECT * FROM movies WHERE category = :categoryName ")
    fun loadCategoryMovies(categoryName: String): List<Movie>

    @Query("SELECT DISTINCT category FROM movies ")
    fun loadCategories(): Flowable<List<String>>

    @Query("SELECT * FROM movies WHERE isFav = 1 ")
    fun loadFavMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movies WHERE movie_name LIKE :text")
    fun getSearchMovies(text: String): Flowable<List<Movie>>
}