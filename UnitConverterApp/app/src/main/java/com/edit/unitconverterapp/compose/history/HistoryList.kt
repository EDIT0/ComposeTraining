package com.edit.unitconverterapp.compose.history

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.edit.unitconverterapp.data.ConversionResult

@Composable
fun HistoryList(
    list: State<List<ConversionResult>>,
    onCloseTask: (ConversionResult) -> (Unit),
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(
            items = list.value,
            key = { it -> 
                it.id
            }
        ) { item ->  
            HistoryItem(
                messagePart1 = item.messagePart1,
                messagePart2 = item.messagePart2,
                onClose = {
                    onCloseTask(item)
                }
            )
        }
    }
}