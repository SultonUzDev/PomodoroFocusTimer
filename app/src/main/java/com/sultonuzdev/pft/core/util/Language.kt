package com.sultonuzdev.pft.core.util




import java.util.Locale

/**
 * Enum representing supported languages in the app
 */
enum class Language(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    UZBEK("uz", "Uzbek", "O'zbekcha"),
    RUSSIAN("ru", "Russian", "Русский");

    companion object {
        fun fromCode(code: String): Language {
            return Language.entries.find { it.code == code } ?: ENGLISH
        }

        fun getSystemLanguage(): Language {
            val systemLanguage = Locale.getDefault().language
            return fromCode(systemLanguage)
        }

        fun getCurrentLocale(language: Language): Locale {
            return Locale(language.code)
        }
    }
}