package com.edit.composelecture4

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.edit.composelecture4.ui.theme.ComposeLecture4Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DisplaySnackBar()
        }
    }
}

@Composable
fun DisplaySnackBar() {
    val scaffoldState : ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    
    Scaffold(scaffoldState = scaffoldState) {
        Button(onClick = {
            coroutineScope.launch {
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = "This is the message",
                    actionLabel = "Undo",
                    duration = SnackbarDuration.Indefinite
                )
                when(snackBarResult) {
                    SnackbarResult.ActionPerformed -> {
                        // 액션을 통해 Dismiss 할 때
                        Log.i("MYTAG", "action label clicked")
                    }
                    SnackbarResult.Dismissed -> {
                        // 자동으로 Dismiss 될 때
                        Log.i("MYTAG", "dismissed!")
                    }
                }
            }
        }) {
            Text(text = "Display SnackBar")
        }
    }
}

