package com.sultonuzdev.pft.core.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview


@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class DarkPreview

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
annotation class LightPreview


@LightPreview
@DarkPreview
annotation class AppPreview