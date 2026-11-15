package com.example.data.i18n

data class SupportedLanguage(
    val code: String,
    val nativeName: String,
    val englishName: String,
    val isRtl: Boolean = false,
    val speechLocaleTag: String = "en-IN"
)

object LanguageRegistry {
    val SUPPORTED_LANGUAGES = listOf(
        SupportedLanguage(
            code = "en",
            nativeName = "English",
            englishName = "English",
            isRtl = false,
            speechLocaleTag = "en-IN"
        ),
        SupportedLanguage(
            code = "bn",
            nativeName = "বাংলা",
            englishName = "Bengali",
            isRtl = false,
            speechLocaleTag = "bn-IN"
        ),
        SupportedLanguage(
            code = "hi",
            nativeName = "हिन्दी",
            englishName = "Hindi",
            isRtl = false,
            speechLocaleTag = "hi-IN"
        ),
        SupportedLanguage(
            code = "ur",
            nativeName = "اردو",
            englishName = "Urdu",
            isRtl = true,
            speechLocaleTag = "ur-IN"
        ),
        SupportedLanguage(
            code = "mr",
            nativeName = "मराठी",
            englishName = "Marathi",
            isRtl = false,
            speechLocaleTag = "mr-IN"
        ),
        SupportedLanguage(
            code = "as",
            nativeName = "অসমীয়া",
            englishName = "Assamese",
            isRtl = false,
            speechLocaleTag = "as-IN"
        ),
        SupportedLanguage(
            code = "ta",
            nativeName = "தமிழ்",
            englishName = "Tamil",
            isRtl = false,
            speechLocaleTag = "ta-IN"
        ),
        SupportedLanguage(
            code = "te",
            nativeName = "తెలుగు",
            englishName = "Telugu",
            isRtl = false,
            speechLocaleTag = "te-IN"
        ),
        SupportedLanguage(
            code = "gu",
            nativeName = "ગુજરાતી",
            englishName = "Gujarati",
            isRtl = false,
            speechLocaleTag = "gu-IN"
        ),
        SupportedLanguage(
            code = "pa",
            nativeName = "ਪੰਜਾਬੀ",
            englishName = "Punjabi",
            isRtl = false,
            speechLocaleTag = "pa-IN"
        ),
        SupportedLanguage(
            code = "or",
            nativeName = "ଓଡ଼ିଆ",
            englishName = "Odia",
            isRtl = false,
            speechLocaleTag = "or-IN"
        ),
        SupportedLanguage(
            code = "ml",
            nativeName = "മലയാളം",
            englishName = "Malayalam",
            isRtl = false,
            speechLocaleTag = "ml-IN"
        ),
        SupportedLanguage(
            code = "kn",
            nativeName = "ಕನ್ನಡ",
            englishName = "Kannada",
            isRtl = false,
            speechLocaleTag = "kn-IN"
        )
    )

    fun findLanguage(query: String?): SupportedLanguage {
        if (query.isNullOrBlank()) return SUPPORTED_LANGUAGES[0]
        val clean = query.trim().lowercase()
        return SUPPORTED_LANGUAGES.firstOrNull { lang ->
            lang.code.lowercase() == clean ||
            lang.englishName.lowercase() == clean ||
            lang.nativeName.lowercase() == clean ||
            clean.contains(lang.code.lowercase()) ||
            clean.contains(lang.englishName.lowercase()) ||
            clean.contains(lang.nativeName.lowercase())
        } ?: SUPPORTED_LANGUAGES[0]
    }

    fun isRtl(query: String?): Boolean {
        return findLanguage(query).isRtl
    }

    fun getDisplayName(lang: SupportedLanguage): String {
        return if (lang.code == "en") {
            "English"
        } else {
            "${lang.nativeName} (${lang.englishName})"
        }
    }
}
