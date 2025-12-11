package com.sultonuzdev.pft.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.sultonuzdev.pft.R


val RobotoFontFamily = FontFamily(
        Font( R.font.roboto_light, FontWeight.Light),
        Font(R.font.roboto_regular, FontWeight.Normal),
        Font(R.font.roboto_medium, FontWeight.Medium),
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

val LibreBaskervilleFontFamily: FontFamily
    = FontFamily(
        Font(
            resId = R.font.libre_baskerville_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
        Font(
            resId =  R.font.libre_baskerville_italic,
            weight = FontWeight.Normal,
            style = FontStyle.Italic
        ),
        Font(
            resId = R.font.libre_baskerville_bold,
            weight = FontWeight.Bold,
            style = FontStyle.Normal
        )
    )

val JetBrainsMonoFontFamily: FontFamily
    @Composable
    get() = FontFamily(
        Font( resId = R.font.jetbrainsmono_regular, weight = FontWeight.Normal),
        Font( resId = R.font.jetbrainsmono_medium, weight = FontWeight.Medium),
        Font( resId = R.font.jetbrainsmono_semibold, weight = FontWeight.SemiBold),
        Font( resId = R.font.jetbrainsmono_bold, weight = FontWeight.Bold)
    )