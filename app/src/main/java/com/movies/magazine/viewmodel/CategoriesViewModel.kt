package com.movies.magazine.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.movies.magazine.R
import com.movies.magazine.data.Category
import com.movies.magazine.data.MovieDataBase
import com.movies.magazine.extensions.CATEGORY_ADVENTURES
import com.movies.magazine.extensions.CATEGORY_ANIMATION
import com.movies.magazine.extensions.CATEGORY_COMEDY
import com.movies.magazine.extensions.CATEGORY_SCI_FI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CategoriesViewModel @Inject constructor(private val repository: MovieDataBase) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val categoriesNames = MutableLiveData<List<String>>()

    fun getCategories() {
        compositeDisposable.add(repository.movieDAO().loadCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it -> categoriesNames.value = it })
    }

    private fun addImageToCategory(categoryNames: List<String>?): List<Category>? {
        return categoryNames?.map { categoryName ->
            when (categoryName) {
                CATEGORY_ADVENTURES -> Category(categoryName, R.drawable.movie)
                CATEGORY_COMEDY -> Category(categoryName, R.drawable.movie)
                CATEGORY_SCI_FI -> Category(categoryName, R.drawable.movie)
                CATEGORY_ANIMATION -> Category(categoryName, R.drawable.movie)
                else -> Category(categoryName, R.drawable.movie)
            }
        }
    }

    val categoriesList: LiveData<List<Category>> = Transformations.map(categoriesNames, ::addImageToCategory)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}
