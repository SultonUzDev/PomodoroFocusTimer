package com.sultonuzdev.pft.presentation.timer.screens.reading.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.ui.theme.customTypography


/**
 * Header with title and subtitle
 */
@Composable
fun ReadingHeader(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Reading Session",
            style = MaterialTheme.customTypography.reading.title,
            color = MaterialTheme.customColors.reading.title
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Deep work through literature",
            style = MaterialTheme.customTypography.reading.subtitle,
            color = MaterialTheme.customColors.reading.subtitle
        )
    }
}