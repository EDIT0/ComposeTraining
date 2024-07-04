package com.my.presentation.screen.test

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.my.presentation.screen.test.model.StringListUiState
import com.my.presentation.screen.test.viewmodel.MainViewModel
import com.my.presentation.screen.test.presenter.MainViewModelPresenter

@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel()
) {

    var isSuccess by remember { mutableStateOf(false) }

    MainScreenUI(
        mainViewModelPresenter = {
            when(it) {
                is MainViewModelPresenter.GetStringList -> {
                    isSuccess = !isSuccess

                    if(isSuccess) {
                        mainViewModel.getStringList(it.data1, "Success")
                    } else {
                        mainViewModel.getStringList(it.data1, "Error")
                    }

                }
            }
        },
        stringListUiState = mainViewModel.stringListUiState.collectAsState().value
    )

    LaunchedEffect(key1 = true) {
        mainViewModel.getStringList("Init Data1", "Error")
    }

}

@Composable
fun MainScreenUI(
    mainViewModelPresenter: (MainViewModelPresenter) -> Unit,
    stringListUiState: StringListUiState
) {

    Log.i("MYTAG", "Update StringListUiState\n${stringListUiState}\n")
//    Log.i("MYTAG", "Update SideEffectModel\n${sideEffectModel.code}\n${sideEffectModel.message}\n${sideEffectModel.throwable}\n")

    Column(
        modifier = Modifier
            .padding(50.dp)
            .fillMaxSize()
            .clickable {
                mainViewModelPresenter.invoke(MainViewModelPresenter.GetStringList("Clicked data1"))
            }
    ) {
        Text(
            text =
            "StringListUiState\n" +
                    "${stringListUiState.isLoading}\n" +
                    "${stringListUiState.stringList}\n" +
                    "${stringListUiState.code}\n" +
                    "${stringListUiState.message}\n" +
                    "${stringListUiState.throwable}\n" +
                    "${stringListUiState.isDataEmpty}\n")
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
//    ComposeMVIExample_1Theme {
        MainScreenUI(
            mainViewModelPresenter = { },
            stringListUiState = StringListUiState()
        )
//    }
}