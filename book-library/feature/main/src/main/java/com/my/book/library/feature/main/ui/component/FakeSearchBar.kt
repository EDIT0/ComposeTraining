package com.my.book.library.feature.main.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.my.book.library.core.resource.Gray300
import com.my.book.library.core.resource.Green500

@Composable
fun FakeSearchBar(
    modifier: Modifier,
    text: String,
    textColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
            .clickable {
                onClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(weight = 1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                maxLines = 1,
                color = textColor
            )
        }
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "",
            modifier = Modifier
                .size(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FakeSearchBarPreview() {
    FakeSearchBar(
        modifier = Modifier
            .background(
                color = Green500,
                shape = RoundedCornerShape(size = 10.dp)
            ),
        text = "Search anything you want",
        textColor = Gray300
    ) { }
}