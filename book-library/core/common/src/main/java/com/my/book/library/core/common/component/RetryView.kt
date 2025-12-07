package com.my.book.library.core.common.component

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun RetryView(
    localContext: Context,
    retry: () -> Unit,
    message: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                retry.invoke()
            }
        ) {
            Text(localContext.getString(com.my.book.library.core.resource.R.string.common_component_retry_title))
        }
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = message,
            textAlign = TextAlign.Center
        )
    }
}