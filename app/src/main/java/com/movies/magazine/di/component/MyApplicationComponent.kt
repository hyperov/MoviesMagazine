package com.movies.magazine.di.component

import android.content.Context
import com.movies.magazine.app.App
import com.movies.magazine.di.DataBaseModule
import com.movies.magazine.di.MyApplicationModule
import com.movies.magazine.di.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules =
[
    AndroidSupportInjectionModule::class,
    MyApplicationModule::class,
    ViewModelModule::class,
    DataBaseModule::class]
)
interface MyApplicationComponent : AndroidInjector<App> {

    override fun inject(instance: App?)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(context: Context): Builder

        fun build(): MyApplicationComponent
    }
}