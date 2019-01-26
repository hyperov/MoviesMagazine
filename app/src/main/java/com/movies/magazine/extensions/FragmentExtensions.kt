package com.movies.magazine.extensions

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

const val CATEGORY_ADVENTURES = "Adventures"
const val CATEGORY_COMEDY = "Comedy"
const val CATEGORY_SCI_FI = "Sci-Fi"
const val CATEGORY_ANIMATION = "Animation"

fun AppCompatActivity.replaceFragment(fragment: Fragment, @IdRes layoutRes: Int, isBackStack: Boolean = false): Fragment {
    val addToBackStack = supportFragmentManager?.beginTransaction()?.replace(layoutRes, fragment)
    if (isBackStack) addToBackStack?.addToBackStack("")
    addToBackStack?.commit()
    return fragment
}

