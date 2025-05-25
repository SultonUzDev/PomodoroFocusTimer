package com.sultonuzdev.pft.features.settings.data.repository

import com.sultonuzdev.pft.core.language.LanguageManager
import com.sultonuzdev.pft.core.util.Language
import com.sultonuzdev.pft.features.settings.domain.repository.LanguageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageRepositoryImpl @Inject constructor(
    private val languageManager: LanguageManager
) : LanguageRepository {

    override suspend fun setLanguage(language: Language) {
        languageManager.setLanguage(language)
    }

    override fun getLanguageFlow(): Flow<Language> {
        return languageManager.getLanguageFlow()
    }

    override suspend fun getCurrentLanguage(): Language {
        return languageManager.getCurrentLanguage()
    }
}