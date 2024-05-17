package com.example.jettriviaapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jettriviaapp.ui.theme.JetTriviaAppTheme

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
    JetTriviaAppTheme {
        content()
    }
}

@Preview
@Composable
fun TotalUI() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        TriviaApp()
    }
}

@Composable
fun TriviaApp() {

}