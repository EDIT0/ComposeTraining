package com.my.book.library.core.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

/**
 * 버튼 클릭시 리플 효과 삭제
 *
 * @param onClick
 * @return
 */
@Composable
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    return this.clickable(
        indication = null, // ripple 제거
        interactionSource = interactionSource,
        onClick = onClick
    )
}

/**
 * Dp to Sp
 *
 * @param dp
 */
@Composable
fun dpToSp(dp: Dp) = with(LocalDensity.current) { dp.toSp() }