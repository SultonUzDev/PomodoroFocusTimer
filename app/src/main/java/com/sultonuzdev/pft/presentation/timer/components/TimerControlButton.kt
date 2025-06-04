package com.sultonuzdev.pft.presentation.timer.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.PomodoroAppTheme

/**
 * Control button for the timer (play, pause, stop, skip)
 */

@Preview
@Composable
private fun TimerTonalButtonPreview() {
    PomodoroAppTheme {
        TimerControlButton(
            modifier = Modifier.width(200.dp),
            onClick = {},
            icon = Icons.Filled.PlayArrow,
            contentDescription = "Start Timer",
            text = "Start"
        )
    }
}

@Composable
fun TimerControlButton(
    onClick: () -> Unit,
    text: String,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tint: Color = Color.White,
    containerColor: Color = MaterialTheme.colorScheme.primary
) {
    ElevatedButton(
        modifier = modifier
            .padding(
                8.dp
            )
            .heightIn(min = 50.dp, max = 60.dp),
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.elevatedButtonColors(containerColor = containerColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription, tint = tint
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = tint
            )
        }
    }
}