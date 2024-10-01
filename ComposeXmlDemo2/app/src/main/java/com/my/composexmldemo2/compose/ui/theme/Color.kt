package com.my.composexmldemo2.compose.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.my.composexmldemo2.R

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

@Composable fun purple200() : Color = colorResource(id = R.color.purple_200)
@Composable fun purple500() : Color = colorResource(id = R.color.purple_500)
@Composable fun purple700() : Color = colorResource(id = R.color.purple_700)
@Composable fun teal200() : Color = colorResource(id = R.color.teal_200)
@Composable fun teal700() : Color = colorResource(id = R.color.teal_700)
@Composable fun black() : Color = colorResource(id = R.color.black)
@Composable fun white() : Color = colorResource(id = R.color.white)

@Composable fun yellow() : Color = colorResource(id = R.color.yellow)
