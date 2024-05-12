package com.example.jetnoteapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PreviewNoteInputText() {

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun NoteInputText(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    maxLine: Int = 1,
    onTextChange: (String) -> Unit,
    onImeAction: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = text,
        onValueChange = onTextChange,
        colors = TextFieldDefaults.textFieldColors(
            Color.DarkGray
        ),
        maxLines = maxLine,
        label = {
            Text(text = label)
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
                keyboardController?.hide()
            }
        ),
        modifier = modifier
    )
}

@Composable
fun NoteButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enable: Boolean = true
) {
    if(enable) {
        Button(
            onClick = {
                onClick.invoke()
            },
            shape = CircleShape,
            enabled = enable,
            modifier = modifier
        ) {
            Text(
                text = text
            )
        }
    }
}

@Composable
fun NoteUpdateButton(
    modifier: Modifier = Modifier,
    onUpdateClick: () -> Unit,
    onCancelClick: () -> Unit,
    enable: Boolean = true
) {
    Row {
        if(enable) {
            Button(
                onClick = {
                    onUpdateClick.invoke()
                },
                enabled = enable,
                modifier = modifier
            ) {
                Text(text = "Update")
            }
            Button(
                onClick = {
                    onCancelClick.invoke()
                },
                enabled = enable,
                modifier = modifier
            ) {
                Text(text = "Cancel")
            }
        }
    }
}