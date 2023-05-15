package com.edit.composenavdemo1

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onNavigateToSecondScreen: (String) -> Unit,
    modifier:Modifier = Modifier,
    dataValue1 : String = "",
    dataValue2 : String = ""
){
    var text by remember {
        mutableStateOf(dataValue2)
    }

    Log.i("MYTAG", "HomeScreen ${dataValue1} ${dataValue2}")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(60.dp)
        ,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,

    ){

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = modifier.fillMaxWidth(),
            textStyle = TextStyle(
                color = Color.DarkGray,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

        )

        Button(
            onClick = {
                if(text != "") {
                    onNavigateToSecondScreen(text)
                }

//                navController.navigate("second_screen")
            },
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                text = "Submit",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

