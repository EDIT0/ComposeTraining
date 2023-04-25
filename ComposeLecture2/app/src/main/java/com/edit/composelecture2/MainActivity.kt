package com.edit.composelecture2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.edit.composelecture2.ui.theme.ComposeLecture2Theme
import androidx.compose.ui.layout.ContentScale.Companion as ContentScale1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoxExample1()
            BoxExample2()
            BoxExample3()
        }
    }
}

@Composable
fun BoxExample1() {
    /**
     * Box Layout은 UI를 겹쳐 그릴 수 있다.
     * align으로 각 레이아웃의 위치 조정 가능
     * */
    Box(
        modifier = Modifier
            .background(color = colorResource(id = R.color.teal_200))
            .size(180.dp, 300.dp)
    ) {
        Box(
            modifier = Modifier
                .background(color = colorResource(id = R.color.green))
                .size(125.dp, 100.dp)
                .align(Alignment.TopEnd)
        ) {

        }

        Text(
            text = "Hi",
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .background(color = colorResource(id = R.color.white))
                .size(90.dp, 50.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun BoxExample2() {
    Box(
        modifier = Modifier
            .background(color = colorResource(id = R.color.blue))
            .fillMaxSize()
    ) {
        Text(
            text = "TopStart",
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .background(Color.Yellow)
                .padding(10.dp)
                .align(Alignment.TopStart)
        )

        Text(
            text = "TopCenter",
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .background(Color.Yellow)
                .padding(10.dp)
                .align(Alignment.TopCenter)
        )

        Text(
            text = "TopEnd",
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .background(Color.Yellow)
                .padding(10.dp)
                .align(Alignment.TopEnd)
        )

        Text(
            text = "CenterStart",
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .background(Color.Yellow)
                .padding(10.dp)
                .align(Alignment.CenterStart)
        )

        Text(
            text = "Center",
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .background(Color.Yellow)
                .padding(10.dp)
                .align(Alignment.Center)
        )

        Text(
            text = "CenterEnd",
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .background(Color.Yellow)
                .padding(10.dp)
                .align(Alignment.CenterEnd)
        )

        Text(
            text = "BottomStart",
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .background(Color.Yellow)
                .padding(10.dp)
                .align(Alignment.BottomStart)
        )

        Text(
            text = "BottomCenter",
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .background(Color.Yellow)
                .padding(10.dp)
                .align(Alignment.BottomCenter)
        )

        Text(
            text = "BottomEnd",
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .background(Color.Yellow)
                .padding(10.dp)
                .align(Alignment.BottomEnd)
        )

    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BoxExample3() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
//        GlideImage(
//            model = ImageBitmap.imageResource(R.drawable.ic_launcher_background),
//            contentScale = ContentScale1.Crop,
//            alignment = error(ImageBitmap.imageResource(id = R.drawable.ic_launcher_foreground)),
//            contentDescription = "android robot",
//            modifier = Modifier
//                .fillMaxWidth()
//        )
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "android robot",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
        )
        
        Text(
            text = "Android Robot",
            style = MaterialTheme.typography.h4,
            color = colorResource(id = R.color.purple_700),
            modifier = Modifier
                .align(Alignment.BottomStart)
        )
        
        Button(
            onClick = {
                
            },
            colors = ButtonDefaults.textButtonColors(
                backgroundColor =  colorResource(id = R.color.white),
                contentColor = colorResource(id = R.color.black)
            ),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .border(5.dp, colorResource(id = R.color.green))
        ) {
            Text(text = "Add To Cart")
        }
    }
}

//@Composable
//fun Greeting(name: String) {
//    Text(text = "Hello $name!")
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    ComposeLecture2Theme {
//        Greeting("Android")
//    }
//}