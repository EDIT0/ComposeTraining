package com.my.composexmldemo2

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.my.composexmldemo2.compose.HomeActivity
import com.my.composexmldemo2.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT, darkScrim = Color.TRANSPARENT
            )
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        val wic = WindowInsetsControllerCompat(window, window.decorView)
        wic.isAppearanceLightStatusBars = false // true: 어두운 아이콘, false: 밝은 아이콘
        wic.isAppearanceLightNavigationBars = false // true: 어두운 아이콘, false: 밝은 아이콘

        Log.d("MYTAG", "onCreate()")

        lifecycleScope.launch {
            mainViewModel.count.collect {
                Log.d("MYTAG", "count collect ${it}")
            }
        }

        lifecycleScope.launch {
            mainViewModel.dataUiState.collect {
                Log.d("MYTAG", "${it.data1} / ${it.data2}")
                binding.tvText.text = it.data1 + " / " + it.data2
            }
        }

        lifecycleScope.launch {
            mainViewModel.dataUiErrorState.collect {
                Log.e("MYTAG", "${it}")
            }
        }

        lifecycleScope.launch {
            mainViewModel.topBarUiState.collect {
                Log.d("MYTAG", "${it}")
            }
        }

        lifecycleScope.launch {
            mainViewModel.topBarUiErrorState.collect {
                Log.e("MYTAG", "${it}")
            }
        }

        // xml 연동 타입
        binding.layoutComposeView1.setContent {
            Log.d("MYTAG", "layoutComposeView1")
            LayoutComposeView1(mainViewModel = mainViewModel)
        }

        // addView 타입
        binding.layoutComposeView2.addView(
            ComposeView(context = this).apply {
                setContent {
                    val localContext = LocalContext.current
                    Log.d("MYTAG", "layoutComposeView2")
                    LayoutComposeView2(localContext = localContext, mainViewModel = mainViewModel)
                }
            }
        )
    }
}

@Composable
fun LayoutComposeView1(
    mainViewModel: MainViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text1(mainViewModel.text1.value)
        Image()
    }
}

@Composable
fun LayoutComposeView2(
    localContext: Context,
    mainViewModel: MainViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text1(mainViewModel.count.value.toString())
        Image()
        Button(
            onClick = {
                val intent = Intent(localContext, HomeActivity::class.java)
                localContext.startActivity(intent)
            }
        ) {
            Text(text = "HomeActivity 로..")
        }
    }
}

@Composable
fun Text1(text: String) {
    Text(text = text)
}

@Composable
fun Image() {
    Icon(imageVector = Icons.Default.Email, contentDescription = "")
}

@Preview(showBackground = true)
@Composable
private fun PreviewLayoutComposeView1() {
    LayoutComposeView1(mainViewModel = MainViewModel())
}

@Preview(showBackground = true)
@Composable
private fun PreviewLayoutComposeView2() {
    LayoutComposeView2(localContext = LocalContext.current, mainViewModel = MainViewModel())
}