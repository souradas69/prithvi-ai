package com.example.data.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.LayoutDirection

val LocalAppLanguage = compositionLocalOf { "English" }

object PrithviI18n {

    private val dictionaries: Map<String, Map<String, String>> = mapOf(
        "en" to translationsEn,
        "bn" to translationsBn,
        "hi" to translationsHi,
        "ur" to translationsUr,
        "mr" to translationsMr,
        "as" to translationsAs,
        "ta" to translationsTa,
        "te" to translationsTe,
        "gu" to translationsGu,
        "pa" to translationsPa,
        "or" to translationsOr,
        "ml" to translationsMl,
        "kn" to translationsKn
    )

    fun t(key: String, languageQuery: String?): String {
        return translationsEn[key] ?: key
    }

    fun isRtl(languageQuery: String?): Boolean {
        return false
    }

    fun getLayoutDirection(languageQuery: String?): LayoutDirection {
        return if (isRtl(languageQuery)) LayoutDirection.Rtl else LayoutDirection.Ltr
    }
}

@Composable
fun tr(key: String): String {
    val lang = LocalAppLanguage.current
    return PrithviI18n.t(key, lang)
}
