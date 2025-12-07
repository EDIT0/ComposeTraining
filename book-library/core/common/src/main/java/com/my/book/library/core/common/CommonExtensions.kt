package com.my.book.library.core.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
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


/**
 * 특정 텍스트 강조
 *
 * @param keywords
 * @param highlightStyle
 * @return
 */
fun String.highlightKeywords(
    keywords: List<String>,
    highlightStyle: SpanStyle
): AnnotatedString {
    return buildAnnotatedString {
        var remainingText = this@highlightKeywords
        var processedLength = 0

        while (remainingText.isNotEmpty()) {
            val firstMatch = keywords
                .mapNotNull { keyword ->
                    val index = remainingText.indexOf(keyword,
                        ignoreCase = true)
                    if (index >= 0) index to keyword else null
                }
                .minByOrNull { it.first }

            if (firstMatch == null) {
                append(remainingText)
                break
            }

            val (index, keyword) = firstMatch

            // 매칭 전 텍스트
            append(remainingText.substring(0, index))

            // 매칭된 텍스트 (강조)
            withStyle(style = highlightStyle) {
                append(remainingText.substring(index, index +
                        keyword.length))
            }

            remainingText = remainingText.substring(index +
                    keyword.length)
        }
    }
}
