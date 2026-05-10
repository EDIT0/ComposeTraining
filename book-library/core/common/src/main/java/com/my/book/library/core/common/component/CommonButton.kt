package com.my.book.library.core.common.component

import androidx.annotation.ColorRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.my.book.library.core.common.dpToSp
import com.my.book.library.core.resource.R

@Composable
fun CommonButton(
    modifier: Modifier = Modifier,
    text: String,
    @ColorRes textColorRes: Int,
    @ColorRes backgroundColorRes: Int,
    cornerRadius: Dp = 8.dp,
    isBorderEnabled: Boolean = false,
    @ColorRes borderColorRes: Int = android.R.color.transparent,
    borderWidth: Dp = 1.dp,
    isEnabled: Boolean = true,
    textSize: Dp = 16.dp,
    onClick: () -> Unit
) {
    val textColor = colorResource(textColorRes)
    val backgroundColor = colorResource(backgroundColorRes)
    val borderColor = colorResource(borderColorRes)
    val shape = RoundedCornerShape(cornerRadius)

    val buttonModifier = modifier
        .then(
            if (isBorderEnabled) {
                Modifier.border(width = borderWidth, color = borderColor, shape = shape)
            } else {
                Modifier
            }
        )

    Button(
        onClick = onClick,
        modifier = buttonModifier,
        enabled = isEnabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.3f),
            disabledContentColor = textColor.copy(alpha = 0.3f)
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = text,
            style = TextStyle(
                color = if (isEnabled) textColor else textColor.copy(alpha = 0.3f),
                fontSize = dpToSp(textSize)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommonButtonPreview() {
    CommonButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        text = "지역 선택 시작",
        textColorRes = R.color.color_FFFFFFFF,
        backgroundColorRes = R.color.color_191F28,
        cornerRadius = 12.dp,
        isBorderEnabled = true,
        borderColorRes = R.color.color_6B7684,
        borderWidth = 1.dp,
        isEnabled = true,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun CommonButtonDisabledPreview() {
    CommonButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        text = "지역 선택 시작",
        textColorRes = R.color.color_FFFFFFFF,
        backgroundColorRes = R.color.color_191F28,
        cornerRadius = 12.dp,
        isEnabled = false,
        onClick = {}
    )
}
