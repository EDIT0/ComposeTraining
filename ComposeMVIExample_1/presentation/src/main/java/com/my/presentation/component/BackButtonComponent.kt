package com.my.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.my.presentation.R
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun BackButtonComponent(
    enable: Boolean,
    backButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(15.dp)
            .clickable(
                enabled = enable
            ) {
                backButtonClick.invoke()
            }
    ) {
        GlideImage(
            imageModel = { R.drawable.ic_arrow_back_black_24 },
            previewPlaceholder = painterResource(id = R.drawable.ic_arrow_back_black_24),
            imageOptions = ImageOptions(
                alignment = Alignment.Center
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBackButtonComponent() {
    BackButtonComponent(
        enable = true,
        backButtonClick = { }
    )
}