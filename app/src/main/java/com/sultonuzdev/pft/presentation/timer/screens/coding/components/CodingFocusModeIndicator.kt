package com.sultonuzdev.pft.presentation.timer.screens.coding.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.ui.theme.customTypography

/**
 * Coding focus mode indicator
 */
@Composable
fun CodingFocusModeIndicator(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.customColors.coding.terminalBg
        ),
        border = BorderStroke(1.dp, MaterialTheme.customColors.coding.primary.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "💻",
                style = MaterialTheme.customTypography.coding.terminal
            )
            Column {
                Text(
                    text = "// DEEP_FOCUS_MODE = true",
                    style = MaterialTheme.customTypography.coding.comment,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.customColors.coding.primary
                )
                Text(
                    text = "// Eliminate distractions, write clean code",
                    style = MaterialTheme.customTypography.coding.comment,
                    color = MaterialTheme.customColors.coding.text.copy(alpha = 0.7f)
                )
            }
        }
    }
}
