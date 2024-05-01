package com.example.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.movieapp.navigation.MovieNavigation
import com.example.movieapp.ui.theme.MovieAppTheme

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(content: @Composable () -> Unit) {
    MovieAppTheme {

//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = {
//                        Text("Movies")
//                    },
//                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Magenta)
//                )
//            }
//        ) {
//            Surface(
//                color = MaterialTheme.colorScheme.background,
//                modifier = Modifier.padding(it)
//            ) {
                content()
//            }
//        }
    }
}

@Preview
@Composable
fun TotalUI() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            MovieNavigation()
        }
    }
}