package com.example.movieappdemo1.presentation.ui.screen.savedmovie

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.movieappdemo1.common.log.LogUtil
import com.example.movieappdemo1.domain.model.MovieModelResult
import com.example.movieappdemo1.domain.usecase.DeleteSavedMovieUseCase
import com.example.movieappdemo1.domain.usecase.GetSavedMoviesUseCase
import com.example.movieappdemo1.domain.usecase.GetSearchSavedMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedMovieScreenViewModel @Inject constructor(
    private val getSavedMoviesUseCase: GetSavedMoviesUseCase,
    private val deleteSavedMovieUseCase: DeleteSavedMovieUseCase,
    private val getSearchSavedMoviesUseCase: GetSearchSavedMoviesUseCase
) : ViewModel() {

    val savedMovieSearchText = mutableStateOf("")

//    val allSavedMoviesList = getSavedMoviesUseCase.execute()
    val currentPosition = mutableStateOf(-1)
    var isDelete = mutableStateOf<Boolean>(false)

    var keyword = MutableLiveData<String>("")
    @OptIn(ExperimentalCoroutinesApi::class)
    val getSearchSavedMoviesStateFlow: Flow<List<MovieModelResult>> = keyword.asFlow().flatMapLatest { filter ->
        getSearchSavedMoviesUseCase.execute_using_stateflow(filter)
            .onCompletion {
                LogUtil.i_dev("MYTAG isDelete: ${isDelete.value}")
                if(isDelete.value) {
                    isDelete.value = false
                }
            }
    }

    fun deleteSavedMovie(movieModelResult: MovieModelResult) {
        LogUtil.i_dev("Delete ${movieModelResult.title}")
        viewModelScope.launch(Dispatchers.IO) {
            isDelete.value = true // 삭제 할 때는 ScrollToTop이 필요없음
            deleteSavedMovieUseCase.execute(movieModelResult)
        }
    }

}