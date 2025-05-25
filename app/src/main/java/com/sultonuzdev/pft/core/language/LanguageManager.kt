package com.sultonuzdev.pft.core.language

import android.content.Context
import android.content.res.Configuration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sultonuzdev.pft.core.util.Language
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val LANGUAGE_KEY = stringPreferencesKey("selected_language")
    }

    suspend fun setLanguage(language: Language) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language.code
        }
    }

    fun getLanguageFlow(): Flow<Language> {
        return dataStore.data.map { preferences ->
            val languageCode = preferences[LANGUAGE_KEY] ?: Language.Companion.getSystemLanguage().code
            Language.Companion.fromCode(languageCode)
        }
    }

    suspend fun getCurrentLanguage(): Language {
        return try {
            val preferences = dataStore.data.first()
            val languageCode = preferences[LANGUAGE_KEY] ?: Language.Companion.getSystemLanguage().code
            Language.Companion.fromCode(languageCode)
        } catch (e: Exception) {
            Language.Companion.getSystemLanguage()
        }
    }

    /**
     * Create a context with the specified language
     */
    fun createLanguageContext(baseContext: Context, language: Language): Context {
        val locale = Locale(language.code)
        Locale.setDefault(locale)

        val config = Configuration(baseContext.resources.configuration)
        config.setLocale(locale)

        return baseContext.createConfigurationContext(config)
    }

    /**
     * Apply language to the application context
     */
    fun applyLanguage(context: Context, language: Language) {
        val locale = Locale(language.code)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        @Suppress("DEPRECATION")
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}