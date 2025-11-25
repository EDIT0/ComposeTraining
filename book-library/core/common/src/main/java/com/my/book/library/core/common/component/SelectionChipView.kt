package com.my.book.library.core.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun <T>SelectionChipView(
    modifier: Modifier = Modifier,
    options: List<T>,
    selected: T?,
    labelMapper: (T) -> String,
    onSelectedChange: (T?) -> Unit
) {

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = modifier
    ) {
        options.forEach { option ->
            FilterChip(
                selected = option == selected,
                onClick = {
                    if(option == selected) {
                        onSelectedChange.invoke(null)
                    } else {
                        onSelectedChange.invoke(option)
                    }
                },
                label = {
                    Text(text = labelMapper.invoke(option))
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectionChipViewPreview() {
    val list = listOf("전체", "스포츠", "경제", "사회", "국제", "과학", "기술",
        "엔터테인먼트", "정치", "문화", "여행", "자동차")
    SelectionChipView(
        options = list,
        selected = "스포츠",
        labelMapper = { it },
        onSelectedChange = {}
    )
}