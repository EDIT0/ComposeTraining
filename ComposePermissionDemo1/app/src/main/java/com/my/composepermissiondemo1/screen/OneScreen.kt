package com.my.composepermissiondemo1.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.my.composepermissiondemo1.component.CommonDialog
import com.my.composepermissiondemo1.event.PermissionSideEvent
import com.my.composepermissiondemo1.event.PermissionViewModelEvent
import com.my.composepermissiondemo1.lifecycle.LifecycleEventEffect
import com.my.composepermissiondemo1.model.CommonDialogModel
import com.my.composepermissiondemo1.navigation.NavigationScreenName
import com.my.composepermissiondemo1.util.LogUtil
import com.my.composepermissiondemo1.util.PermissionManager

@Composable
fun OneScreen(
    navController: NavController,
    oneViewModel: OneViewModel = hiltViewModel()
) {
    LogUtil.d_dev("Start OneScreen")
    val localContext = LocalContext.current

    val showCommonDialog = remember {
        mutableStateOf(CommonDialogModel(false, "", ""))
    }

    val permissionSideEvent = oneViewModel.permissionSideEvent.collectAsState(initial = PermissionSideEvent.Init()).value

    val requestAllPermissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // 퍼미션 결과에 대한 응답
        PermissionManager.requestAllPermissionsLauncherLogic(localContext, permissions)
    }

    LaunchedEffect(key1 = permissionSideEvent) {
        when(permissionSideEvent) {
            is PermissionSideEvent.Init -> { }
            is PermissionSideEvent.RequestPermission -> {
                PermissionManager.requestPermission(
                    requestAllPermissionsLauncher = requestAllPermissionsLauncher,
                    permissionArray = PermissionManager.getAllPermissions()
                )
            }
        }
    }

    OneScreenUI(
        permissionViewModelEvent = {
            when(it) {
                is PermissionViewModelEvent.RequestPermission -> {
                    oneViewModel.requestPermission()
                }
            }
        }
    )

    PermissionManager.setPermissionResultCallback(object : PermissionManager.ResultCallback {
        override fun result(enable: Boolean) {
            LogUtil.d_dev("result : ${enable}")
            navController.navigate(route = NavigationScreenName.TwoScreen.name)
        }

        override fun dialog(message: String, buttonText: String) {
            showCommonDialog.value = showCommonDialog.value.copy(isShow = true, message = message, buttonText = buttonText)
        }
    })

    if(showCommonDialog.value.isShow) {
        CommonDialog(
            title = null,
            message = showCommonDialog.value.message,
            negativeText = null,
            positiveText = showCommonDialog.value.buttonText,
            onNegativeClick = { },
            onPositiveClick = {
                showCommonDialog.value = showCommonDialog.value.copy(isShow = false, message = "", buttonText = "")
                PermissionManager.moveToPermissionSettingPage(localContext)
            },
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    }

    OnResume(
        oneViewModel = oneViewModel
    )

}

@Composable
fun OneScreenUI(
    permissionViewModelEvent: (PermissionViewModelEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                permissionViewModelEvent.invoke(PermissionViewModelEvent.RequestPermission())
            }
        ) {
            Text(text = "권한 확인하기")
        }
    }
}

@Composable
fun OnResume(
    oneViewModel: OneViewModel
) {
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        if(!oneViewModel.getIsAlreadyRequest()) {
            oneViewModel.requestPermission()
        } else {
            oneViewModel.setIsAlreadyRequest(false)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOneScreen() {
    OneScreenUI(
        permissionViewModelEvent = { }
    )
}