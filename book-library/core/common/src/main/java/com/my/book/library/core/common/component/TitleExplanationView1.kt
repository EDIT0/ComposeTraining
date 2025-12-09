package com.my.book.library.core.common.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.my.book.library.core.common.dpToSp
import com.my.book.library.core.common.noRippleClickable
import com.my.book.library.core.resource.Gray500

@Composable
fun TitleExplanationView1(
    modifier: Modifier = Modifier,
    title: String,
    explanation: String,
    onExplanationTextClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = Color.Black,
                fontSize = dpToSp(18.dp)
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = explanation,
            style = TextStyle(
                color = Gray500,
                fontSize = dpToSp(16.dp)
            ),
            modifier = Modifier
                .noRippleClickable {
                    onExplanationTextClick.invoke(explanation)
                }
        )
    }
}