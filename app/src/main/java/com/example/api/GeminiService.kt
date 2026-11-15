package com.example.api

import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await

/**
 * Calls the authenticated PRITHVI AI Firebase Functions backend.
 * Gemini credentials are kept server-side as a Functions secret and are never
 * packaged into the Android application.
 */
class GeminiService {

    suspend fun queryPrithviAi(
        prompt: String,
        systemContext: String = "",
        conversationHistory: List<Pair<String, String>> = emptyList(),
        imageBase64: String? = null,
        mimeType: String = "image/jpeg",
        language: String = "English"
    ): String {
        if (prompt.isBlank() && imageBase64.isNullOrBlank()) return "Please enter a question or select a crop image."
        return try {
            val functions = FirebaseFunctions.getInstance("asia-south1")
            val result = if (!imageBase64.isNullOrBlank()) {
                val safeMime = mimeType.takeIf { it in listOf("image/jpeg", "image/png", "image/webp") } ?: "image/jpeg"
                functions.getHttpsCallable("analyzeCrop").call(
                    mapOf("imageDataUrl" to "data:$safeMime;base64,$imageBase64", "mimeType" to safeMime)
                ).await()
            } else {
                val history = conversationHistory.takeLast(8).map { (role, text) ->
                    mapOf("role" to if (role.equals("user", true) || role.equals("farmer", true)) "user" else "assistant", "text" to text)
                }
                functions.getHttpsCallable("askPrithvi").call(
                    mapOf(
                        "message" to prompt,
                        "history" to history,
                        "context" to systemContext.take(2000),
                        "language" to "English"
                    )
                ).await()
            }
            val response = result.data as? Map<*, *>
            response?.get("text") as? String
                ?: "PRITHVI AI received an unexpected response. Please try again."
        } catch (error: Exception) {
            "PRITHVI AI is not connected yet. Please check your internet connection, sign in with your Firebase account, and confirm the PRITHVI AI backend is deployed."
        }
    }
}
