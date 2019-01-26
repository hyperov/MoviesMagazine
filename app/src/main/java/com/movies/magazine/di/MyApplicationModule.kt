package com.movies.magazine.di

import com.movies.magazine.view.CategoriesFragment
import com.movies.magazine.view.MainActivity
import com.movies.magazine.view.MoviesFragment
import com.movies.magazine.view.SearchableActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MyApplicationModule {

    @ContributesAndroidInjector
    internal abstract fun contributeMainActivityInjector(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun contributeCategoriesFragmentInjector(): CategoriesFragment

    @ContributesAndroidInjector
    internal abstract fun contributeMoviesFragmentInjector(): MoviesFragment

    @ContributesAndroidInjector
    internal abstract fun contributeSearchActivityInjector(): SearchableActivity
}