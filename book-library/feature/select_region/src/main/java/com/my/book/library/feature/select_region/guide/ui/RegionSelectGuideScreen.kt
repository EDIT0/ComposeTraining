package com.my.book.library.feature.select_region.guide.ui

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.common.component.CommonActionBar
import com.my.book.library.core.common.component.LifecycleListener
import com.my.book.library.core.common.component.LifecycleResult
import com.my.book.library.core.common.noRippleClickable
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.common.util.SystemBarConfig
import com.my.book.library.core.common.util.SystemBarController
import com.my.book.library.core.resource.Black
import com.my.book.library.core.resource.LibraryData
import com.my.book.library.core.resource.R
import com.my.book.library.feature.select_region.guide.intent.RegionSelectGuideViewModelEvent
import com.my.book.library.feature.select_region.guide.viewmodel.RegionSelectGuideViewModel

@Composable
fun RegionSelectGuideScreen(
    commonViewModel: CommonViewModel,
    onBackPressed: () -> Unit,
    onMoveToRegionSelection: () -> Unit,
    modifier: Modifier
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val commonViewModel = commonViewModel
    val regionSelectGuideViewModel = hiltViewModel<RegionSelectGuideViewModel>()

    val lifecycleResult = remember {
        object : LifecycleResult {
            override fun onEnter() {}
            override fun onStart() {}
            override fun onResume() {}
            override fun onPause() {}
            override fun onStop() {}
            override fun onDispose() {}
        }
    }

    RegionSelectGuideContent(
        localContext = context,
        onMoveToRegionSelection = onMoveToRegionSelection,
        onBackPressed = {
            onBackPressed.invoke()
        },
        modifier = Modifier,
        regionSelectGuideViewModelEvent = {
            when(it) {
                else -> {}
            }
        }
    )

    LifecycleListener(
        lifecycleOwner = lifecycleOwner,
        screenName = object {}.javaClass.enclosingClass?.simpleName ?: "RegionSelectGuideScreen",
        lifecycleResult = lifecycleResult
    )

    BackHandler(
        enabled = true,
        onBack = {
            LogUtil.i_dev("${object {}.javaClass.enclosingClass?.simpleName} BackHandler")
            onBackPressed.invoke()
        }
    )

}

@Composable
fun RegionSelectGuideContent(
    localContext: Context,
    onMoveToRegionSelection: () -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier,
    regionSelectGuideViewModelEvent: (RegionSelectGuideViewModelEvent) -> Unit
) {

    SystemBarController.Setup(
        config = SystemBarConfig(
            statusBarColor = Black,
            statusBarDarkIcons = false,
            useStatusBarSpace = true,
            navigationBarColor = Black,
            navigationBarDarkIcons = false,
            useNavigationBarSpace = true
        )
    ) { state ->
        Scaffold(
            modifier = Modifier
                .padding(top = state.statusBarHeight, bottom = state.navigationBarHeight)
                .consumeWindowInsets(WindowInsets.statusBars)
                .consumeWindowInsets(WindowInsets.navigationBars)
        ) { innerPadding ->
            Box(
                modifier = modifier
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    CommonActionBar(
                        context = localContext,
                        modifier = Modifier
                            .padding(horizontal = 20.dp),
                        actionBarTitle = "지역선택안내 액션바",
                        isShowBackButton = false,
                        onBackClick = {
                            onBackPressed.invoke()
                        }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(text = "이미지")
                        Text(text = "어느 지역에서\n책을 찾을까요?")
                        Text(text = "시도 -> 시군구 -> 순으로 선택하면\n해당 지역의 도서관 소장 정보를 보여드려요.")
                        Button(
                            onClick = {
                                onMoveToRegionSelection.invoke()
                            }
                        ) {
                            Text(text = "지역 선택 시작")
                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun RegionSelectGuideUIPreview() {
    RegionSelectGuideContent(
        localContext = LocalContext.current,
        onMoveToRegionSelection = {},
        onBackPressed = {},
        modifier = Modifier,
        regionSelectGuideViewModelEvent = {},
    )
}