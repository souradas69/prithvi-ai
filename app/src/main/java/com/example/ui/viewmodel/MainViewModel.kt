package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.util.Base64
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.GeminiService
import com.example.data.AppDatabase
import com.example.data.auth.AuthUser
import com.example.data.auth.FirebaseCommunityAuthManager
import com.example.data.entity.CommunityPostEntity
import com.example.data.entity.CropScanEntity
import com.example.data.entity.ExpenseEntity
import com.example.data.entity.FarmEntity
import com.example.data.entity.FarmerProfileEntity
import com.example.data.entity.FieldEntity
import com.example.data.entity.HarvestEntity
import com.example.data.entity.SoilHealthEntity
import com.example.data.repository.FarmerRepository
import com.example.data.repository.GovernmentSchemeRepository
import com.example.data.repository.MarketRepository
import com.example.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

data class ChatMessage(
    val id: Long = System.currentTimeMillis(),
    val sender: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isUser: Boolean = false,
    val attachedBitmap: Bitmap? = null
)

data class DirectMessage(
    val id: Long = System.currentTimeMillis(),
    val senderName: String,
    val message: String,
    val timestamp: String = "Just now",
    val isUser: Boolean = false
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = FarmerRepository(db)
    private val geminiService = GeminiService()

    val farmerProfile: StateFlow<FarmerProfileEntity?> = repository.farmerProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val farms: StateFlow<List<FarmEntity>> = repository.allFarms
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val fields: StateFlow<List<FieldEntity>> = repository.allFields
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val scans: StateFlow<List<CropScanEntity>> = repository.allScans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expenses: StateFlow<List<ExpenseEntity>> = repository.allExpenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val harvests: StateFlow<List<HarvestEntity>> = repository.allHarvests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val soilRecords: StateFlow<List<SoilHealthEntity>> = repository.allSoilRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val communityPosts: StateFlow<List<CommunityPostEntity>> = repository.allCommunityPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedFieldId = MutableStateFlow<Long?>(null)
    val selectedFieldId = _selectedFieldId.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking = _isAiThinking.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = "PRITHVI AI",
                message = "Hello! I am PRITHVI AI, your farming assistant. Ask me about crop care, plant symptoms, soil management or farm planning.",
                isUser = false
            )
        )
    )
    val chatMessages = _chatMessages.asStateFlow()

    val authManager = FirebaseCommunityAuthManager.getInstance(application)
    val authUser: StateFlow<AuthUser?> = authManager.currentUser
    val isFirebaseCloudConfigured: StateFlow<Boolean> = authManager.isFirebaseCloudConfigured
    
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _isSimpleMode = MutableStateFlow(false)
    val isSimpleMode: StateFlow<Boolean> = _isSimpleMode.asStateFlow()

    private val _isVoiceMode = MutableStateFlow(false)
    val isVoiceMode: StateFlow<Boolean> = _isVoiceMode.asStateFlow()

    private val _weatherData = MutableStateFlow(
        WeatherRepository.getWeatherDataForLocation("West Bengal", "Hooghly", "Singur", "Tomato")
    )
    val weatherData: StateFlow<com.example.data.repository.WeatherData> = _weatherData.asStateFlow()

    private val _isWeatherLoading = MutableStateFlow(false)
    val isWeatherLoading: StateFlow<Boolean> = _isWeatherLoading.asStateFlow()

    private val _directMessages = MutableStateFlow<Map<String, List<DirectMessage>>>(emptyMap())
    val directMessages: StateFlow<Map<String, List<DirectMessage>>> = _directMessages.asStateFlow()

    init {
        viewModelScope.launch {
            refreshWeatherData()
            // Observe and restore persisted language preference
            repository.farmerProfile.collect { profile ->
                if (profile != null) {
                    // Language is disabled, forcing English
                }
            }
        }
        viewModelScope.launch {
            authManager.currentUser.collect { user ->
                if (user != null) {
                    val curr = farmerProfile.value
                    if (curr != null) {
                        repository.saveProfile(
                            curr.copy(
                                name = user.fullName.ifBlank { curr.name },
                                phone = user.phoneNumber.ifBlank { curr.phone },
                                language = "English",
                                email = user.email,
                                uid = user.uid,
                                preferredLanguages = "English",
                                isAuthenticated = true
                            )
                        )
                    }
                }
            }
        }
    }

    fun signIn(email: String, pass: String, onSuccess: (AuthUser) -> Unit, onError: (String) -> Unit) {
        authManager.signIn(email, pass, onSuccess = { user ->
            onSuccess(user)
        }, onError = onError)
    }

    fun register(
        email: String,
        pass: String,
        name: String,
        phone: String,
        lang: String,
        prefLangs: List<String>,
        onSuccess: (AuthUser) -> Unit,
        onError: (String) -> Unit
    ) {
        authManager.register(email, pass, name, phone, "English", listOf("English"), onSuccess = { user ->
            onSuccess(user)
        }, onError = onError)
    }

    fun sendPasswordReset(email: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        authManager.sendPasswordReset(email, onSuccess, onError)
    }

    fun signOut() {
        authManager.signOut()
    }

    fun refreshWeatherData(
        state: String? = null,
        district: String? = null,
        village: String? = null,
        cropName: String? = null
    ) {
        viewModelScope.launch {
            _isWeatherLoading.value = true
            val profile = farmerProfile.value
            val st = state ?: profile?.state ?: "West Bengal"
            val dist = district ?: profile?.district ?: "Hooghly"
            val vil = village ?: profile?.village ?: "Singur"
            val activeField = fields.value.firstOrNull()
            val crop = cropName ?: activeField?.cropName ?: "Tomato"

            val data = WeatherRepository.fetchRealtimeWeatherData(st, dist, vil, crop)
            _weatherData.value = data
            _isWeatherLoading.value = false
        }
    }

    fun toggleSimpleMode() {
        _isSimpleMode.value = !_isSimpleMode.value
    }

    fun toggleVoiceMode() {
        _isVoiceMode.value = !_isVoiceMode.value
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
    }

    fun setSelectedLanguage(lang: String) {
        // Disabled
    }

    fun selectField(fieldId: Long) {
        _selectedFieldId.value = fieldId
    }

    fun saveFarmerProfile(
        name: String,
        phone: String,
        language: String,
        state: String,
        district: String,
        village: String,
        goals: String
    ) {
        viewModelScope.launch {
            val entity = FarmerProfileEntity(
                id = 1,
                name = name,
                phone = phone,
                language = language,
                state = state,
                district = district,
                village = village,
                onboardingCompleted = true,
                goals = goals
            )
            repository.saveProfile(entity)
        }
    }

    fun updateAccountAndFieldDetails(
        name: String,
        phone: String,
        language: String,
        state: String,
        district: String,
        village: String,
        cropName: String,
        cropVariety: String,
        growthStage: String,
        area: Double,
        soilType: String,
        irrigationMethod: String,
        email: String = "",
        prefLangs: List<String> = listOf("English")
    ) {
        viewModelScope.launch {
            val curr = farmerProfile.value
            
            val entity = FarmerProfileEntity(
                id = 1,
                name = name,
                phone = phone,
                language = language,
                state = state,
                district = district,
                village = village,
                onboardingCompleted = true,
                goals = curr?.goals ?: "High Yield, Pest Alert",
                email = email.ifBlank { curr?.email ?: "" },
                uid = curr?.uid ?: "",
                preferredLanguages = prefLangs.joinToString(","),
                isAuthenticated = curr?.isAuthenticated ?: (authUser.value != null),
                updatedAt = System.currentTimeMillis()
            )
            repository.saveProfile(entity)

            // Sync with Firebase Firestore profile
            val user = authUser.value
            if (user != null) {
                authManager.updateUserProfile(
                    name = name,
                    phone = phone,
                    primaryLang = language,
                    prefLangs = prefLangs,
                    onSuccess = {},
                    onError = {}
                )
            }

            // Update or add active field
            val existingFields = fields.value
            val activeField = existingFields.firstOrNull()
            val epochDays = System.currentTimeMillis() / (1000 * 60 * 60 * 24)

            if (activeField != null) {
                val updatedField = activeField.copy(
                    fieldName = "$cropName Field",
                    area = area,
                    cropName = cropName,
                    cropVariety = cropVariety,
                    growthStage = growthStage,
                    soilInfo = soilType,
                    irrigationMethod = irrigationMethod
                )
                repository.updateField(updatedField)
            } else {
                repository.addField(
                    FieldEntity(
                        farmId = 1L,
                        fieldName = "$cropName Field",
                        area = area,
                        cropName = cropName,
                        cropVariety = cropVariety,
                        plantingDateEpochDays = epochDays - 35,
                        growthStage = growthStage,
                        soilInfo = soilType,
                        irrigationMethod = irrigationMethod,
                        waterSource = "Borewell",
                        lastWateredEpochDays = epochDays - 1,
                        notes = "Primary field updated from Account Centre"
                    )
                )
            }
        }
    }

    fun logoutToSwitchAccount() {
        viewModelScope.launch {
            val curr = farmerProfile.value
            if (curr != null) {
                repository.saveProfile(curr.copy(onboardingCompleted = false))
            } else {
                repository.saveProfile(
                    FarmerProfileEntity(
                        id = 1,
                        name = "Ramesh Kumar",
                        phone = "9876543210",
                        language = "English",
                        state = "West Bengal",
                        district = "Hooghly",
                        village = "Singur",
                        onboardingCompleted = false
                    )
                )
            }
        }
    }

    fun addFarm(
        farmName: String,
        location: String,
        area: Double,
        unit: String,
        soilType: String,
        irrigationMethod: String,
        waterSource: String,
        farmingMethod: String,
        season: String
    ) {
        viewModelScope.launch {
            repository.addFarm(
                FarmEntity(
                    farmName = farmName,
                    location = location,
                    area = area,
                    unit = unit,
                    soilType = soilType,
                    irrigationMethod = irrigationMethod,
                    waterSource = waterSource,
                    farmingMethod = farmingMethod,
                    season = season
                )
            )
        }
    }

    fun addField(
        farmId: Long,
        fieldName: String,
        area: Double,
        cropName: String,
        cropVariety: String,
        growthStage: String,
        soilInfo: String,
        irrigationMethod: String,
        waterSource: String,
        notes: String
    ) {
        viewModelScope.launch {
            val epochDays = System.currentTimeMillis() / (1000 * 60 * 60 * 24)
            repository.addField(
                FieldEntity(
                    farmId = farmId,
                    fieldName = fieldName,
                    area = area,
                    cropName = cropName,
                    cropVariety = cropVariety,
                    plantingDateEpochDays = epochDays - 35, // default 35 days ago for vegetative
                    growthStage = growthStage,
                    soilInfo = soilInfo,
                    irrigationMethod = irrigationMethod,
                    waterSource = waterSource,
                    lastWateredEpochDays = epochDays - 2,
                    notes = notes
                )
            )
        }
    }

    fun markFieldWatered(fieldId: Long) {
        viewModelScope.launch {
            val todayEpoch = System.currentTimeMillis() / (1000 * 60 * 60 * 24)
            repository.updateFieldLastWatered(fieldId, todayEpoch)
        }
    }

    fun sendMessageToPrithvi(userMessage: String, bitmap: Bitmap? = null) {
        if (userMessage.isBlank() && bitmap == null) return

        val textToSend = if (userMessage.isBlank()) "Attached image for PRITHVI crop analysis" else userMessage
        val userMsgObj = ChatMessage(sender = "Farmer", message = textToSend, isUser = true, attachedBitmap = bitmap)
        val currentHistory = _chatMessages.value
        _chatMessages.value = currentHistory + userMsgObj
        _isAiThinking.value = true

        viewModelScope.launch {
            val profile = farmerProfile.value
            val activeFields = fields.value
            val firstField = activeFields.firstOrNull()

            val farmerName = profile?.name ?: "Farmer"
            val location = if (profile != null) "${profile.village}, ${profile.district}, ${profile.state}" else "Hooghly, West Bengal"
            val crop = firstField?.cropName ?: "Not specified"
            val stage = firstField?.growthStage ?: "Not specified"
            val area = firstField?.area
            val soil = firstField?.soilInfo ?: "Not recorded"
            val lang = "English"

            val imageBase64 = if (bitmap != null) {
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
                Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)
            } else null

            // Build multi-turn conversational history turns
            val historyPairs = currentHistory.takeLast(6).map {
                Pair(if (it.isUser) "user" else "model", it.message)
            }

            val systemContext = """
                You are PRITHVI AI, a knowledgeable, trusted personal farming assistant speaking directly to one farmer ($farmerName).
                
                FARMER & FIELD CONTEXT:
                - Farmer: $farmerName
                - Location: $location
                - Crop: $crop (${area?.let { "$it acres" } ?: "area not recorded"}, $stage stage)
                - Soil: $soil
                - No connected soil sensor readings are available.
                - Weather data must be obtained from the weather service; do not assume values when unavailable.
                - Preferred Language: $lang
                
                RESPONSE RULES (MANDATORY):
                1. SHORT & CONCISE:
                   - For normal questions: 2 to 5 short sentences.
                   - For simple questions: 1 to 3 sentences.
                   - NEVER produce long paragraphs, scientific essays, or chemical dosage dumps.
                2. ANSWER THE QUESTION FIRST:
                   - Immediately give the direct answer in the first sentence.
                3. SIMPLE STRUCTURE (Use when helpful):
                   - 🟢 Direct answer in 1 sentence.
                   - 🔍 Short why/explanation (1 sentence).
                   - ✅ What to do (1 or 2 practical actions).
                   - 💡 Tip (1 small preventive tip when genuinely relevant).
                4. ONE-TO-ONE CONVERSATIONAL STYLE:
                   - Talk directly to $farmerName ("You should check your soil today", "For your $crop, check the moisture first").
                   - Never say "Farmers should consider...". Do not repeatedly call them "farmer".
                   - Be calm, practical, and helpful.
                5. SAFETY & CHEMICALS:
                   - Do not overwhelm with chemical concentrations or recipes.
                   - For plant health issues, say "This may be..." or "This appears consistent with...", never claim certain diagnosis without physical lab check. Ask for a photo if needed.
                   - Advise following product labels and local Krishi Vigyan Kendra guidance.
                6. STRICT LANGUAGE:
                   - You MUST reply ENTIRELY in $lang (e.g. Bengali script for Bengali, Hindi Devanagari for Hindi, English for English).
                   - Do NOT mix languages unnecessarily.
            """.trimIndent()

            val aiResponse = geminiService.queryPrithviAi(
                prompt = textToSend,
                systemContext = systemContext,
                conversationHistory = historyPairs,
                imageBase64 = imageBase64,
                language = lang
            )

            val finalReply = aiResponse.ifBlank { "PRITHVI AI could not generate a response. Please try again when the AI service is available." }

            _chatMessages.value = _chatMessages.value + ChatMessage(sender = "PRITHVI AI", message = finalReply, isUser = false)
            _isAiThinking.value = false
        }
    }

    fun requestTellMeMore(aiMessageToExpand: String) {
        val prompt = "Please explain slightly more practical details about this."
        sendMessageToPrithvi(prompt)
    }

    fun clearChat() {
        val welcomeMsg = "Hello! I am PRITHVI AI, your farming assistant. Ask me about crop care, plant symptoms, soil management or farm planning."
        _chatMessages.value = listOf(
            ChatMessage(
                sender = "PRITHVI AI",
                message = welcomeMsg,
                isUser = false
            )
        )
    }

    fun sendDirectMessage(recipient: String, messageText: String) {
        if (messageText.isBlank()) return
        val currentMap = _directMessages.value.toMutableMap()
        val currentList = currentMap[recipient] ?: emptyList()
        val newMsg = DirectMessage(senderName = "Farmer", message = messageText, timestamp = "Just now", isUser = true)
        currentMap[recipient] = currentList + newMsg
        _directMessages.value = currentMap
    }

    fun shareScanToCommunity(scan: CropScanEntity) {
        viewModelScope.launch {
            val profile = farmerProfile.value
            val name = profile?.name ?: "Farmer"
            val location = if (profile != null) "${profile.district}, ${profile.state}" else "Hooghly, West Bengal"
            val postContent = """
                ⚡ [PRITHVI AI Scan Shared]
                Crop: ${scan.cropName}
                Diagnosis: ${scan.problemName} (${scan.confidencePercent}% AI Match)
                Symptoms: ${scan.symptoms}
                Recommended Treatment: ${scan.recommendedSteps}
            """.trimIndent()

            repository.addCommunityPost(
                CommunityPostEntity(
                    authorName = name,
                    location = location,
                    cropTag = scan.cropName,
                    content = postContent,
                    isUserPost = true
                )
            )
        }
    }

    fun analyzeCropScanImage(bitmap: Bitmap, userCropHint: String = "") {
        _isAiThinking.value = true
        viewModelScope.launch {
            // Local brightness check to catch pitch dark photos immediately
            val isTooDark = checkBitmapTooDark(bitmap)
            if (isTooDark) {
                val darkScan = CropScanEntity(
                    cropName = "Unidentified",
                    problemName = "Image Quality Issue",
                    confidencePercent = 0,
                    symptoms = "The photo is too dark or underexposed to detect crop features.",
                    possibleCauses = "Insufficient ambient lighting or camera shadow.",
                    recommendedSteps = "Please take a closer, well-lit photo of the affected leaf or plant.",
                    prevention = "Ensure direct, even lighting on the plant part being scanned.",
                    isSevere = false,
                    plantPart = "Unknown",
                    healthStatus = "Unusable Photo",
                    immediateAction = "Retake photo with better lighting.",
                    whenToSeekExpert = "Consult an agricultural expert if you cannot obtain clear photos.",
                    isQualityIssue = true,
                    qualityMessage = "Please take a closer, well-lit photo of the affected leaf or plant."
                )
                repository.addCropScan(darkScan)
                _isAiThinking.value = false
                return@launch
            }

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos)
            val base64 = Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)

            val prompt = """
                Analyze this single plant or crop photograph. Do NOT require the user to pre-select any crop. Automatically identify the crop/plant and analyze its health condition.

                STEP 1: IMAGE USABILITY CHECK
                Check whether the image is usable. If the image is:
                - Too blurry
                - Too dark
                - Too far away
                - Obstructed
                - Showing an unrelated object (not an agricultural plant/crop)
                - Not showing enough of the plant
                Then you MUST output:
                Image Quality: Invalid
                Quality Issue: Please take a closer, well-lit photo of the affected leaf or plant.

                STEP 2: IF USABLE, PROVIDE UNIVERSAL PLANT PATHOLOGY ANALYSIS
                Analyze across all types of agricultural crops (e.g. rice/paddy, wheat, maize/corn, tomato, potato, cotton, mustard, sugarcane, jute, pulses, legumes, vegetables, fruits, oilseeds, spices, etc.).
                Do not present the diagnosis as 100% certain. Use wording such as "Possible diagnosis: ..." or "The image appears consistent with...".

                Format your response using the exact labels below:
                Image Quality: Valid
                Crop Detected: <Identified crop name in English, e.g. Wheat, Rice / Paddy, Tomato, Maize / Corn, Potato, Cotton, Mustard, Sugarcane, Chilli, Gram, Soybean, etc.>
                Plant Part: <Leaf, Stem, Fruit, Flower, Root, Ear / Panicle, or Whole Plant>
                Health Status: <Healthy OR Possible Abnormality Detected OR Pest Infestation OR Nutrient Deficiency>
                Possible Disease / Problem: <"Healthy Plant - No apparent abnormality" OR "The image appears consistent with <Name of Disease/Problem>" or "Possible diagnosis: <Name>">
                Confidence: <A percentage only when justified by the visible evidence; otherwise write Not reliably quantifiable>
                Severity: <Low OR Moderate OR High OR None>
                Visible Symptoms: <Clear farmer-friendly description of observed symptoms>
                Likely Cause: <Simple, farmer-friendly explanation of pathogen, pest, environmental factor, or soil/water cause>
                Immediate Action: <1 or 2 quick immediate steps the farmer can take today>
                Recommended Treatment: <Practical, farmer-friendly organic and standard agricultural recommendations. Remind farmer to follow product label instructions and local regulations.>
                Prevention Tips: <Practical long-term prevention, sanitation, spacing, or crop rotation tips>
                When to Seek Expert Help: <Clear threshold for when the farmer should contact their local Krishi Vigyan Kendra (KVK) or extension officer>
            """.trimIndent()

            val systemContext = """
                You are PRITHVI AI — Universal Crop Scanner, an expert agricultural plant pathologist and agronomist.
                You assess any crop photograph without requiring prior crop selection. Identify the plant only when possible and describe visible features. Offer possible causes, not a definitive diagnosis.
                Always communicate uncertainty honestly (e.g., 'Possible diagnosis', 'The image appears consistent with').
                Never recommend hazardous chemical overdoses.
            """.trimIndent()

            val result = geminiService.queryPrithviAi(prompt = prompt, systemContext = systemContext, imageBase64 = base64)

            val scanEntity = if (result.startsWith("PRITHVI AI is not connected", ignoreCase = true) || result.contains("AI service is temporarily unavailable", ignoreCase = true)) {
                CropScanEntity(
                    cropName = "Unidentified Plant",
                    problemName = "AI analysis unavailable",
                    confidencePercent = 0,
                    symptoms = "No image assessment was generated.",
                    possibleCauses = "No cause was assessed.",
                    recommendedSteps = "Check your connection and retry the scan. Do not apply treatment based on an unavailable analysis.",
                    prevention = "Keep a clear record of plant symptoms and their progression.",
                    isSevere = false,
                    plantPart = "Unclear",
                    healthStatus = "Analysis unavailable",
                    immediateAction = "Retry when the AI service is connected.",
                    whenToSeekExpert = "Contact a local agricultural extension officer if the crop is deteriorating.",
                    isQualityIssue = true,
                    qualityMessage = result
                )
            } else if (result.isNotBlank()) {
                val imageQuality = extractField(result, "Image Quality") ?: "Valid"
                val isInvalidQuality = imageQuality.contains("invalid", ignoreCase = true) ||
                        (result.contains("Quality Issue:", ignoreCase = true) && !result.contains("Crop Detected:", ignoreCase = true))

                if (isInvalidQuality) {
                    val qualityMsg = extractField(result, "Quality Issue")
                        ?: "Please take a closer, well-lit photo of the affected leaf or plant."
                    CropScanEntity(
                        cropName = "Unidentified Plant",
                        problemName = "Image Quality Issue",
                        confidencePercent = 0,
                        symptoms = "The image is unclear, blurry, or does not show enough of the crop for analysis.",
                        possibleCauses = "Blurriness, poor lighting, or camera distance.",
                        recommendedSteps = "Please take a closer, well-lit photo of the affected leaf or plant.",
                        prevention = "Hold the camera steady 15-20 cm from the plant with good lighting.",
                        isSevere = false,
                        plantPart = "Unclear",
                        healthStatus = "Unusable Photo",
                        immediateAction = "Retake photo with better focus.",
                        whenToSeekExpert = "If you have trouble photographing, consult a local extension agent in person.",
                        isQualityIssue = true,
                        qualityMessage = qualityMsg
                    )
                } else {
                    val cropDetected = extractField(result, "Crop Detected")?.ifBlank { null } ?: "Unidentified Plant"
                    val plantPart = extractField(result, "Plant Part") ?: "Leaf"
                    val healthStatus = extractField(result, "Health Status") ?: "Unable to assess"
                    val problemName = extractField(result, "Possible Disease / Problem")
                        ?: extractField(result, "Possible Problem")
                        ?: extractField(result, "Disease Name")
                        ?: "Insufficient evidence for a diagnosis"
                    val confidence = extractField(result, "Confidence")?.filter { it.isDigit() }?.toIntOrNull()?.coerceIn(0, 100) ?: 0
                    val severityStr = extractField(result, "Severity") ?: ""
                    val isSevere = severityStr.contains("high", ignoreCase = true) || severityStr.contains("critical", ignoreCase = true)
                    val symptoms = extractField(result, "Visible Symptoms") ?: extractField(result, "Symptoms") ?: "The image did not provide enough detail for a reliable symptom assessment."
                    val likelyCause = extractField(result, "Likely Cause") ?: extractField(result, "Cause") ?: "Several causes may produce similar visual symptoms. More information is needed."
                    val immediateAction = extractField(result, "Immediate Action") ?: "Retake a clear photograph in natural light and inspect nearby plants for similar symptoms."
                    val treatment = extractField(result, "Recommended Treatment") ?: extractField(result, "Treatment") ?: "Do not apply a pesticide or fertilizer based on this image alone. Seek a local agricultural assessment if symptoms persist."
                    val prevention = extractField(result, "Prevention Tips") ?: extractField(result, "Prevention") ?: "Keep field observations and photographs so changes can be compared over time."
                    val expertHelp = extractField(result, "When to Seek Expert Help") ?: "Consult a local Krishi Vigyan Kendra (KVK) or extension officer if symptoms spread or plants decline rapidly."

                    CropScanEntity(
                        cropName = cropDetected,
                        problemName = problemName,
                        confidencePercent = confidence,
                        symptoms = symptoms,
                        possibleCauses = likelyCause,
                        recommendedSteps = treatment,
                        prevention = prevention,
                        isSevere = isSevere,
                        plantPart = plantPart,
                        healthStatus = healthStatus,
                        immediateAction = immediateAction,
                        whenToSeekExpert = expertHelp,
                        isQualityIssue = false,
                        qualityMessage = ""
                    )
                }
            } else {
                CropScanEntity(
                    cropName = "Unidentified Plant",
                    problemName = "AI analysis unavailable",
                    confidencePercent = 0,
                    symptoms = "No image assessment was generated.",
                    possibleCauses = "No cause was assessed.",
                    recommendedSteps = "Check your connection and retry the scan. Do not apply treatment based on an unavailable analysis.",
                    prevention = "Keep a clear record of plant symptoms and their progression.",
                    isSevere = false,
                    plantPart = "Unclear",
                    healthStatus = "Analysis unavailable",
                    immediateAction = "Retry when the AI service is connected.",
                    whenToSeekExpert = "Contact a local agricultural extension officer if the crop is deteriorating.",
                    isQualityIssue = true,
                    qualityMessage = result.ifBlank { "The AI service did not return an assessment." }
                )
            }

            repository.addCropScan(scanEntity)
            _isAiThinking.value = false
        }
    }

    private fun checkBitmapTooDark(bitmap: Bitmap): Boolean {
        return try {
            val sampleWidth = 20.coerceAtMost(bitmap.width)
            val sampleHeight = 20.coerceAtMost(bitmap.height)
            val stepX = (bitmap.width / sampleWidth).coerceAtLeast(1)
            val stepY = (bitmap.height / sampleHeight).coerceAtLeast(1)
            var totalBrightness = 0L
            var sampleCount = 0
            for (x in 0 until bitmap.width step stepX) {
                for (y in 0 until bitmap.height step stepY) {
                    val pixel = bitmap.getPixel(x, y)
                    val r = (pixel shr 16) and 0xFF
                    val g = (pixel shr 8) and 0xFF
                    val b = pixel and 0xFF
                    val brightness = (r * 299 + g * 587 + b * 114) / 1000
                    totalBrightness += brightness
                    sampleCount++
                }
            }
            if (sampleCount > 0) {
                val avgBrightness = totalBrightness / sampleCount
                avgBrightness < 12
            } else false
        } catch (e: Exception) {
            false
        }
    }

    private fun extractField(text: String, label: String): String? {
        val lines = text.lines()
        for (i in lines.indices) {
            val line = lines[i].trim()
            val cleanLine = line.replace("*", "").trim()
            if (cleanLine.startsWith("$label:", ignoreCase = true) || (cleanLine.startsWith(label, ignoreCase = true) && cleanLine.contains(":"))) {
                val directValue = cleanLine.substringAfter(":").trim()
                if (directValue.isNotBlank()) {
                    return directValue
                }
                if (i + 1 < lines.size) {
                    val nextLine = lines[i + 1].trim().replace("*", "").trim()
                    if (nextLine.isNotBlank() && !nextLine.contains(":")) {
                        return nextLine
                    }
                }
            }
        }
        val fallbackLine = text.lines().firstOrNull { it.contains(label, ignoreCase = true) && it.contains(":") }
        return fallbackLine?.substringAfter(":")?.replace("*", "")?.trim()
    }

    fun addExpense(farmId: Long, category: String, amount: Double, note: String) {
        viewModelScope.launch {
            repository.addExpense(ExpenseEntity(farmId = farmId, category = category, amount = amount, note = note))
        }
    }

    fun deleteExpense(expenseId: Long) {
        viewModelScope.launch {
            repository.deleteExpense(expenseId)
        }
    }

    fun addHarvest(fieldId: Long, cropName: String, quantity: Double, unit: String, price: Double, market: String) {
        viewModelScope.launch {
            val revenue = quantity * price
            repository.addHarvest(
                HarvestEntity(
                    fieldId = fieldId,
                    cropName = cropName,
                    quantity = quantity,
                    unit = unit,
                    sellingPricePerUnit = price,
                    marketName = market,
                    revenue = revenue
                )
            )
        }
    }

    fun deleteHarvest(harvestId: Long) {
        viewModelScope.launch {
            repository.deleteHarvest(harvestId)
        }
    }

    fun addSoilHealth(farmId: Long, ph: Double, n: Double, p: Double, k: Double, oc: Double, date: String, notes: String) {
        viewModelScope.launch {
            repository.addSoilRecord(
                SoilHealthEntity(
                    farmId = farmId,
                    phLevel = ph,
                    nitrogenKgHa = n,
                    phosphorusKgHa = p,
                    potassiumKgHa = k,
                    organicCarbonPercent = oc,
                    testDate = date,
                    notes = notes
                )
            )
        }
    }

    fun addCommunityPost(content: String, cropTag: String, imageUri: String = "") {
        viewModelScope.launch {
            val profile = farmerProfile.value
            val name = profile?.name ?: "Farmer"
            val location = if (profile != null) "${profile.district}, ${profile.state}" else "Hooghly, West Bengal"
            repository.addCommunityPost(
                CommunityPostEntity(
                    authorName = name,
                    location = location,
                    cropTag = cropTag,
                    content = content,
                    imageUri = imageUri,
                    isUserPost = true
                )
            )
        }
    }

    fun toggleLikePost(post: CommunityPostEntity) {
        viewModelScope.launch {
            repository.toggleLikePost(post)
        }
    }
}
