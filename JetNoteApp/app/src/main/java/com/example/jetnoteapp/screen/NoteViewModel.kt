package com.example.jetnoteapp.screen

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetnoteapp.data.NoteDataSource
import com.example.jetnoteapp.model.Note
import com.example.jetnoteapp.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

//    private var noteList = mutableStateListOf<Note>()
    private val _noteList = MutableStateFlow<List<Note>>(emptyList())
    val noteList = _noteList.asStateFlow()

    init {
//        noteList.addAll(NoteDataSource().loadNotes())
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getAllNotes()
                .distinctUntilChanged()
                .collect {
                    if(it.isNullOrEmpty()) {
                        Log.d("MYTAG", "Empty List")
                        _noteList.value = it
                    } else {
                        _noteList.value = it
                    }
                }
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            noteRepository.addNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            noteRepository.updateNote(note)
        }
    }

    fun removeNote(note: Note) {
        viewModelScope.launch {
            noteRepository.deleteNote(note)
        }
    }
}