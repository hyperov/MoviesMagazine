package com.movies.magazine.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.movies.magazine.data.Movie
import com.movies.magazine.data.MovieDataBase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val repository: MovieDataBase) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val searchList = MutableLiveData<List<Movie>>()
    val errorHolder = MutableLiveData<Throwable>()

    val favMovieName = MutableLiveData<String>()

    fun getSearchResults(searchText: String) {
        compositeDisposable.add(repository.movieDAO().getSearchMovies("%$searchText%")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it -> searchList.value = it },
                        { t -> errorHolder.value = t }))

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