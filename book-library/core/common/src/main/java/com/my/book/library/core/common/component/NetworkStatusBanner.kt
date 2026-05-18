package com.my.book.library.core.common.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.my.book.library.core.common.dpToSp
import com.my.book.library.core.resource.NotoSansKR
import com.my.book.library.core.resource.R
import kotlinx.coroutines.delay

@Composable
fun NetworkStatusBanner(
    isConnected: Boolean,
    modifier: Modifier = Modifier
) {
    // 연결 복구 시 잠시 보여주다가 사라지게 하기 위한 상태
    var showRestoredMessage by remember { mutableStateOf(false) }
    var previousConnected by remember { mutableStateOf(isConnected) }

    LaunchedEffect(isConnected) {
        if (!previousConnected && isConnected) {
            // 끊겼다가 다시 연결됨 → "연결됨" 메시지 잠깐 표시
            showRestoredMessage = true
            delay(2000)
            showRestoredMessage = false
        }
        previousConnected = isConnected
    }

    val showBanner = !isConnected || showRestoredMessage
    val backgroundColor = colorResource(
        if (isConnected) R.color.color_CC4CAF50 else R.color.color_CCF44336
    )
    val message = stringResource(
        if (isConnected) R.string.common_network_status_connected else R.string.common_network_status_disconnected
    )

    AnimatedVisibility(
        visible = showBanner,
        enter = expandVertically(expandFrom = Alignment.Top),
        exit = shrinkVertically(shrinkTowards = Alignment.Top),
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .statusBarsPadding()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                style = TextStyle(
                    color = colorResource(R.color.color_FFFFFFFF),
                    fontSize = dpToSp(13.dp),
                    fontFamily = NotoSansKR,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
