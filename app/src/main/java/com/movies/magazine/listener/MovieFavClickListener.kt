package com.movies.magazine.listener

import com.movies.magazine.data.Movie

interface MovieFavClickListener {
    fun onMovieFav(isFav: Boolean, item: Movie)
}