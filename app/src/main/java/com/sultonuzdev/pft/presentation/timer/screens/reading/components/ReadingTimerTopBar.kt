package com.sultonuzdev.pft.presentation.timer.screens.reading.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sultonuzdev.pft.core.ui.theme.customColors


/**
 * Reading Timer TopBar component
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingTimerTopBar(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = {
//            Text(
//                text = "Reading Timer",
//                style = MaterialTheme.typography.titleLarge,
//                fontWeight = FontWeight.Bold,
//                color = MaterialTheme.customColors.reading.text
//            )

        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigate back",
                    tint = MaterialTheme.customColors.reading.text
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.customColors.reading.backgroundStart
        ),
        modifier = modifier
    )
}
