package com.example.traveltracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.linker.core.designsystem.R

val isCheckFontFamily = FontFamily(
    Font(R.font.is_check_regular)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = isCheckFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
        textAlign = TextAlign.Right
    ),
    displayMedium = TextStyle(
        fontFamily = isCheckFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        textAlign = TextAlign.Right
    ),
    displaySmall = TextStyle(
        fontFamily = isCheckFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        textAlign = TextAlign.Right
    ),
    headlineLarge = TextStyle(
        fontFamily = isCheckFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        textAlign = TextAlign.Right
    ),
    headlineMedium = TextStyle(
        fontFamily = isCheckFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        textAlign = TextAlign.Right
    ),
    headlineSmall = TextStyle(
        fontFamily = isCheckFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        textAlign = TextAlign.Right,
    ),
    titleLarge = TextStyle(
        fontFamily = isCheckFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        textAlign = TextAlign.Right,
    ),
    titleMedium = TextStyle(
        fontFamily = isCheckFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp,
        textAlign = TextAlign.Right
    ),
    titleSmall = TextStyle(
        fontFamily = isCheckFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        textAlign = TextAlign.Right
    ),
    bodyLarge = TextStyle(
        fontFamily = isCheckFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Right,
    ),
    bodyMedium = TextStyle(
        fontFamily = isCheckFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
        textAlign = TextAlign.Right
    ),
    bodySmall = TextStyle(
        fontFamily = isCheckFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
        textAlign = TextAlign.Right
    ),
    labelLarge = TextStyle(
        fontFamily = isCheckFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        textAlign = TextAlign.Right
    ),
    labelMedium = TextStyle(
        fontFamily = isCheckFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Right
    ),
    labelSmall = TextStyle(
        fontFamily = isCheckFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        textAlign = TextAlign.Right
    )
)