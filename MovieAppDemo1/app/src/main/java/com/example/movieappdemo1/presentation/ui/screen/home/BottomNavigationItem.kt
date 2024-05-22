package com.example.movieappdemo1.presentation.ui.screen.home

import com.example.movieappdemo1.R
import com.example.movieappdemo1.common.constant.MOVIE_LIST
import com.example.movieappdemo1.common.constant.SAVED_MOVIE
import com.example.movieappdemo1.common.constant.SEARCH_MOVIE

sealed class BottomNavItem(
    val title: Int, val icon: Int, val screenRoute: String
) {
    object MovieList : BottomNavItem(R.string.bottom_nav_item_movie_list, R.drawable.ic_movie_24, MOVIE_LIST)
    object SearchMovie : BottomNavItem(R.string.bottom_nav_item_search_movie, R.drawable.ic_search_24, SEARCH_MOVIE)
    object SavedMovie : BottomNavItem(R.string.bottom_nav_item_saved_movie, R.drawable.ic_save_24, SAVED_MOVIE)
}