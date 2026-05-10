package com.my.book.library.feature.select_region.guide.ui

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.common.component.CommonButton
import com.my.book.library.core.common.component.LifecycleListener
import com.my.book.library.core.common.component.LifecycleResult
import com.my.book.library.core.common.dpToSp
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.common.util.SystemBarConfig
import com.my.book.library.core.common.util.SystemBarController
import com.my.book.library.core.resource.NotoSansKR
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

    val useStatusBarSpace = false
    val useNavigationBarSpace = true

    SystemBarController.Setup(
        config = SystemBarConfig(
            statusBarColor = colorResource(R.color.color_FFFFFFFF),
            statusBarDarkIcons = true,
            useStatusBarSpace = useStatusBarSpace,
            navigationBarColor = colorResource(R.color.color_FFFFFFFF),
            navigationBarDarkIcons = true,
            useNavigationBarSpace = useNavigationBarSpace
        )
    ) { state ->
        Scaffold(
            modifier = Modifier
                .padding(top = if(useStatusBarSpace) {state.statusBarHeight} else {0.dp}, bottom = if(useNavigationBarSpace) {state.navigationBarHeight} else {0.dp})
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
                        .background(color = colorResource(R.color.color_FFFFFFFF))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 20.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_point_marker_blue),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(top = 44.dp + if(useNavigationBarSpace) {state.navigationBarHeight} else {0.dp})
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = stringResource(R.string.select_region_title),
                            style = TextStyle(
                                color = colorResource(R.color.color_191F28),
                                fontSize = dpToSp(26.dp),
                                lineHeight = dpToSp(35.dp),
                                fontFamily = NotoSansKR,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = stringResource(R.string.select_region_subtitle),
                            style = TextStyle(
                                color = colorResource(R.color.color_6B7684),
                                fontSize = dpToSp(16.dp),
                                lineHeight = dpToSp(24.dp),
                                fontFamily = NotoSansKR,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                    ) {
                        CommonButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 24.dp),
                            text = stringResource(R.string.select_region_next_button_title),
                            textColorRes = R.color.color_FFFFFFFF,
                            backgroundColorRes = R.color.color_3182F6,
                            cornerRadius = 12.dp,
                            isBorderEnabled = false,
                            isEnabled = true,
                            textSize = 16.dp,
                            onClick = {
                                onMoveToRegionSelection.invoke()
                            }
                        )
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