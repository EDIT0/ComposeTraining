package com.my.book.library.feature.main.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.my.book.library.core.resource.R

enum class MainDestination(
    val route: String,
    val icon: ImageVector,
    @StringRes val labelResId: Int,
    @StringRes val contentDescriptionResId: Int
) {
    HOME(
        route = "home",
        icon = Icons.Default.Home,
        labelResId = R.string.home_bottom_navigation_bar_menu_home,
        contentDescriptionResId = R.string.home_bottom_navigation_bar_menu_home
    ),
    SAVE(
        route = "save",
        icon = Icons.Default.FavoriteBorder,
        labelResId = R.string.home_bottom_navigation_bar_menu_save,
        contentDescriptionResId = R.string.home_bottom_navigation_bar_menu_save
    )
}