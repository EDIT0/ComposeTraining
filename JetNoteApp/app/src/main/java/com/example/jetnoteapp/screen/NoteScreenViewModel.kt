package com.example.jetnoteapp.screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class NoteScreenViewModel: ViewModel() {

    val title = mutableStateOf("")

    val description = mutableStateOf("")

}