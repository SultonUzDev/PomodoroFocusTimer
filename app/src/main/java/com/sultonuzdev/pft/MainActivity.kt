package com.sultonuzdev.pft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.sultonuzdev.pft.core.ui.PomodoroRootApp
import com.sultonuzdev.pft.domain.repository.ThemePreferencesRepository
import com.sultonuzdev.pft.presentation.navigation.SetUpNavGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themePreferencesRepository: ThemePreferencesRepository



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            PomodoroRootApp(themePreferencesRepository = themePreferencesRepository) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()

                    ,
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    SetUpNavGraph(navController = navController)
                }
            }

        }
    }
}