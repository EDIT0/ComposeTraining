package com.my.jetpackcomposebasic.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

/**
 * primary
 * 앱의 주요 브랜드 색상
 * 버튼, FAB, 체크박스, 활성 상태 아이콘 등
 *
 * onPrimary
 * primary 위에 올려지는 글자/아이콘 색상
 * primary 배경 버튼 위 텍스트 색
 *
 * secondary
 * 보조 강조 색상
 * 토글, 선택 강조, 서브 버튼 등
 *
 * onSecondary
 * secondary 배경 위 텍스트/아이콘 색
 * secondary 버튼 텍스트 색
 *
 * surface
 * 카드, 시트, 배경 등 앱의 기본 표면 색
 * Card, BottomSheet, Dialog 배경
 *
 * onSurface
 * surface 위 텍스트/아이콘 색
 * Card 안 글자, Dialog 텍스트
 *
 * background
 * 앱의 기본 배경 색
 * Scaffold 배경, Activity 배경
 *
 * onBackground
 * background 위 텍스트/아이콘 색
 * 일반 텍스트, 기본 화면 내용
 *
 * error
 * 오류 상태 색상
 * TextField 오류, SnackBar
 *
 * onError
 * error 배경 위 텍스트 색
 * 오류 메시지 텍스트
 * */
private val DarkColorScheme = darkColorScheme(
    surface = Blue,
    onSurface = Navy,
    primary = Navy,
    onPrimary = Chartreuse
)

private val LightColorScheme = lightColorScheme(
    surface = Blue,
    onSurface = Color.White,
    primary = LightBlue,
    onPrimary = Navy
)

//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80
//)

//private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40
//
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//    */
//)

@Composable
fun JetpackComposeBasicTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}