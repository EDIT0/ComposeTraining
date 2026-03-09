package com.my.book.library.core.common.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.my.book.library.core.common.util.LogUtil

interface LifecycleResult {
    fun onEnter()
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onDispose()
}

@Composable
fun LifecycleListener(
    lifecycleOwner: LifecycleOwner,
    screenName: String,
    lifecycleResult: LifecycleResult
) {
    DisposableEffect(lifecycleOwner) {
        LogUtil.i_dev("$screenName Enter")
        lifecycleResult.onEnter()

        val observer = LifecycleEventObserver { _, event ->
            when(event) {
                Lifecycle.Event.ON_START -> {
                    LogUtil.i_dev("$screenName ON_START")
                    lifecycleResult.onStart()
                }
                Lifecycle.Event.ON_RESUME -> {
                    LogUtil.i_dev("$screenName ON_RESUME")
                    lifecycleResult.onResume()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    LogUtil.i_dev("$screenName ON_PAUSE")
                    lifecycleResult.onPause()
                }
                Lifecycle.Event.ON_STOP -> {
                    LogUtil.i_dev("$screenName ON_STOP")
                    lifecycleResult.onStop()
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            LogUtil.i_dev("$screenName Dispose")
            lifecycleResult.onDispose()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}