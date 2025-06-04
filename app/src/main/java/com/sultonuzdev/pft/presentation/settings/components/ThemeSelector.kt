package com.sultonuzdev.pft.presentation.settings.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.PomodoroAppTheme
import com.sultonuzdev.pft.core.ui.theme.ThemeMode

@Preview
@Composable
private fun THemeSelecterPreview() {
    PomodoroAppTheme {
        ThemeSelector(
            modifier = Modifier,
            selectedThemeMode = ThemeMode.SYSTEM,
            onThemeModeSelected = {})
    }
}

@Composable
fun ThemeSelector(
    selectedThemeMode: ThemeMode,
    onThemeModeSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup()
    ) {
        Text(
            text = "Theme Mode",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        ThemeModeOption(
            icon = Icons.Default.PhoneAndroid,
            label = "System Default",
            description = "Follow system theme settings",
            selected = selectedThemeMode == ThemeMode.SYSTEM,
            onSelect = { onThemeModeSelected(ThemeMode.SYSTEM) }
        )

        ThemeModeOption(
            icon = Icons.Default.LightMode,
            label = "Light",
            description = "Always use light theme",
            selected = selectedThemeMode == ThemeMode.LIGHT,
            onSelect = { onThemeModeSelected(ThemeMode.LIGHT) }
        )

        ThemeModeOption(
            icon = Icons.Default.DarkMode,
            label = "Dark",
            description = "Always use dark theme",
            selected = selectedThemeMode == ThemeMode.DARK,
            onSelect = { onThemeModeSelected(ThemeMode.DARK) }
        )
    }
}

@Composable
private fun ThemeModeOption(
    icon: ImageVector,
    label: String,
    description: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .selectable(
                selected = selected,
                onClick = onSelect,
                role = Role.RadioButton
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null // null because we're handling the click in the row
        )

        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}