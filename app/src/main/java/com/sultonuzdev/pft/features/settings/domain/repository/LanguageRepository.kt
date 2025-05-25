package com.sultonuzdev.pft.features.settings.domain.repository

import com.sultonuzdev.pft.core.util.Language
import kotlinx.coroutines.flow.Flow

interface LanguageRepository {
    suspend fun setLanguage(language: Language)
    fun getLanguageFlow(): Flow<Language>
    suspend fun getCurrentLanguage(): Language
}