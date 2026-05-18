package com.my.book.library

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.common.component.NetworkStatusBanner
import com.my.book.library.core.navigation.AppNavHost
import com.my.book.library.ui.theme.BooklibraryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BooklibraryTheme {

                val navController = rememberNavController()
                val commonViewModel = hiltViewModel<CommonViewModel>()
                val isNetworkConnected by commonViewModel.isNetworkConnected.collectAsStateWithLifecycle()

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavHost(
                        navHostController = navController,
                        commonViewModel = commonViewModel,
                        onAppOff = {
                            finishAffinity()
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    NetworkStatusBanner(
                        isConnected = isNetworkConnected,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            }
        }
    }
}