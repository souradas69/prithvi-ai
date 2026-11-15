package com.example.data.voice

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

data class IndianVoiceLanguage(
    val code: String,
    val tag: String,
    val englishName: String,
    val nativeName: String,
    val voiceHint: String,
    val sampleQueries: List<String>
)

object IndianVoiceRecognitionManager {

    val supportedLanguages = listOf(
        IndianVoiceLanguage(
            code = "en",
            tag = "en-IN",
            englishName = "English",
            nativeName = "English",
            voiceHint = "Ask your crop question in English...",
            sampleQueries = listOf(
                "How do I treat leaf curl disease in tomato?",
                "Should I irrigate my crop today based on weather?",
                "What is the recommended fertilizer schedule?",
                "How to prepare organic neem pesticide spray?"
            )
        )
    )

    fun getLanguageByDisplayName(name: String): IndianVoiceLanguage {
        return supportedLanguages.first()
    }

    fun isSpeechRecognitionAvailable(context: Context): Boolean {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            return true
        }
        val pm = context.packageManager
        val activities = pm.queryIntentActivities(
            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),
            PackageManager.MATCH_DEFAULT_ONLY
        )
        return activities.isNotEmpty()
    }

    fun createSpeechIntent(
        language: IndianVoiceLanguage,
        cropName: String = "Tomato"
    ): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, language.tag)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language.tag)
            putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, language.tag)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your $cropName crop query...")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }
    }
}
