package com.edit.composelecture7

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.edit.composelecture7.ui.theme.ComposeLecture7Theme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private val myViewModel2 = MyViewModel2(0)
    private val myViewModel3 = MyViewModel3(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val myViewModel = viewModel<MyViewModel>()
            val currentValue = myViewModel.numberFlow.collectAsState(initial = 1).value

            Text(
                text = "Current index is ${currentValue}",
                fontSize = 25.sp
            )
        }

        // StateFlow MyViewModel2
        /**
         * LiveData는 View가 Stop 상태가 되면 자동으로 등록을 해제하지만, StateFlow는 자동으로 해주지 않는다.
         * 이 이유로 우리는 lifecycleScope를 사용한다.
         * launchWhenCreated는 View 상태가 Created 상태일 때만 실행된다.
         * */
        lifecycleScope.launchWhenCreated {
            myViewModel2.flowTotal.collect {
                Log.i("MYTAG", "State Flow ${it}")
            }
        }

        lifecycleScope.launchWhenCreated {
            for(i in 0 .. 100) {
                Log.i("MYTAG", "Change Value State ${i}")
                myViewModel2.setTotal(i)
                delay(1000L)
            }
        }

        // SharedFlow MyViewModel3
        lifecycleScope.launchWhenCreated {
            myViewModel3.message.collect {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launchWhenCreated {
            myViewModel3.flowTotal.collect {
                Log.i("MYTAG", "Shared Flow ${it}")
            }
        }

        lifecycleScope.launchWhenCreated {
            for(i in 0 .. 100) {
                Log.i("MYTAG", "Change Value Shared ${i}")
                myViewModel3.setTotal(i)
                delay(1000L)
            }
        }
    }
}