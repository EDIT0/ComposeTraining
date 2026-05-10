package com.my.book.library.core.common.component

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.my.book.library.core.common.dpToSp
import com.my.book.library.core.common.noRippleClickable
import com.my.book.library.core.resource.NotoSansKR
import com.my.book.library.core.resource.R

@Composable
fun CommonActionBar(
    context: Context,
    modifier: Modifier = Modifier,
    actionBarTitle: String,
    isShowBackButton: Boolean,
    onBackClick: () -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(color = colorResource(R.color.color_FFFFFFFF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {
            if(isShowBackButton) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .noRippleClickable {
                            onBackClick.invoke()
                        }
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_left_black_40x40),
                        contentDescription = null,
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    maxLines = 1,
                    text = actionBarTitle,
                    style = TextStyle(
                        color = colorResource(R.color.color_191F28),
                        fontSize = dpToSp(16.dp),
                        lineHeight = dpToSp(24.dp),
                        fontFamily = NotoSansKR,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(64.dp)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.Center
            ) {
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonActionBarPreview() {
    CommonActionBar(
        context = LocalContext.current,
        modifier = Modifier,
        actionBarTitle = "Action Bar Title",
        isShowBackButton = true,
        onBackClick = {},
    )
}