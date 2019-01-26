package com.movies.magazine.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.movies.magazine.data.Movie

class MovieDetailsViewModel : ViewModel() {

    val movie = MutableLiveData<Movie>()
}