package com.my.presentation.screen.moviedetail

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.my.common.UrlConvertUtil
import com.my.domain.model.MovieModelResult
import com.my.presentation.screen.moviedetail.model.MovieDetailInfoUiState
import com.my.presentation.screen.moviedetail.viewmodel.MovieDetailViewModel

@Composable
fun MovieDetailScreen(
    navController: NavController,
    movieDetailViewModel: MovieDetailViewModel = hiltViewModel(),
    movieModelResultString: String
) {
    LaunchedEffect(key1 = true) {
        movieDetailViewModel.updateMovieModelResult(stringToMovieModelResult(movieModelResultString))
    }

    MovieDetailScreenUI(
        movieDetailInfoUiState = movieDetailViewModel.movieDetailInfoUiState.collectAsState().value
    )

}

@Composable
fun MovieDetailScreenUI(
    movieDetailInfoUiState: MovieDetailInfoUiState
) {
    Log.d("MYTAG", "${movieDetailInfoUiState.movieModelResult}")
}

@Preview(showBackground = true)
@Composable
fun PreviewMovieDetailScreen() {
    MovieDetailScreenUI(
        movieDetailInfoUiState = MovieDetailInfoUiState()
    )
}

fun stringToMovieModelResult(movieModelResultString: String): MovieModelResult {
    val tmp = UrlConvertUtil.urlDecode(movieModelResultString)
    Log.i("MYTAG", "movieModelResultJson: ${tmp}")
    val gson = Gson()
    val movieModelResult: MovieModelResult = gson.fromJson(movieModelResultString, MovieModelResult::class.java)
    Log.i("MYTAG","movieModelResult: ${movieModelResult}")
    return movieModelResult
}