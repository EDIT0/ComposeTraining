package com.edit.composelecture3

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                ButtonDemo()
            }
        }
    }
}
private var toast: Toast? = null

@Composable
fun ButtonDemo() {
    val context = LocalContext.current
    Button(onClick = {
        Toast.makeText(context, "Clicked on Button", Toast.LENGTH_SHORT).show()
    }) {
        Text(text = "Add To Cart")
    }

    Button(onClick = {
        Toast.makeText(context, "Clicked on Button", Toast.LENGTH_SHORT).show()
    }, enabled = false) {
        Text(text = "Add To Cart")
    }

    TextButton(onClick = {
        Toast.makeText(context, "Clicked on Text Button", Toast.LENGTH_SHORT).show()
    }) {
        Text(text = "Add To Cart")
    }

    OutlinedButton(onClick = {
        showToast(context, "Clicked on Outlined Button")
    }) {
        Text(text = "Add To Cart")
    }

    IconButton(onClick = {
        showToast(context, "Clicked on Icon Button")
    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_android_black_24dp_black),
            contentDescription = "",
            tint = colorResource(id = R.color.teal_700)
        )
    }

    IconButton(onClick = {
        showToast(context, "Clicked on Icon Button")
    }, modifier = Modifier
        .background(colorResource(id = R.color.orange))) {
        Icon(
            Icons.Filled.Star,
            contentDescription = "",
            tint = colorResource(id = R.color.purple_500),
            modifier = Modifier
                .size(24.dp)
                .background(color = colorResource(id = R.color.black))
        )
    }

    Button(onClick = {
        showToast(context, "Clicked on Padding Button")
    },
    contentPadding = PaddingValues(16.dp),
        border = BorderStroke(10.dp, color = colorResource(id = R.color.orange)),
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = colorResource(id = R.color.green),
            contentColor = colorResource(id = R.color.yellow)
        )
    ) {
        Text(
            text = "Add To Cart",
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .padding(5.dp)
        )
    }

    Button(onClick = {
        showToast(context, "Clicked on Padding Button")
    },
        shape = CutCornerShape(10.dp),
        contentPadding = PaddingValues(16.dp),
        border = BorderStroke(10.dp, color = colorResource(id = R.color.orange)),
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = colorResource(id = R.color.green),
            contentColor = colorResource(id = R.color.yellow)
        )
    ) {
        Text(
            text = "Add To Cart",
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .padding(5.dp)
        )
    }

    Button(onClick = {
        showToast(context, "Clicked on Padding Button")
    },
        shape = CircleShape,
        contentPadding = PaddingValues(16.dp),
        border = BorderStroke(10.dp, color = colorResource(id = R.color.orange)),
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = colorResource(id = R.color.green),
            contentColor = colorResource(id = R.color.yellow)
        )
    ) {
        Text(
            text = "Add To Cart",
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .padding(5.dp)
        )
    }
}

fun showToast(context: Context, str: String) {
    toast?.cancel()
    toast = Toast.makeText(context, str, Toast.LENGTH_SHORT)
    toast?.show()
}