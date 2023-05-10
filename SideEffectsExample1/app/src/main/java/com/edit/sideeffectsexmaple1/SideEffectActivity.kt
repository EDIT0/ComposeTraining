package com.edit.sideeffectsexmaple1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import com.edit.sideeffectsexmaple1.ui.theme.SideEffectsExmaple1Theme

class SideEffectActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SideEffectScreen()
        }
    }
}

@Composable
fun SideEffectScreen() {
    var isVisible by remember {
        mutableStateOf(false)
    }

    var focusRequester = remember {
        FocusRequester()
    }

    var textInput by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                isVisible = !isVisible
            }
        ) {
            Text(text = "버튼 클릭")
        }

        if(isVisible) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxSize()
                    .focusRequester(focusRequester),
                value = textInput,
                onValueChange = {
                    textInput = it
                }
            )
        }

    }

    SideEffect {
        Log.i("MYTAG", "SideEffect")
        if(isVisible) {
            focusRequester.requestFocus()
        }
    }
}