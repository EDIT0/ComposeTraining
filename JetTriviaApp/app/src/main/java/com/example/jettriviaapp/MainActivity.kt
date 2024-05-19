package com.example.jettriviaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jettriviaapp.screens.QuestionsViewModel
import com.example.jettriviaapp.screens.TriviaHome
import com.example.jettriviaapp.ui.theme.JetTriviaAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
    val questionsViewModel = viewModel<QuestionsViewModel>()
    TriviaHome(questionsViewModel)
}