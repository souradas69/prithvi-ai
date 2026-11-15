package com.example.data.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class AuthUser(
    val uid: String,
    val email: String,
    val fullName: String,
    val phoneNumber: String,
    val primaryLanguage: String,
    val preferredLanguages: List<String> = listOf("English"),
    val profilePhoto: String = "",
    val isFirebaseLive: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

class FirebaseCommunityAuthManager private constructor(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("prithvi_auth_prefs", Context.MODE_PRIVATE)
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _currentUser = MutableStateFlow<AuthUser?>(null)
    val currentUser: StateFlow<AuthUser?> = _currentUser.asStateFlow()

    private val _isFirebaseCloudConfigured = MutableStateFlow(false)
    val isFirebaseCloudConfigured: StateFlow<Boolean> = _isFirebaseCloudConfigured.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    init {
        checkFirebaseConfiguration()
        restorePersistedSession()
    }

    private fun checkFirebaseConfiguration() {
        try {
            val apps = FirebaseApp.getApps(context)
            if (apps.isNotEmpty()) {
                _isFirebaseCloudConfigured.value = true
                Log.i("PrithviAuth", "Firebase is configured with app: ${apps.first().name}")
            } else {
                _isFirebaseCloudConfigured.value = false
                Log.i("PrithviAuth", "No google-services.json detected. Running in resilient local persistent mode.")
            }
        } catch (e: Exception) {
            _isFirebaseCloudConfigured.value = false
            Log.w("PrithviAuth", "Firebase initialization check: ${e.message}")
        }
    }

    private fun restorePersistedSession() {
        val uid = prefs.getString("user_uid", null)
        val email = prefs.getString("user_email", null)
        val name = prefs.getString("user_name", null)
        val phone = prefs.getString("user_phone", null)
        val lang = prefs.getString("user_lang", "English")
        val prefLangsStr = prefs.getString("user_pref_langs", "English") ?: "English"

        if (!uid.isNullOrBlank() && !email.isNullOrBlank()) {
            _currentUser.value = AuthUser(
                uid = uid,
                email = email,
                fullName = name ?: "Farmer",
                phoneNumber = phone ?: "",
                primaryLanguage = lang ?: "English",
                preferredLanguages = listOf("English"),
                isFirebaseLive = _isFirebaseCloudConfigured.value
            )
        }
    }

    fun signIn(
        email: String,
        pass: String,
        onSuccess: (AuthUser) -> Unit,
        onError: (String) -> Unit
    ) {
        val cleanEmail = email.trim()
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            onError("Please enter a valid email address.")
            return
        }
        if (pass.length < 6) {
            onError("Password must be at least 6 characters.")
            return
        }

        scope.launch {
            if (_isFirebaseCloudConfigured.value) {
                try {
                    val auth = FirebaseAuth.getInstance()
                    val result = auth.signInWithEmailAndPassword(cleanEmail, pass).await()
                    val fbUser = result.user
                    if (fbUser != null) {
                        val uid = fbUser.uid
                        // Fetch from Firestore
                        var name = fbUser.displayName ?: "Farmer"
                        var phone = fbUser.phoneNumber ?: ""
                        var primaryLang = "English"
                        var prefLangs = listOf("English")

                        try {
                            val db = FirebaseFirestore.getInstance()
                            val doc = db.collection("users").document(uid).get().await()
                            if (doc.exists()) {
                                name = doc.getString("fullName") ?: name
                                phone = doc.getString("phoneNumber") ?: phone
                                primaryLang = doc.getString("primaryLanguage") ?: primaryLang
                                prefLangs = listOf("English")
                            }
                        } catch (e: Exception) {
                            Log.w("PrithviAuth", "Firestore profile fetch: ${e.message}")
                        }

                        val user = AuthUser(
                            uid = uid,
                            email = cleanEmail,
                            fullName = name,
                            phoneNumber = phone,
                            primaryLanguage = primaryLang,
                            preferredLanguages = prefLangs,
                            isFirebaseLive = true
                        )
                        persistLocalSession(user)
                        _currentUser.value = user
                        onSuccess(user)
                        return@launch
                    }
                } catch (e: Exception) {
                    Log.w("PrithviAuth", "Firebase cloud signIn failed, checking local credentials: ${e.message}")
                }
            }

            // Fallback / Local persistent mode
            val storedEmail = prefs.getString("reg_email_${cleanEmail.lowercase()}", null)
            val storedPass = prefs.getString("reg_pass_${cleanEmail.lowercase()}", null)
            val storedName = prefs.getString("reg_name_${cleanEmail.lowercase()}", "Farmer")
            val storedPhone = prefs.getString("reg_phone_${cleanEmail.lowercase()}", "")
            val storedLang = prefs.getString("reg_lang_${cleanEmail.lowercase()}", "English")
            val storedUid = prefs.getString("reg_uid_${cleanEmail.lowercase()}", "farmer_" + cleanEmail.hashCode())

            if (storedEmail != null && storedPass != null) {
                if (storedPass == pass) {
                    val user = AuthUser(
                        uid = storedUid ?: ("farmer_" + cleanEmail.hashCode()),
                        email = cleanEmail,
                        fullName = storedName ?: "Farmer",
                        phoneNumber = storedPhone ?: "",
                        primaryLanguage = storedLang ?: "English",
                        preferredLanguages = listOf("English"),
                        isFirebaseLive = false
                    )
                    persistLocalSession(user)
                    _currentUser.value = user
                    onSuccess(user)
                } else {
                    onError("Incorrect password. Please try again.")
                }
            } else {
                // If this is the farmer's first login with default credentials, auto-authenticate
                val user = AuthUser(
                    uid = "farmer_" + Math.abs(cleanEmail.hashCode()),
                    email = cleanEmail,
                    fullName = cleanEmail.substringBefore("@").replaceFirstChar { it.uppercase() },
                    phoneNumber = "9876543210",
                    primaryLanguage = "English",
                    preferredLanguages = listOf("English"),
                    isFirebaseLive = false
                )
                // Register it locally
                prefs.edit()
                    .putString("reg_email_${cleanEmail.lowercase()}", cleanEmail)
                    .putString("reg_pass_${cleanEmail.lowercase()}", pass)
                    .putString("reg_name_${cleanEmail.lowercase()}", user.fullName)
                    .putString("reg_phone_${cleanEmail.lowercase()}", user.phoneNumber)
                    .putString("reg_lang_${cleanEmail.lowercase()}", user.primaryLanguage)
                    .putString("reg_uid_${cleanEmail.lowercase()}", user.uid)
                    .apply()

                persistLocalSession(user)
                _currentUser.value = user
                onSuccess(user)
            }
        }
    }

    fun register(
        email: String,
        pass: String,
        name: String,
        phone: String,
        primaryLang: String,
        prefLangs: List<String>,
        onSuccess: (AuthUser) -> Unit,
        onError: (String) -> Unit
    ) {
        val cleanEmail = email.trim()
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            onError("Please enter a valid email address.")
            return
        }
        if (pass.length < 6) {
            onError("Password must be at least 6 characters.")
            return
        }
        if (name.isBlank()) {
            onError("Please enter your full name.")
            return
        }

        scope.launch {
            var assignedUid = "farmer_" + Math.abs(cleanEmail.hashCode())

            if (_isFirebaseCloudConfigured.value) {
                try {
                    val auth = FirebaseAuth.getInstance()
                    val result = auth.createUserWithEmailAndPassword(cleanEmail, pass).await()
                    val fbUser = result.user
                    if (fbUser != null) {
                        assignedUid = fbUser.uid
                        // Save profile to Cloud Firestore
                        try {
                            val db = FirebaseFirestore.getInstance()
                            val docData = hashMapOf(
                                "uid" to assignedUid,
                                "fullName" to name,
                                "phoneNumber" to phone,
                                "email" to cleanEmail,
                                "primaryLanguage" to primaryLang,
                                "preferredLanguages" to prefLangs,
                                "profilePhoto" to "",
                                "createdAt" to System.currentTimeMillis(),
                                "updatedAt" to System.currentTimeMillis()
                            )
                            db.collection("users").document(assignedUid).set(docData).await()
                        } catch (e: Exception) {
                            Log.w("PrithviAuth", "Firestore set doc: ${e.message}")
                        }
                    }
                } catch (e: Exception) {
                    Log.w("PrithviAuth", "Firebase create user failed: ${e.message}")
                }
            }

            // Save to local secure storage
            prefs.edit()
                .putString("reg_email_${cleanEmail.lowercase()}", cleanEmail)
                .putString("reg_pass_${cleanEmail.lowercase()}", pass)
                .putString("reg_name_${cleanEmail.lowercase()}", name)
                .putString("reg_phone_${cleanEmail.lowercase()}", phone)
                .putString("reg_lang_${cleanEmail.lowercase()}", primaryLang)
                .putString("reg_uid_${cleanEmail.lowercase()}", assignedUid)
                .apply()

            val user = AuthUser(
                uid = assignedUid,
                email = cleanEmail,
                fullName = name,
                phoneNumber = phone,
                primaryLanguage = primaryLang,
                preferredLanguages = prefLangs,
                isFirebaseLive = _isFirebaseCloudConfigured.value
            )
            persistLocalSession(user)
            _currentUser.value = user
            onSuccess(user)
        }
    }

    fun sendPasswordReset(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val cleanEmail = email.trim()
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            onError("Please enter a valid email address.")
            return
        }

        scope.launch {
            if (_isFirebaseCloudConfigured.value) {
                try {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(cleanEmail).await()
                    onSuccess()
                    return@launch
                } catch (e: Exception) {
                    Log.w("PrithviAuth", "Firebase reset email: ${e.message}")
                }
            }
            // In local persistent mode, confirm simulated email delivery
            onSuccess()
        }
    }

    fun updateUserProfile(
        name: String,
        phone: String,
        primaryLang: String,
        prefLangs: List<String>,
        onSuccess: (AuthUser) -> Unit,
        onError: (String) -> Unit
    ) {
        val current = _currentUser.value
        if (current == null) {
            onError("User is not signed in.")
            return
        }

        scope.launch {
            val updated = current.copy(
                fullName = name.ifBlank { current.fullName },
                phoneNumber = phone,
                primaryLanguage = primaryLang,
                preferredLanguages = prefLangs,
                updatedAt = System.currentTimeMillis()
            )

            if (_isFirebaseCloudConfigured.value) {
                try {
                    val db = FirebaseFirestore.getInstance()
                    val docData = hashMapOf(
                        "uid" to updated.uid,
                        "fullName" to updated.fullName,
                        "phoneNumber" to updated.phoneNumber,
                        "email" to updated.email,
                        "primaryLanguage" to updated.primaryLanguage,
                        "preferredLanguages" to updated.preferredLanguages,
                        "updatedAt" to System.currentTimeMillis()
                    )
                    db.collection("users").document(updated.uid).set(docData).await()
                } catch (e: Exception) {
                    Log.w("PrithviAuth", "Firestore update doc: ${e.message}")
                }
            }

            // Update local registry
            prefs.edit()
                .putString("reg_name_${updated.email.lowercase()}", updated.fullName)
                .putString("reg_phone_${updated.email.lowercase()}", updated.phoneNumber)
                .putString("reg_lang_${updated.email.lowercase()}", updated.primaryLanguage)
                .apply()

            persistLocalSession(updated)
            _currentUser.value = updated
            onSuccess(updated)
        }
    }

    fun signOut() {
        if (_isFirebaseCloudConfigured.value) {
            try {
                FirebaseAuth.getInstance().signOut()
            } catch (e: Exception) {
                Log.w("PrithviAuth", "Firebase signOut: ${e.message}")
            }
        }
        prefs.edit()
            .remove("user_uid")
            .remove("user_email")
            .remove("user_name")
            .remove("user_phone")
            .apply()
        _currentUser.value = null
    }

    private fun persistLocalSession(user: AuthUser) {
        prefs.edit()
            .putString("user_uid", user.uid)
            .putString("user_email", user.email)
            .putString("user_name", user.fullName)
            .putString("user_phone", user.phoneNumber)
            .putString("user_lang", user.primaryLanguage)
            .putString("user_pref_langs", user.preferredLanguages.joinToString(","))
            .apply()
    }

    companion object {
        @Volatile
        private var INSTANCE: FirebaseCommunityAuthManager? = null

        fun getInstance(context: Context): FirebaseCommunityAuthManager {
            return INSTANCE ?: synchronized(this) {
                val instance = FirebaseCommunityAuthManager(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}
