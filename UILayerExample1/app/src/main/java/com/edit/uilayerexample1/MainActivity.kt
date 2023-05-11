package com.edit.uilayerexample1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.edit.uilayerexample1.ui.theme.UILayerExample1Theme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    counterViewModel: CounterViewModel = viewModel()
) {
    val screenState = counterViewModel.screenState.value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true, block = {
        counterViewModel.uiEventFlow.collectLatest {
            when(it) {
                is UIEvent.ShowMessage -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = it.message
                    )
                }
            }
        }
    })

    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(50.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = screenState.displayingResult,
                modifier = modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = Color.DarkGray
            )
            OutlinedTextField(
                value = screenState.inputValue,
                onValueChange = {
                    counterViewModel.onEvent(CounterEvent.ValueEntered(it))
                },
                modifier = modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                textStyle = TextStyle(
                    color = Color.LightGray,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                ),
                label = {
                    Text(text = "New Count")
                }
            )
            if (screenState.isCountButtonVisible) {
                Button(
                    onClick = {
                        counterViewModel.onEvent(CounterEvent.CountButtonClicked)
                    },
                    modifier = modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Count",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Button(
                onClick = {
                    counterViewModel.onEvent(CounterEvent.ResetButtonClicked)
                },
                modifier = modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Reset",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}