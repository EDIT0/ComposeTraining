package com.my.presentation.component

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SearchBarComponent(
    searchText: String,
    changeSearchText: (String) -> Unit,
    searchIconClick: (String) -> Unit,
    fakeButton: Boolean,
    fakeButtonClick: () -> Unit,
    startKeyboardUp: Boolean
) {

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    TextField(
        value = searchText,
        onValueChange = {
            changeSearchText.invoke(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                fakeButtonClick.invoke()
            }
            .focusRequester(focusRequester),
        enabled = !fakeButton,
        shape = RoundedCornerShape(20.dp),
        singleLine = true,
        placeholder = {
            Text(text = "Search Movie", color = Color.Black)
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "search movie",
                modifier = Modifier
                    .clickable {
                        searchIconClick.invoke(searchText)
                    },
                tint = Color.Black
            )
        },
        colors = TextFieldDefaults.colors(
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                Log.d("MYTAG", "Keyboard onDone")
                focusManager.clearFocus()
            },
            onNext = {
                Log.d("MYTAG", "Keyboard onNext")
                focusManager.moveFocus(FocusDirection.Down)
            }
        )
    )

    if(startKeyboardUp) {
        rememberCoroutineScope().launch {
            delay(200L)
            focusRequester.requestFocus()
        }
    } else {

    }

}

@Preview
@Composable
fun PreviewSearchBarComponent() {
    SearchBarComponent(searchText = "Text", {}, {}, false, {}, true)
}