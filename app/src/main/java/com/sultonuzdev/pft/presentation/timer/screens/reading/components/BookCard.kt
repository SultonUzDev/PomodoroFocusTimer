package com.sultonuzdev.pft.presentation.timer.screens.reading.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.ui.theme.customTypography


/**
 * Book card with timer display
 */
@Composable
fun BookCard(
    time: String,
    pageStatus: String,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .aspectRatio(4/5f)
            .shadow(12.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.customColors.reading.bookStart,
                        MaterialTheme.customColors.reading.bookEnd
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Book spine line
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 25.dp).width(2.dp)
                .fillMaxHeight(0.7f)
                .background(Color.Black.copy(alpha = 0.2f))
        )

        // Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = time,
                style = MaterialTheme.customTypography.reading.timer,
                color = MaterialTheme.customColors.reading.timerText
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = pageStatus,
                style = MaterialTheme.customTypography.reading.pageStatus,
                color = MaterialTheme.customColors.reading.statLabel
            )
        }
    }
}
