package com.example.jetnoteapp.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetnoteapp.R
import com.example.jetnoteapp.components.NoteButton
import com.example.jetnoteapp.components.NoteInputText
import com.example.jetnoteapp.data.NoteDataSource
import com.example.jetnoteapp.model.Note
import java.time.format.DateTimeFormatter


@Preview(showBackground = true)
@Composable
fun PreviewNoteScreen() {
    NoteScreen(NoteDataSource().loadNotes(), {}, {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    notes: List<Note>,
    onAddNote: (Note) -> Unit,
    onRemoveNote: (Note) -> Unit
) {

    val context = LocalContext.current

    val noteScreenViewModel: NoteScreenViewModel = viewModel<NoteScreenViewModel>()

//    val title = remember {
//        mutableStateOf("")
//    }

//    val description = remember {
//        mutableStateOf("")
//    }

    Column(
        modifier = Modifier
            .padding(5.dp)
    ) {
        TopAppBar(
            title = {
                Text(text = stringResource(id = R.string.app_name))
            },
            actions = {
                Icon(
                    imageVector = Icons.Rounded.Notifications,
                    contentDescription = "Icon"
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(Color(0xFF1D7BC7))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NoteInputText(
                modifier = Modifier
                    .padding(
                        top = 9.dp,
                        bottom = 8.dp
                    ),
                text = noteScreenViewModel.title.value,
                label = "Title",
                onTextChange = {
                    if (it.all {
                            it.isLetter() || it.isWhitespace()
                        }) {
                        noteScreenViewModel.title.value = it
                    }
                    Log.d("MYTAG", "${noteScreenViewModel.title.value}")
                }
            )

            NoteInputText(
                modifier = Modifier
                    .padding(
                        top = 9.dp,
                        bottom = 8.dp
                    ),
                text = noteScreenViewModel.description.value,
                label = "Add a note",
                onTextChange = {
                    if (it.all {
                            it.isLetter() || it.isWhitespace()
                        }) {
                        noteScreenViewModel.description.value = it
                    }
                    Log.d("MYTAG", "${noteScreenViewModel.description.value}")
                }
            )

            NoteButton(
                text = "Save",
                onClick = {
                    Log.d("MYTAG", "NoteButton Clicked")
                    if (noteScreenViewModel.title.value.isNotEmpty() && noteScreenViewModel.description.value.isNotEmpty()) {
                        onAddNote(Note(title = noteScreenViewModel.title.value, description = noteScreenViewModel.description.value))
                        Toast.makeText(context, "Note Added", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        Divider(
            modifier = Modifier
                .padding(10.dp)
        )

        LazyColumn {
            itemsIndexed(notes) { index, note ->
                NoteRow(
                    modifier = Modifier,
                    note = note,
                    onNoteClicked = {
                        Log.d("MYTAG", "LazyColumn Clicked ${it}")
                        onRemoveNote(note)
                    }
                )
            }
        }
    }
}

@Composable
fun NoteRow(
    modifier: Modifier = Modifier,
    note: Note,
    onNoteClicked: (Note) -> Unit
) {
    Surface(
        modifier = modifier
            .padding(4.dp)
            .clip(
                RoundedCornerShape(topEnd = 33.dp, bottomStart = 33.dp)
            )
            .fillMaxWidth()
            .shadow(6.dp),
        color = Color(0xFFDFE6EB),
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 14.dp, vertical = 6.dp)
                .clickable {
                    onNoteClicked(note)
                },
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = note.description,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = note.entryDate.format(DateTimeFormatter.ofPattern("EEE, d MMM")),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}