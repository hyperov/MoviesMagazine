package com.movies.magazine.listener

import com.movies.magazine.data.Movie

interface MovieItemClickListener {
    fun onMovieItemClick(movieItem: Movie)
}