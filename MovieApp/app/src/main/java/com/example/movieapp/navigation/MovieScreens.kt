package com.example.movieapp.navigation

enum class MovieScreens {
    HomeScreen,
    DetailsScreen;

    companion object {
        fun fromRoute(route: String?): MovieScreens {
            when(route?.substringBefore("/")) {
                HomeScreen.name -> {
                    return HomeScreen
                }
                DetailsScreen.name -> {
                    return DetailsScreen
                }
                null -> {
                    return HomeScreen
                }
                else -> {
                    throw IllegalArgumentException("Route ${route} is not recognized")
                }
            }
        }
    }
}