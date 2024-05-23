package com.example.movieappdemo1.presentation.ui.screen.movielist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieListScreenViewModel @Inject constructor(

) : ViewModel() {

    val title = mutableStateOf(
        "Title ${(1..100).random()}\n1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11"
    )
}