package com.edit.composelecture6

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.edit.composelecture6.ui.theme.ComposeLecture6Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val numberFlow = flow<Int> {
            for(i in 0 until 100) {
                emit(i)
                delay(500L)
            }
        }

        setContent {
            val viewModel = viewModel<MyViewModel>()

            val numberValueFlow = numberFlow.collectAsState(initial = 1).value

            ComposeLecture6Theme {
                Column(
                    modifier = Modifier
                        .border(10.dp, Color.Yellow)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyButton()
                    MyButtonWithDelegatedProperties()

                    var count by rememberSaveable() {
                        mutableStateOf(0)
                    }

                    MyButtonHoisting(count) {
                        Log.i("MYTAG", "실행5")
                        count = it + 1
                        Log.i("MYTAG", "실행6")
                    }

                    var viewModelCount = viewModel.count
                    MyButtonHoisting(viewModelCount) {
                        viewModel.increaseCount()
                    }

                    Text(text = "Number: ${numberValueFlow}")

                }
            }
        }
    }
}

/**
 * 공통적으로 configuration 변경에 대해 상태를 유지하고 싶다면 remember 대신에 rememberSaveable 사용
 * 그러나 이보다 viewModel 사용 권장
 * */

var toast: Toast? = null
// Recomposition 하려면 상태를 관찰할 수 있도록 Observable 변수로 선언
// Global 변수임
// 추천되지 않는 방식
//var count: MutableState<Int> = mutableStateOf(0)

@Preview(name = "MyButton")
@Composable
fun MyButton() {
    val context = LocalContext.current
//    val count: MutableState<Int> = mutableStateOf(0) // 오류가 뜨는 이유는 recomposition이 일어날 때 UI 컴포넌트를 다시 recreate 하기 때문이다. (그럼 다시 초기화된다..)
    // remember는 recomposition 될 때 재생성을 막아주는 방패 역할을 한다.
    val count: MutableState<Int> = remember {
        mutableStateOf(0)
    }
    /**
     * In Jetpack compose, we can treat composables as separate components.
     * Same composable can be used by different places and different screens of the app.
     * If we call the same composable from different parts of the screen app will create different UI elements, each with its own version of the state.
     * The composable function will automatically be "subscribed" to the state.
     * If the state changes, composables that read these fields will be recomposed to display the updates.
     * */

    Button(
        onClick = {
            count.value = count.value + 1
            toast?.cancel()
            toast = Toast.makeText(context, "Clicked Button ${count.value}", Toast.LENGTH_SHORT)
            toast?.show()
        },
        modifier = Modifier
            .padding(10.dp)
            .border(5.dp, Color.Yellow),
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = Color.Gray,
            contentColor = Color.Blue
        )
    ) {
        Text(
            text = "Count is ${count.value}",
            color = Color.White
        )
    }
}

@Composable
fun MyButtonWithDelegatedProperties() {
    val context = LocalContext.current
    /**
     * 코틀린 프로펄티 델리게이트
     * */
    var count by remember {
        mutableStateOf(0)
    }

    Button(
        onClick = {
            count = count + 1
            toast?.cancel()
            toast = Toast.makeText(context, "Clicked Button ${count}", Toast.LENGTH_SHORT)
            toast?.show()
        },
        modifier = Modifier
            .padding(10.dp)
            .border(5.dp, Color.Yellow),
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = Color.Gray,
            contentColor = Color.Blue
        )
    ) {
        Text(
            text = "Count is ${count}",
            color = Color.White
        )
    }
}


// 상태 호이스팅 패턴
// 현재 상태를 나타내는 currentCount 파라미터와 currentCount의 변화를 요청하는 이벤트 파라미터
@Composable
fun MyButtonHoisting(currentCount: Int, updateCount: (Int) -> (Unit)) {
    val context = LocalContext.current

    /**
     * Stateful composable 은 직접 상태를 컨트롤 할 필요가 없을 때 유용하다. (상태 관리 x)
     * But, problem is composables with internal state are less reusable and they are very difficult to test.
     * Therefore , the best practice to handle the states, recommended in jetpack compose is moving states up to caller, and make composable components stateless.
     * This pattern is called state hoisting.
     * */

    Button(
        onClick = {
            Log.i("MYTAG", "실행1")
            updateCount(currentCount)
            Log.i("MYTAG", "실행2")
            toast?.cancel()
            toast = Toast.makeText(context, "Clicked Button ${currentCount}", Toast.LENGTH_SHORT)
            Log.i("MYTAG", "실행4")
            toast?.show()
        },
        modifier = Modifier
            .padding(10.dp)
            .border(5.dp, Color.Yellow),
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = Color.Gray,
            contentColor = Color.Blue
        )
    ) {
        Log.i("MYTAG", "실행3")
        Text(
            text = "Count is ${currentCount}",
            color = Color.White
        )
    }
}