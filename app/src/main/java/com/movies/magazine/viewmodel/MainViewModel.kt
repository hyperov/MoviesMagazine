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

class MainViewModel @Inject constructor(private val repository: MovieDataBase) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val isInserted = MutableLiveData<Long>()
    val clickedCategory = MutableLiveData<String>()
    val clickedMovie = MutableLiveData<Movie>()


    fun insertMovies(vararg movies: Movie) {

        compositeDisposable.add(Observable.fromCallable { repository.movieDAO().insertAllMovies(*movies) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it -> isInserted.value = it.size.toLong() }
                        , { isInserted.value = 0 }))
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}