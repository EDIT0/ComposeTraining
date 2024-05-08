package com.example.jetnoteapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetnoteapp.data.NoteDataSource
import com.example.jetnoteapp.model.Note
import com.example.jetnoteapp.screen.NoteScreen
import com.example.jetnoteapp.screen.NoteViewModel
import com.example.jetnoteapp.ui.theme.JetNoteAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                TotalUI()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    JetNoteAppTheme {
        content()
    }
}

@Preview
@Composable
fun TotalUI() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val noteViewModel: NoteViewModel = viewModel<NoteViewModel>()
        NoteApp(noteViewModel)
    }
}

@Composable
fun NoteApp(noteViewModel: NoteViewModel = viewModel()) {
    val notesList = noteViewModel.getAllNotes()
    NoteScreen(
        notes = notesList,
        onAddNote = {
            Log.d("MYTAG", "onAddNote ${it}")
            noteViewModel.addNote(it)
        },
        onRemoveNote = {
            Log.d("MYTAG", "onRemoveNote ${it}")
            noteViewModel.removeNote(it)
        }
    )
}