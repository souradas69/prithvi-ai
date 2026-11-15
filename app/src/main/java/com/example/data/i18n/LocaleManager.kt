package com.example.data.i18n

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import com.example.data.auth.FirebaseCommunityAuthManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocaleManager private constructor(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("app_locale_prefs", Context.MODE_PRIVATE)
    private val authManager = FirebaseCommunityAuthManager.getInstance(context)

    private val _currentLocaleList = MutableStateFlow(getSavedLocaleList())
    val currentLocaleList: StateFlow<LocaleList> = _currentLocaleList.asStateFlow()

    private val _currentLanguageName = MutableStateFlow(getSavedLanguageName())
    val currentLanguageName: StateFlow<String> = _currentLanguageName.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            authManager.currentUser.collect { user ->
                if (user != null && user.primaryLanguage.isNotBlank() && user.primaryLanguage != _currentLanguageName.value) {
                    val lang = LanguageRegistry.findLanguage(user.primaryLanguage)
                    setLocale(user.primaryLanguage, lang.code, updateFirebase = false)
                }
            }
        }
    }

    private fun getSavedLocaleList(): LocaleList {
        val langCode = prefs.getString("language_code", "en") ?: "en"
        return LocaleList(Locale(langCode))
    }
    
    private fun getSavedLanguageName(): String {
        return prefs.getString("language_name", "English") ?: "English"
    }

    fun setLocale(languageName: String, languageCode: String, updateFirebase: Boolean = true) {
        prefs.edit()
            .putString("language_code", languageCode)
            .putString("language_name", languageName)
            .apply()

        _currentLocaleList.value = LocaleList(Locale(languageCode))
        _currentLanguageName.value = languageName

        if (updateFirebase) {
            val user = authManager.currentUser.value
            if (user != null) {
                authManager.updateUserProfile(
                    name = user.fullName,
                    phone = user.phoneNumber,
                    primaryLang = languageName,
                    prefLangs = user.preferredLanguages,
                    onSuccess = {},
                    onError = {}
                )
            }
        }
    }

    companion object {
        @Volatile
        private var instance: LocaleManager? = null

        fun getInstance(context: Context): LocaleManager {
            return instance ?: synchronized(this) {
                instance ?: LocaleManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
