package com.my.composepermissiondemo1.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.my.composepermissiondemo1.R

// Base
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


// Custom
@Composable
fun transparent() : Color = colorResource(id = R.color.transparent)

@Composable
fun white() : Color = colorResource(id = R.color.white)

@Composable
fun black() : Color = colorResource(id = R.color.black)

@Composable
fun cyan500() : Color = colorResource(id = R.color.cyan_500)

@Composable
fun blue500() : Color = colorResource(id = R.color.blue_500)

@Composable
fun blueGrey100() : Color = colorResource(id = R.color.blue_grey_100)
@Composable
fun blueGrey200() : Color = colorResource(id = R.color.blue_grey_200)

@Composable
fun grey400() : Color = colorResource(id = R.color.grey_400)
@Composable
fun grey700() : Color = colorResource(id = R.color.grey_700)
@Composable
fun grey900() : Color = colorResource(id = R.color.grey_900)

@Composable
fun red700() : Color = colorResource(id = R.color.red_700)