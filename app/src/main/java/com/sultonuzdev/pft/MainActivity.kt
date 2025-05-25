package com.sultonuzdev.pft

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.sultonuzdev.pft.core.language.LanguageManager
import com.sultonuzdev.pft.core.ui.PomodoroRootApp
import com.sultonuzdev.pft.features.settings.domain.repository.ThemePreferencesRepository
import com.sultonuzdev.pft.navigation.SetUpNavGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themePreferencesRepository: ThemePreferencesRepository

    @Inject
    lateinit var languageManager: LanguageManager

    override fun attachBaseContext(newBase: Context?) {
        if (newBase == null) {
            super.attachBaseContext(newBase)
            return
        }

        val languageContext = runBlocking {
            try {
                val currentLanguage = languageManager.getCurrentLanguage()
                languageManager.createLanguageContext(newBase, currentLanguage)
            } catch (e: Exception) {
                // If there's any error, fall back to the original context
                newBase
            }
        }

        super.attachBaseContext(languageContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            PomodoroRootApp(themePreferencesRepository = themePreferencesRepository) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    SetUpNavGraph(navController = navController)
                }
            }

        }
    }
}