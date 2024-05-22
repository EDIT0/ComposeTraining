package com.example.movieappdemo1.presentation.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.movieappdemo1.common.log.LogUtil
import com.example.movieappdemo1.presentation.ui.navigation.AppNavigation
import com.example.movieappdemo1.presentation.ui.screen.intro.IntroScreen
import com.example.movieappdemo1.ui.theme.MovieAppDemo1Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppDemo1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LogUtil.d_dev("AppStart")
                    AppStart()
                }
            }
        }
    }
}

@Composable
fun AppStart() {
    AppNavigation()
}

@Preview(showBackground = true)
@Composable
fun AppStartPreview() {
    MovieAppDemo1Theme {
        AppStart()
    }
}