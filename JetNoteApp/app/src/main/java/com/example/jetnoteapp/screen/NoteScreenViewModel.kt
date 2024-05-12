package com.example.jetnoteapp.screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.jetnoteapp.model.Note

class NoteScreenViewModel: ViewModel() {

    val title = mutableStateOf("")

    val description = mutableStateOf("")

    val isCanUpdate = mutableStateOf<Boolean>(false)

    val currentUpdateNote = mutableStateOf<Note?>(null)
}