package com.edit.sideeffectsexmaple1.ui.compose

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edit.sideeffectsexmaple1.R

@Composable
fun LaunchedEffect(
    onChangeTextCallback: (String) -> (Unit),
    onClickCallback: (String) -> (Unit),
    modifier: Modifier = Modifier
) {

    var isHighlighted by remember { mutableStateOf(false) }
    val backgroundColor = if (isHighlighted) Color.Red else Color.Transparent

    var text by remember {
        mutableStateOf(TextFieldValue(""))
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(colorResource(id = R.color.white))
        ) {
            Text(
                text = "",
                modifier = modifier
                    .width(20.dp)
                    .wrapContentHeight()
                    .background(backgroundColor)
                    .clickable {
                        Log.i("MYTAG", "Spacer Clicked")
                        isHighlighted = !isHighlighted
                    }
            )

            TextField(
                value = text,
                onValueChange = {
                    text = it
                    onChangeTextCallback(it.text)
                },
//                label = { Text("Enter your name") },
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "",
                modifier = modifier
                    .width(20.dp)
                    .background(colorResource(id = R.color.purple_200))
                    .clickable {
                        Log.i("MYTAG", "Spacer Clicked")
                        isHighlighted = !isHighlighted
                    })
        }

        Button(
            onClick = {
                onClickCallback(text.text)
            },
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Insert Number"
            )
        }
    }



}