package com.my.composebottomsheetdemo1

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import com.kakao.sdk.common.util.Utility
import com.kakao.vectormap.KakaoMapSdk
import com.my.composebottomsheetdemo1.navigation.MainNavHost
import com.my.composebottomsheetdemo1.ui.theme.ComposeBottomSheetDemo1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge() // 주석처리 필수 -> Navigationbar overlap
//        WindowCompat.setDecorFitsSystemWindows(window, false)

//        window.statusBarColor = getColor(R.color.white)
//        window.navigationBarColor = getColor(R.color.white)
//        WindowInsetsControllerCompat(window, this.window.decorView).run {
//            isAppearanceLightStatusBars = true // 아이콘 검정: true 화이트: false
//            isAppearanceLightNavigationBars = true // 아이콘 검정: true 화이트: false
//        }

        var keyHash = Utility.getKeyHash(this)
        LogUtil.d_dev("카카오 키해시: ${keyHash}")
        KakaoMapSdk.init(this, "fdce39935e06339595f64e877052af36")

        setContent {
            ComposeBottomSheetDemo1Theme {
                val navController = rememberNavController()

//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(Color.White)
//                        .padding(
//                            top = WindowInsets.statusBars
//                                .asPaddingValues()
//                                .calculateTopPadding(),
//                            bottom = WindowInsets.navigationBars
//                                .asPaddingValues()
//                                .calculateBottomPadding()
//                        )
//                ) {
//                    MainNavHost(navHostController = navController)
//                }
//                window.apply { // 이 부분은 WindowCompat.setDecorFitsSystemWindows 이거 필요없음
//                    // 상태바를 투명하게 만듭니다.
//                    statusBarColor = Color.Transparent.value.toInt()
//                    // 상태바가 투명해지도록 설정합니다.
//                    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                }
                window.statusBarColor = Color.White.value.toInt()
                WindowInsetsControllerCompat(window, window.decorView).run {
                    isAppearanceLightStatusBars = true // 아이콘 검정: true 화이트: false
                }

                Box {
                    MainNavHost(navHostController = navController)
                }



//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//
//                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeBottomSheetDemo1Theme {
        Greeting("Android")
    }
}