package com.my.book.library.core.common.component

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.my.book.library.core.common.dpToSp
import com.my.book.library.core.common.noRippleClickable

@Composable
fun CommonActionBar(
    context: Context,
    modifier: Modifier = Modifier,
    actionBarTitle: String,
    onBackClick: () -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .noRippleClickable {
                        onBackClick.invoke()
                    }
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "",
                    modifier = Modifier
                        .size(24.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier,
                    maxLines = 1,
                    text = actionBarTitle,
                    style = TextStyle(
                        fontSize = dpToSp(18.dp)
                    )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(44.dp),
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
        onBackClick = {}
    )
}