package com.edit.sideeffectsexmaple1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.edit.sideeffectsexmaple1.ui.compose.LaunchedEffect
import com.edit.sideeffectsexmaple1.ui.theme.SideEffectsExmaple1Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * LaunchedEffect
 * rememberCoroutineScope
 * rememberUpdatedState
 * DisposableEffect
 * SideEffect
 * produceState
 * derivedStateOf
 * snapshotFlow
 * */
class MainActivity : ComponentActivity() {
    lateinit var mActivity : Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivity = this

        setContent {
            MainScreen()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(
    context:Context = LocalContext.current
) {
    var toast : Toast? = null

    val keyboardController = LocalSoftwareKeyboardController.current

    var totalCount by remember {
        mutableStateOf(0.0)
    }

    var inputCount by remember {
        mutableStateOf("숫자 입력")
    }

    val rememberScaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    // For rememberUpdatedState
    /**
     * 값이 들어올 때 마다 새로운 value 값을 remember된 MutableState에 넣어준다.
     * */
    val rememberInput by remember {
        mutableStateOf(inputCount)
    }
    val rememberUpdatedStateInput by rememberUpdatedState(inputCount)

    // For LaunchedEffect
    var key by remember {
        mutableStateOf(0)
    }


    /**
     * DisposableEffect는 key 값이 업데이트 될 때 마다 onDispose 실행 후 다시 1번 코드를 실행한다.
     * */
    DisposableEffect(key1 = key, effect = {
        // 1번
        Log.i("MYTAG", "Add Observer")

        onDispose {
            Log.i("MYTAG", "onDispose")
            Log.i("MYTAG", "Remove Observer")
        }
    })

    Scaffold(scaffoldState = rememberScaffoldState) {

        /**
         * key 값이 변하면 리컴포지션 될 때 다시 호출
         * 각 호출 시 마다 코루틴이 생성되었다가 끝나면 바로 종료
         * */
        LaunchedEffect(
            key1 = key,
            block = {
                rememberScaffoldState.snackbarHostState.showSnackbar(
                    message = "LaunchedEffect!",
                    actionLabel = "Done",
                    duration = SnackbarDuration.Short
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp, 30.dp, 30.dp, 30.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "Total is ${totalCount}",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.black),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            TextField(
                value = inputCount,
                onValueChange = {
                    inputCount = it
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    disabledTextColor = Color.Transparent,
                    backgroundColor = Color.Yellow,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(20),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        val input = inputCount.toDoubleOrNull()
                        if (input != null) {
                            totalCount += inputCount.toDouble()
                            inputCount = ""
                            /**
                             * 컴포저블 외부에 있을 때 rememberCoroutineScope() 사용하면 범위를 확보하여 코루틴 실행 가능
                             * */
                            coroutineScope.launch {
                                rememberScaffoldState.snackbarHostState.showSnackbar(
                                    message = "Count updated ${totalCount}",
                                    duration = SnackbarDuration.Short
                                )
                            }
                            key++
                            keyboardController?.hide()
                        } else {
                            toast?.cancel()
                            toast = Toast.makeText(context, "Please, write only number", Toast.LENGTH_SHORT)
                            toast?.show()
                        }
                    }
                )
            )

            Text(
                text = "${inputCount}"
            )

            Text(
                text = "remember ${rememberInput}"
            )

            Text(
                text = "rememberUpdatedState ${rememberUpdatedStateInput}"
            )

            Button(
                onClick = {
                    val input = inputCount.toDoubleOrNull()
                    if (input != null) {
                        totalCount += inputCount.toDouble()
                        inputCount = ""
                        coroutineScope.launch {
                            rememberScaffoldState.snackbarHostState.showSnackbar(
                                message = "Count updated ${totalCount}",
                                duration = SnackbarDuration.Short
                            )
                        }
                        keyboardController?.hide()
                    } else {
                        toast?.cancel()
                        toast = Toast.makeText(context, "Please, write only number", Toast.LENGTH_SHORT)
                        toast?.show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Count",
                    fontSize = 30.sp
                )
            }
            
            Button(
                onClick = {
                    context.startActivity(Intent(context, SideEffectActivity::class.java))
                }
            ) {
                Text(text = "Go to the SideEffectActivity")
            }
        }

    }

}