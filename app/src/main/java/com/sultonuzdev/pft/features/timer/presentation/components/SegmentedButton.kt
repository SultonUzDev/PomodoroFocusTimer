// com.sultonuzdev.pft.core.ui.segmentedbutton.SegmentedButton.kt
package com.sultonuzdev.pft.features.timer.presentation.components

import android.R.attr.label
import android.R.attr.onClick
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Custom implementation of a segmented button item for the SegmentedButtonRow
 */
@Composable
fun SegmentedButton(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    label: @Composable () -> Unit,
) {
    val backgroundColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(
                enabled = enabled,
                onClick = onClick,
                role = Role.Button
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        val textStyle = MaterialTheme.typography.labelLarge.copy(color = contentColor)
        ProvideTextStyle(textStyle, content = label)
    }
}

/**
 * Simple provider of text style for the content
 */
@Composable
private fun ProvideTextStyle(
    style: TextStyle,
    content: @Composable () -> Unit
) {
    val mergedStyle = MaterialTheme.typography.labelLarge.merge(style)
    ProvideTextStyle(mergedStyle, content)
}

/**
 * Replacement for SingleChoiceSegmentedButtonRow that uses custom SegmentedButton implementation
 */
@Composable
fun SegmentedButtonRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
    }
}

/**
 * Convenience function for creating a text segmented button
 */
@Composable
fun TextSegmentedButton(
    text: String,
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    SegmentedButton(
        selected = selected,
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxHeight(),
        label = {
            Text(
                text = text,
                textAlign = TextAlign.Center
            )
        }
    )
}