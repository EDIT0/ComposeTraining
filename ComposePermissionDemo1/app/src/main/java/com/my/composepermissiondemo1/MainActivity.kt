package com.my.composepermissiondemo1

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.my.composepermissiondemo1.component.CommonDialog
import com.my.composepermissiondemo1.model.CommonDialogModel
import com.my.composepermissiondemo1.navigation.MainNavHost
import com.my.composepermissiondemo1.navigation.NavigationScreenName
import com.my.composepermissiondemo1.ui.theme.ComposePermissionDemo1Theme
import com.my.composepermissiondemo1.util.LogUtil
import com.my.composepermissiondemo1.util.PermissionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var currentRoute: String? = null
    private val mainViewModel: MainViewModel by viewModels()
    private var isShowDialogWithOnResume by mutableStateOf(false)

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentRoute = savedInstanceState?.getString("savedCurrentRoute")

        LogUtil.d_dev("안녕..")
        enableEdgeToEdge()
        setContent {
            ComposePermissionDemo1Theme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                val showCommonDialog = remember {
                    mutableStateOf(CommonDialogModel(false, "", ""))
                }


                if(isShowDialogWithOnResume) {
                    showCommonDialog.value = showCommonDialog.value.copy(isShow = true, message = "message", buttonText = "buttonText")
                } else {
                    showCommonDialog.value = showCommonDialog.value.copy(isShow = false, message = "", buttonText = "")
                }

                val navController = rememberNavController()
                    MainNavHost(
                        navHostController = navController
                    )

                // Observe current route in a LaunchedEffect
                LaunchedEffect(navController) {
                    navController.currentBackStackEntryFlow.collect { backStackEntry ->
                        val tmpCurrentRoute = backStackEntry.destination.route
                        LogUtil.d_dev("Current Route: $currentRoute")
                        currentRoute = tmpCurrentRoute
                    }
                }

                if(showCommonDialog.value.isShow) {
                    CommonDialog(
                        title = null,
                        message = showCommonDialog.value.message,
                        negativeText = null,
                        positiveText = showCommonDialog.value.buttonText,
                        onNegativeClick = { },
                        onPositiveClick = {
                            showCommonDialog.value = showCommonDialog.value.copy(isShow = false, message = "", buttonText = "")
                            PermissionManager.moveToPermissionSettingPage(this)
                        },
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false
                    )
                }
//                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        currentRoute?.let {
            when(currentRoute) {
                NavigationScreenName.OneScreen.name -> {
                    LogUtil.d_dev("권한 체크 Off")
                }
                else -> {
                    LogUtil.d_dev("권한 체크 On")
                    if(!PermissionManager.checkAllPermissions(this)) {
                        LogUtil.d_dev("권한이 부족합니다.")
                        isShowDialogWithOnResume = true
                    } else {
                        LogUtil.d_dev("권한이 모두 승인된 상태입니다.")
                        isShowDialogWithOnResume = false
                    }
                }
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("savedCurrentRoute", currentRoute)
    }
}

