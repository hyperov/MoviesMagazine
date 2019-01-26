package com.movies.magazine.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.movies.magazine.data.Movie
import com.movies.magazine.data.MovieDataBase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MoviesViewModel @Inject constructor(private val repository: MovieDataBase) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val favMovieName = MutableLiveData<String>()
    val allMovies = MutableLiveData<List<Movie>>()
    val favCount = MutableLiveData<Int>()

    fun getMovies(categoryName: String) {
        compositeDisposable.add(Observable.fromCallable { repository.movieDAO().loadCategoryMovies(categoryName) }
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it -> allMovies.value = it })
    }

    fun getFavMovies(): LiveData<List<Movie>> {
        return repository.movieDAO().loadFavMovies()
    }

    fun updateMovie(movie: Movie, isFav: Boolean) {
        compositeDisposable.add(Observable.fromCallable { repository.movieDAO().updateMovies(movie.id!!, isFav) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    favMovieName.value = movie.movieName
                })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}