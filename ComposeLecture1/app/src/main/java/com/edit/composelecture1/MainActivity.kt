package com.edit.composelecture1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edit.composelecture1.ui.theme.ComposeLecture1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            /**
             * UI가 겹치지 않도록 하는 3가지 방법
             * Column(Vertical), Row(Horizontal), Box
             * */

            Column(
                modifier = Modifier
                    .background(color = colorResource(id = R.color.yellow))
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Greeting("AB")
                    Greeting("CDEF")
                    Greeting("G")
                }

                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Greeting("AB")
                    Greeting("CDEF")
                    Greeting("G")
                }
            }

        }
    }
}

@Composable
fun Greeting(name: String) {
    /**
     * Modifier에는 4가지 상호작용 요소 있음
     * clickable, zoomable, draggable, scrollable
     * */
    Text(
        text = "$name",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Red,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .background(color = colorResource(id = R.color.purple_200))
            .border(2.dp, color = colorResource(id = R.color.black))
            .padding(10.dp)

    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeLecture1Theme {
        Greeting("Android")
    }
}