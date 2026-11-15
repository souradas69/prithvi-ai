package com.example.ui.screens

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import com.example.data.voice.IndianVoiceLanguage
import com.example.data.voice.IndianVoiceRecognitionManager
import com.example.ui.components.AccessibleFarmerVoiceDialog
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AgContainerGreen
import com.example.ui.theme.AgDarkGreen
import com.example.ui.theme.AgGenZLime
import com.example.ui.theme.AgGenZMint
import com.example.ui.theme.AgGenZNeon
import com.example.ui.theme.AgLeafGreen
import com.example.ui.theme.AgPrimaryGreen
import com.example.ui.theme.AgSproutGreen
import com.example.ui.theme.AgWarningOrange
import com.example.ui.viewmodel.ChatMessage
import java.util.Locale

data class LanguageOption(val displayName: String, val nativeName: String, val tag: String)

val SUPPORTED_LANGUAGES = listOf(
    LanguageOption("English", "English", "en-IN")
)

fun getQuickSparksForLanguage(language: String): List<String> {
    return when {
        language.equals("Bengali", ignoreCase = true) || language.equals("বাংলা", ignoreCase = true) -> listOf(
            "💧 কখন সেচ দেব?",
            "🌿 পাতার সমস্যা?",
            "🌤️ আজকের আবহাওয়া",
            "📈 বাজার দর",
            "📋 আজ কী করব?"
        )
        language.equals("Hindi", ignoreCase = true) || language.equals("हिन्दी", ignoreCase = true) -> listOf(
            "💧 सिंचाई कब करें?",
            "🌿 फसल की समस्या?",
            "🌤️ आज का मौसम",
            "📈 आज का मंडी भाव",
            "📋 आज क्या करें?"
        )
        language.equals("Tamil", ignoreCase = true) -> listOf(
            "💧 எப்போது நீர் பாய்ச்ச வேண்டும்?",
            "🌿 இலை நோய் பிரச்சனை?",
            "🌤️ இன்றைய வானிலை",
            "📈 சந்தை விலை",
            "📋 இன்று என்ன செய்ய வேண்டும்?"
        )
        language.equals("Telugu", ignoreCase = true) -> listOf(
            "💧 ఎప్పుడు నీరు పెట్టాలి?",
            "🌿 ఆకుల సమస్య?",
            "🌤️ నేటి వాతావరణం",
            "📈 మార్కెట్ ధర",
            "📋 ఈరోజు ఏమి చేయాలి?"
        )
        language.equals("Marathi", ignoreCase = true) -> listOf(
            "💧 पाणी कधी द्यावे?",
            "🌿 पानांची समस्या?",
            "🌤️ आजचे हवामान",
            "📈 आजचा बाजारभाव",
            "📋 आज काय करावे?"
        )
        language.equals("Gujarati", ignoreCase = true) -> listOf(
            "💧 ક્યારે પિયત આપવું?",
            "🌿 પાંદડાની સમસ્યા?",
            "🌤️ આજનું હવામાન",
            "📈 બજાર ભાવ",
            "📋 આજે શું કરવું?"
        )
        language.equals("Punjabi", ignoreCase = true) -> listOf(
            "💧 ਸਿੰਚਾਈ ਕਦੋਂ ਕਰੀਏ?",
            "🌿 ਪੱਤਿਆਂ ਦੀ ਸਮੱਸਿਆ?",
            "🌤️ ਅੱਜ ਦਾ ਮੌਸਮ",
            "📈 ਮੰਡੀ ਦਾ ਭਾਅ",
            "📋 ਅੱਜ ਕੀ ਕਰਨਾ ਹੈ?"
        )
        else -> listOf(
            "💧 When should I irrigate?",
            "🌿 Crop problem?",
            "🌤️ Today's weather",
            "📈 Market price",
            "📋 What should I do today?"
        )
    }
}

fun getVoicePromptsForLanguage(language: String, cropName: String): List<String> {
    return when {
        language.equals("Bengali", ignoreCase = true) || language.equals("বাংলা", ignoreCase = true) -> listOf(
            "আজ $cropName জমিতে কী করতে হবে?",
            "$cropName পাতার হলুদ দাগে কী করব?",
            "$cropName গাছে কখন জল দেব?",
            "আজকের বাজারে $cropName দর কত?"
        )
        language.equals("Hindi", ignoreCase = true) || language.equals("हिन्दी", ignoreCase = true) -> listOf(
            "आज $cropName में क्या करना चाहिए?",
            "$cropName की पत्तियों पर पीले धब्बे?",
            "$cropName में सिंचाई कब करें?",
            "$cropName का आज का मंडी भाव?"
        )
        language.equals("Tamil", ignoreCase = true) -> listOf(
            "இன்று $cropName பயிருக்கு என்ன செய்ய வேண்டும்?",
            "$cropName இலை மஞ்சள் நோய்க்கான மருந்து?",
            "$cropName பயிருக்கு எப்போது நீர் பாய்ச்ச வேண்டும்?"
        )
        language.equals("Telugu", ignoreCase = true) -> listOf(
            "ఈరోజు $cropName పంటకు ఏమి చేయాలి?",
            "$cropName ఆకులపై పసుపు మచ్చలకు మందు?",
            "$cropName పంటకు ఎప్పుడు నీరు పెట్టాలి?"
        )
        language.equals("Marathi", ignoreCase = true) -> listOf(
            "आज $cropName पिकासाठी काय करावे?",
            "$cropName पानांवरील पिवळे डाग?",
            "$cropName ला पाणी कधी द्यावे?"
        )
        else -> listOf(
            "What should I do on my $cropName farm today?",
            "Why are my $cropName leaves yellow?",
            "When should I irrigate $cropName?",
            "What is today's market price for $cropName?"
        )
    }
}

enum class VoiceAssistantState {
    IDLE,
    LISTENING,
    THINKING,
    SPEAKING
}

fun getVoiceStateText(state: VoiceAssistantState, language: String, cropName: String): String {
    return when (state) {
        VoiceAssistantState.LISTENING -> when {
            language.equals("Bengali", ignoreCase = true) || language.equals("বাংলা", ignoreCase = true) -> "🎤 শুনছি... $cropName নিয়ে বলুন"
            language.equals("Hindi", ignoreCase = true) || language.equals("हिन्दी", ignoreCase = true) -> "🎤 सुन रहा हूँ... $cropName के लिए बोलें"
            else -> "🎤 Listening... Speak your $cropName query"
        }
        VoiceAssistantState.THINKING -> when {
            language.equals("Bengali", ignoreCase = true) || language.equals("বাংলা", ignoreCase = true) -> "🧠 ভাবছি... সঠিক কৃষি পরামর্শ তৈরি হচ্ছে"
            language.equals("Hindi", ignoreCase = true) || language.equals("हिन्दी", ignoreCase = true) -> "🧠 सोच रहा हूँ... कृषि सलाह तैयार हो रही है"
            else -> "🧠 Thinking... Formulating farm advice"
        }
        VoiceAssistantState.SPEAKING -> when {
            language.equals("Bengali", ignoreCase = true) || language.equals("বাংলা", ignoreCase = true) -> "🔊 পৃথ্বী এআই উত্তর শোনাচ্ছে..."
            language.equals("Hindi", ignoreCase = true) || language.equals("हिन्दी", ignoreCase = true) -> "🔊 पृथ्वी एआई उत्तर सुना रहा है..."
            else -> "🔊 PRITHVI Voice Speaking..."
        }
        VoiceAssistantState.IDLE -> ""
    }
}

fun getTellMeMoreLabel(language: String): String {
    return when {
        language.equals("Bengali", ignoreCase = true) || language.equals("বাংলা", ignoreCase = true) -> "💡 আরও জানুন"
        language.equals("Hindi", ignoreCase = true) || language.equals("हिन्दी", ignoreCase = true) -> "💡 और जानें"
        language.equals("Tamil", ignoreCase = true) -> "💡 மேலும் அறிய"
        language.equals("Telugu", ignoreCase = true) -> "💡 మరిన్ని వివరాలు"
        language.equals("Marathi", ignoreCase = true) -> "💡 अधिक माहिती"
        language.equals("Gujarati", ignoreCase = true) -> "💡 વધુ જાણો"
        language.equals("Punjabi", ignoreCase = true) -> "💡 ਹੋਰ ਜਾਣਕਾਰੀ"
        else -> "💡 Tell me more"
    }
}

fun getLocaleForLanguage(language: String): Locale {
    return when {
        language.equals("Bengali", ignoreCase = true) || language.equals("বাংলা", ignoreCase = true) -> Locale("bn", "IN")
        language.equals("Hindi", ignoreCase = true) || language.equals("हिन्दी", ignoreCase = true) -> Locale("hi", "IN")
        language.equals("Tamil", ignoreCase = true) -> Locale("ta", "IN")
        language.equals("Telugu", ignoreCase = true) -> Locale("te", "IN")
        language.equals("Marathi", ignoreCase = true) -> Locale("mr", "IN")
        language.equals("Gujarati", ignoreCase = true) -> Locale("gu", "IN")
        language.equals("Punjabi", ignoreCase = true) -> Locale("pa", "IN")
        language.equals("Kannada", ignoreCase = true) -> Locale("kn", "IN")
        language.equals("Malayalam", ignoreCase = true) -> Locale("ml", "IN")
        language.equals("Urdu", ignoreCase = true) -> Locale("ur", "IN")
        else -> Locale("en", "IN")
    }
}

fun getInputPlaceholderForLanguage(language: String, isListening: Boolean, cropName: String): String {
    return when {
        isListening -> when {
            language.equals("Bengali", ignoreCase = true) || language.equals("বাংলা", ignoreCase = true) -> "শুনছি... $cropName নিয়ে প্রশ্ন বলুন"
            language.equals("Hindi", ignoreCase = true) || language.equals("हिन्दी", ignoreCase = true) -> "सुन रहा हूँ... $cropName के लिए बोलें"
            else -> "Listening for $cropName question..."
        }
        else -> when {
            language.equals("Bengali", ignoreCase = true) || language.equals("বাংলা", ignoreCase = true) -> "ফসলের প্রশ্ন লিখুন বা মাইকে বলুন..."
            language.equals("Hindi", ignoreCase = true) || language.equals("हिन्दी", ignoreCase = true) -> "फसल का प्रश्न पूछें या माइक दबाएँ..."
            else -> "Ask crop query or tap mic..."
        }
    }
}

@Composable
fun AnimatedEqualizerWaveform(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF0284C7)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "eq_bars")
    val h1 by infiniteTransition.animateFloat(
        initialValue = 0.25f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "h1"
    )
    val h2 by infiniteTransition.animateFloat(
        initialValue = 0.85f, targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(550, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "h2"
    )
    val h3 by infiniteTransition.animateFloat(
        initialValue = 0.35f, targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(350, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "h3"
    )
    val h4 by infiniteTransition.animateFloat(
        initialValue = 0.65f, targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(480, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "h4"
    )

    Row(
        modifier = modifier.height(16.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(h1, h2, h3, h4).forEach { factor ->
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .fillMaxHeight(fraction = factor.coerceIn(0.2f, 1f))
                    .clip(RoundedCornerShape(2.dp))
                    .background(color)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun PrithviAiChatScreen(
    messages: List<ChatMessage>,
    isAiThinking: Boolean,
    onSendMessage: (String) -> Unit,
    onSendMessageWithImage: ((String, Bitmap?) -> Unit)? = null,
    onTellMeMore: ((String) -> Unit)? = null,
    onClearChat: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null,
    farmerName: String,
    cropName: String,
    selectedLanguage: String = "English",
    onLanguageChange: ((String) -> Unit)? = null,
    isSimpleMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }
    var isListening by remember { mutableStateOf(false) }
    var isAudioSpeaking by remember { mutableStateOf(false) }
    var showLangMenu by remember { mutableStateOf(false) }
    var showClearDialog by remember { mutableStateOf(false) }
    var showVoiceDialog by remember { mutableStateOf(false) }
    var transcribedVoiceText by remember { mutableStateOf("") }
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    var showPermissionDialog by remember { mutableStateOf<String?>(null) } // "CAMERA" or "MICROPHONE"

    // Text To Speech initialization
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    DisposableEffect(context, selectedLanguage) {
        var textToSpeechInstance: TextToSpeech? = null
        textToSpeechInstance = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                try {
                    val loc = getLocaleForLanguage(selectedLanguage)
                    textToSpeechInstance?.language = loc
                } catch (e: Exception) {
                    textToSpeechInstance?.language = Locale.ENGLISH
                }
            }
        }
        tts = textToSpeechInstance
        onDispose {
            try {
                textToSpeechInstance?.stop()
                textToSpeechInstance?.shutdown()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun speakText(text: String) {
        val clean = text.replace(Regex("[#*•_`\\[\\]]"), " ")
            .replace("🟢", "")
            .replace("🔍", "")
            .replace("✅", "")
            .replace("💡", "")
            .replace("🌱", "")
            .replace("💧", "")
            .replace("📈", "")
            .replace("🌾", "")
            .trim()

        // Voice output should be concise (10-25 seconds of speech)
        val sentences = clean.split(Regex("(?<=[.!?।\n])\\s+")).filter { it.isNotBlank() }
        val speechSummary = if (sentences.size > 3) {
            sentences.take(3).joinToString(" ")
        } else clean

        try {
            val loc = getLocaleForLanguage(selectedLanguage)
            tts?.language = loc
            tts?.speak(speechSummary, TextToSpeech.QUEUE_FLUSH, null, "prithvi_msg_${System.currentTimeMillis()}")
            isAudioSpeaking = true
        } catch (e: Exception) {
            Toast.makeText(context, "Voice playback initialized", Toast.LENGTH_SHORT).show()
        }
    }

    // Native Speech Recognizer Launcher
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        isListening = false
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val spokenMatches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = spokenMatches?.firstOrNull()
            if (!spokenText.isNullOrBlank()) {
                inputText = spokenText
            }
        }
    }

    fun launchVoiceRecognition(langName: String) {
        val lang = IndianVoiceRecognitionManager.getLanguageByDisplayName(langName)
        val intent = IndianVoiceRecognitionManager.createSpeechIntent(lang, cropName)
        isListening = true
        try {
            speechLauncher.launch(intent)
        } catch (e: Exception) {
            isListening = false
            Toast.makeText(context, "Speech recognizer unavailable.", Toast.LENGTH_SHORT).show()
        }
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            selectedBitmap = bitmap
        }
    }

    // Permission Launchers
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                cameraLauncher.launch(null)
            } catch (e: Exception) {
                Toast.makeText(context, "Could not open camera", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Camera permission is needed to scan crops", Toast.LENGTH_LONG).show()
        }
    }

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchVoiceRecognition("English")
        } else {
            Toast.makeText(context, "Microphone access is needed for voice queries", Toast.LENGTH_LONG).show()
        }
    }

    fun requestCamera() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            try {
                cameraLauncher.launch(null)
            } catch (e: Exception) {
                Toast.makeText(context, "Camera unavailable", Toast.LENGTH_SHORT).show()
            }
        } else {
            showPermissionDialog = "CAMERA"
        }
    }

    fun requestMicrophone() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            launchVoiceRecognition("English")
        } else {
            showPermissionDialog = "MICROPHONE"
        }
    }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                if (bitmap != null) {
                    selectedBitmap = bitmap
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun launchGallery() {
        try {
            galleryLauncher.launch("image/*")
        } catch (e: Exception) {
            Toast.makeText(context, "Photo gallery unavailable", Toast.LENGTH_SHORT).show()
        }
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val density = LocalDensity.current
    val imeBottom = WindowInsets.ime.getBottom(density)

    val isNearBottom by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            if (totalItems == 0) true
            else {
                val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                lastVisibleIndex >= totalItems - 2
            }
        }
    }

    LaunchedEffect(imeBottom) {
        if (imeBottom > 0 && messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            val lastMsg = messages.last()
            if (lastMsg.isUser || isNearBottom) {
                listState.animateScrollToItem(messages.size - 1)
            }
            // If audio output is active, read the latest AI message
            if (isAudioSpeaking && !lastMsg.isUser) {
                speakText(lastMsg.message)
            }
        }
    }

    // Infinite pulsating animation for active AI status
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    val currentVoiceState = when {
        isListening -> VoiceAssistantState.LISTENING
        isAiThinking -> VoiceAssistantState.THINKING
        isAudioSpeaking -> VoiceAssistantState.SPEAKING
        else -> VoiceAssistantState.IDLE
    }

    val micInfiniteTransition = rememberInfiniteTransition(label = "mic_pulse")
    val micScale by micInfiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.18f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mic_scale"
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Sleek, Compact, Standard Modern Top Bar
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = AgDarkGreen,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Back Navigation Button
                        IconButton(
                            onClick = {
                                if (onBack != null) {
                                    onBack()
                                }
                            },
                            modifier = Modifier
                                .testTag("chat_back_button")
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        // Compact Avatar with Pulsing Online Dot
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(AgPrimaryGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_prithvi_ai_illustration),
                                contentDescription = "PRITHVI AI",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Assistant Name & Status
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "PRITHVI AI",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(AgGenZLime.copy(alpha = 0.25f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "PM AI Core",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AgGenZNeon
                                    )
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(AgGenZMint.copy(alpha = pulseAlpha))
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Online • $cropName Crop ($farmerName)",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }

                        // Right Action Controls: Language, TTS, Clear
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Language Selector
                            Box {
                                Surface(
                                    onClick = { showLangMenu = true },
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color.White.copy(alpha = 0.15f),
                                    modifier = Modifier.testTag("toggle_chat_language")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Translate,
                                            contentDescription = "Language",
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = selectedLanguage.take(3).uppercase(),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }

                                DropdownMenu(
                                    expanded = showLangMenu,
                                    onDismissRequest = { showLangMenu = false }
                                ) {
                                    SUPPORTED_LANGUAGES.forEach { lang ->
                                        DropdownMenuItem(
                                            text = {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(lang.displayName, fontWeight = if (lang.displayName == selectedLanguage) FontWeight.Bold else FontWeight.Normal)
                                                    Text(lang.nativeName, fontSize = 12.sp, color = Color.Gray)
                                                }
                                            },
                                            onClick = {
                                                onLanguageChange?.invoke(lang.displayName)
                                                showLangMenu = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            // Audio Read Aloud Toggle
                            IconButton(
                                onClick = {
                                    isAudioSpeaking = !isAudioSpeaking
                                    if (isAudioSpeaking) {
                                        Toast.makeText(context, "Voice read-aloud active", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .testTag("toggle_voice_output")
                                    .size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (isAudioSpeaking) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                                    contentDescription = "Voice Output",
                                    tint = if (isAudioSpeaking) AgGenZNeon else Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Clear Chat Option
                            IconButton(
                                onClick = { showClearDialog = true },
                                modifier = Modifier
                                    .testTag("clear_chat_button")
                                    .size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Clear Chat",
                                    tint = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                // Compact Quick Topic Sparks Row
                val quickSparksList = remember(selectedLanguage) { getQuickSparksForLanguage(selectedLanguage) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = AgPrimaryGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    quickSparksList.forEach { q ->
                        Surface(
                            onClick = { onSendMessage(q) },
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, AgSproutGreen.copy(alpha = 0.6f))
                        ) {
                            Text(
                                text = q,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                // Live Voice Assistant Status Banner (Listening / Thinking / Speaking)
                AnimatedVisibility(visible = isListening || isAiThinking || isAudioSpeaking) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(14.dp),
                        color = when {
                            isListening -> Color(0xFFE0F2FE)
                            isAiThinking -> Color(0xFFE8F5E9)
                            else -> Color(0xFFFFF8E1)
                        },
                        border = BorderStroke(
                            1.dp,
                            when {
                                isListening -> Color(0xFF0284C7)
                                isAiThinking -> AgPrimaryGreen
                                else -> Color(0xFFFFA000)
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                if (isListening) {
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = null,
                                        tint = Color(0xFF0284C7),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "🎤 Is Listening... Speak in $selectedLanguage",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0369A1)
                                        )
                                        Text(
                                            text = "Restricted to $cropName query topic",
                                            fontSize = 9.5.sp,
                                            color = Color(0xFF0284C7),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                } else if (isAiThinking) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        color = AgPrimaryGreen,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "🧠 PRITHVI AI Thinking... Formulating 3-part farm advice",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AgDarkGreen
                                    )
                                } else if (isAudioSpeaking) {
                                    Icon(
                                        imageVector = Icons.Default.GraphicEq,
                                        contentDescription = null,
                                        tint = Color(0xFFE65100),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "🔊 PRITHVI Voice Speaking ($selectedLanguage)...",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFE65100)
                                    )
                                }
                            }

                            if (isListening) {
                                Surface(
                                    onClick = { isListening = false },
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFBAE6FD)
                                ) {
                                    Text(
                                        text = "⏹️ Stop",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0369A1),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            } else if (isAudioSpeaking) {
                                Surface(
                                    onClick = {
                                        tts?.stop()
                                        isAudioSpeaking = false
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFFFE0B2)
                                ) {
                                    Text(
                                        text = "⏹️ Stop Voice",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFBF360C),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Messages Feed
            LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(4.dp)) }

                    items(messages) { msg ->
                        ModernChatMessageBubble(
                            msg = msg,
                            tellMeMoreLabel = getTellMeMoreLabel(selectedLanguage),
                            onTellMeMore = if (!msg.isUser && onTellMeMore != null) {
                                { onTellMeMore.invoke(msg.message) }
                            } else null,
                            onCopy = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("PRITHVI Advisory", msg.message)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                            },
                            onSpeak = { speakText(msg.message) }
                        )
                    }

                    if (isAiThinking) {
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = AgContainerGreen.copy(alpha = 0.95f)
                                ),
                                border = BorderStroke(1.dp, AgSproutGreen.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .fillMaxWidth(0.85f)
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        color = AgPrimaryGreen,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "PRITHVI AI analyzing specific topic...",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = AgDarkGreen
                                    )
                                }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(4.dp)) }
                }

                // Attached Photo Preview
                if (selectedBitmap != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = AgContainerGreen)
                    ) {
                        Row(
                            modifier = Modifier.padding(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    bitmap = selectedBitmap!!.asImageBitmap(),
                                    contentDescription = "Photo Preview",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(6.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Crop photo attached",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgDarkGreen
                                )
                            }
                            IconButton(
                                onClick = { selectedBitmap = null },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Remove", tint = AgDarkGreen, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }

                // Topic-Restricted Spoken Query Placeholder Bar (Visible when listening)
                AnimatedVisibility(visible = isListening) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
                        border = BorderStroke(1.dp, Color(0xFF38BDF8))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.GraphicEq,
                                        contentDescription = null,
                                        tint = Color(0xFF0284C7),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Is Listening... Speak or tap query for $cropName:",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0369A1)
                                    )
                                }
                                Surface(
                                    onClick = { isListening = false },
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFE0F2FE)
                                ) {
                                    Text(
                                        text = "✕",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0369A1),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf(
                                    "Fertilizer dose for $cropName",
                                    "Cure yellow leaf spots on $cropName",
                                    "Water requirement for $cropName",
                                    "Best mandi selling price for $cropName",
                                    "Pest control spray for $cropName"
                                ).forEach { prompt ->
                                    Surface(
                                        onClick = {
                                            isListening = false
                                            inputText = prompt
                                            if (onSendMessageWithImage != null && selectedBitmap != null) {
                                                onSendMessageWithImage(prompt, selectedBitmap)
                                                selectedBitmap = null
                                            } else {
                                                onSendMessage(prompt)
                                            }
                                            inputText = ""
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color.White,
                                        border = BorderStroke(1.dp, Color(0xFFBAE6FD))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Mic,
                                                contentDescription = null,
                                                tint = Color(0xFF0284C7),
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = prompt,
                                                fontSize = 11.sp,
                                                color = Color(0xFF0369A1),
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Attached Photo Preview Bar
                if (selectedBitmap != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("attached_photo_preview_card"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = AgContainerGreen),
                        border = BorderStroke(1.dp, AgSproutGreen)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                bitmap = selectedBitmap!!.asImageBitmap(),
                                contentDescription = "Attached Crop Photo",
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "🌿 $cropName Photo Attached",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgDarkGreen
                                )
                                Text(
                                    text = "PRITHVI AI will analyze this leaf/pest when you send",
                                    fontSize = 10.sp,
                                    color = Color.DarkGray
                                )
                            }
                            IconButton(
                                onClick = { selectedBitmap = null },
                                modifier = Modifier
                                    .testTag("remove_attached_photo_button")
                                    .size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove photo",
                                    tint = Color.Red,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                // Active Audio Speaking Bar with Playback Controls
                if (isAudioSpeaking) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("audio_speaking_bar"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = AgDarkGreen),
                        border = BorderStroke(1.dp, AgSproutGreen)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = null,
                                tint = AgSproutGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "🔊 PRITHVI Voice Speaking...",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Playing agricultural advice in $selectedLanguage",
                                    fontSize = 10.sp,
                                    color = AgSproutGreen
                                )
                            }
                            Button(
                                onClick = {
                                    try {
                                        tts?.stop()
                                    } catch (e: Exception) {}
                                    isAudioSpeaking = false
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                modifier = Modifier.testTag("stop_audio_button")
                            ) {
                                Text("Stop", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Button(
                                onClick = {
                                    try {
                                        tts?.stop()
                                    } catch (e: Exception) {}
                                    isAudioSpeaking = false
                                    requestMicrophone()
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen),
                                modifier = Modifier.testTag("mic_after_speech_button")
                            ) {
                                Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("Reply", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Voice Assistant State Machine Visual Indicators
                val currentVoiceState = when {
                    isListening -> VoiceAssistantState.LISTENING
                    isAiThinking -> VoiceAssistantState.THINKING
                    isAudioSpeaking -> VoiceAssistantState.SPEAKING
                    else -> VoiceAssistantState.IDLE
                }

                // Active Listening Indicator
                AnimatedVisibility(
                    visible = currentVoiceState == VoiceAssistantState.LISTENING,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                            .testTag("voice_listening_status_card"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2FE)),
                        border = BorderStroke(1.dp, Color(0xFF38BDF8))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AnimatedEqualizerWaveform(
                                modifier = Modifier.size(24.dp, 16.dp),
                                color = Color(0xFF0284C7)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "🎤 Listening...",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0369A1)
                                )
                                Text(
                                    text = "Speak your $cropName question in $selectedLanguage",
                                    fontSize = 10.sp,
                                    color = Color(0xFF0284C7)
                                )
                            }
                            Button(
                                onClick = { isListening = false },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("stop_listening_button")
                            ) {
                                Text("Done", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Active Thinking Indicator
                AnimatedVisibility(
                    visible = currentVoiceState == VoiceAssistantState.THINKING,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                            .testTag("voice_thinking_status_card"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = AgContainerGreen),
                        border = BorderStroke(1.dp, AgSproutGreen)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = AgDarkGreen
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "🧠 Thinking...",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgDarkGreen
                                )
                                Text(
                                    text = "PRITHVI AI is formulating advice for $cropName...",
                                    fontSize = 10.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }

                // Compact Modern Input Bar with Prominent Mic & Camera Options
                val micInfiniteTransition = rememberInfiniteTransition(label = "mic_pulse")
                val micScale by micInfiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = if (isListening) 1.18f else 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(600, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "mic_scale"
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(26.dp),
                    shadowElevation = 4.dp,
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(
                        1.dp,
                        when (currentVoiceState) {
                            VoiceAssistantState.LISTENING -> Color(0xFF0284C7)
                            VoiceAssistantState.THINKING -> AgPrimaryGreen
                            else -> AgSproutGreen.copy(alpha = 0.5f)
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Camera Action with Permission Handling
                        IconButton(
                            onClick = { requestCamera() },
                            modifier = Modifier
                                .testTag("chat_camera_button")
                                .size(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Camera",
                                tint = AgPrimaryGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Gallery Action with Safe Handling
                        IconButton(
                            onClick = { launchGallery() },
                            modifier = Modifier
                                .testTag("chat_gallery_button")
                                .size(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "Gallery",
                                tint = AgPrimaryGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Text Input
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = {
                                Text(
                                    when (currentVoiceState) {
                                        VoiceAssistantState.LISTENING -> "Listening... Speak now"
                                        VoiceAssistantState.THINKING -> "Thinking... Generating advice"
                                        else -> "Ask crop query or tap mic..."
                                    },
                                    fontSize = 13.sp,
                                    color = when (currentVoiceState) {
                                        VoiceAssistantState.LISTENING -> Color(0xFF0284C7)
                                        VoiceAssistantState.THINKING -> AgDarkGreen
                                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                            },
                            modifier = Modifier
                                .testTag("chat_input_text")
                                .weight(1f)
                                .bringIntoViewRequester(bringIntoViewRequester)
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused) {
                                        coroutineScope.launch {
                                            bringIntoViewRequester.bringIntoView()
                                            if (messages.isNotEmpty()) {
                                                listState.animateScrollToItem(messages.size - 1)
                                            }
                                        }
                                    }
                                },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                imeAction = ImeAction.Send
                            ),
                            keyboardActions = KeyboardActions(
                                onSend = {
                                    if (inputText.isNotBlank() || selectedBitmap != null) {
                                        if (onSendMessageWithImage != null && selectedBitmap != null) {
                                            onSendMessageWithImage(inputText, selectedBitmap)
                                        } else {
                                            onSendMessage(inputText)
                                        }
                                        inputText = ""
                                        selectedBitmap = null
                                        coroutineScope.launch {
                                            if (messages.isNotEmpty()) {
                                                listState.animateScrollToItem(messages.size - 1)
                                            }
                                        }
                                    }
                                }
                            ),
                            shape = RoundedCornerShape(20.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            maxLines = 4
                        )

                        // Voice Mic Button (State machine triggered with animated pulse)
                        Box(
                            modifier = Modifier
                                .size(42.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isListening) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .scale(micScale)
                                        .clip(CircleShape)
                                        .background(Color(0xFF38BDF8).copy(alpha = 0.35f))
                                )
                            }
                            IconButton(
                                onClick = {
                                    if (isListening) {
                                        isListening = false
                                    } else {
                                        requestMicrophone()
                                    }
                                },
                                modifier = Modifier
                                    .testTag("chat_mic_button")
                                    .size(38.dp)
                                    .scale(if (isListening) micScale else 1f)
                                    .clip(CircleShape)
                                    .background(
                                        when (currentVoiceState) {
                                            VoiceAssistantState.LISTENING -> Color(0xFF0284C7)
                                            VoiceAssistantState.THINKING -> AgSproutGreen
                                            else -> AgSproutGreen.copy(alpha = 0.35f)
                                        }
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Voice Input Microphone",
                                    tint = if (isListening) Color.White else AgDarkGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        // Send Button
                        IconButton(
                            onClick = {
                                if (inputText.isNotBlank() || selectedBitmap != null) {
                                    if (onSendMessageWithImage != null) {
                                        onSendMessageWithImage(inputText, selectedBitmap)
                                    } else {
                                        onSendMessage(inputText)
                                    }
                                    inputText = ""
                                    selectedBitmap = null
                                    coroutineScope.launch {
                                        if (messages.isNotEmpty()) {
                                            listState.animateScrollToItem(messages.size - 1)
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .testTag("chat_send_button")
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(AgPrimaryGreen)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // Permission Request Dialog
        if (showPermissionDialog != null) {
            AlertDialog(
                onDismissRequest = { showPermissionDialog = null },
                icon = {
                    Icon(
                        imageVector = if (showPermissionDialog == "CAMERA") Icons.Default.CameraAlt else Icons.Default.Mic,
                        contentDescription = null,
                        tint = AgPrimaryGreen,
                        modifier = Modifier.size(32.dp)
                    )
                },
                title = {
                    Text(
                        text = if (showPermissionDialog == "CAMERA") "📷 Camera Access Required" else "🎤 Microphone Access Required",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                },
                text = {
                    Text(
                        text = if (showPermissionDialog == "CAMERA")
                            "PRITHVI AI uses your device camera to take live photos of plant leaves, pests, and soil to identify crop diseases and pests accurately."
                        else
                            "PRITHVI AI uses your microphone so you can speak your crop questions directly in $selectedLanguage and receive spoken guidance.",
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val req = showPermissionDialog
                            showPermissionDialog = null
                            if (req == "CAMERA") {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            } else if (req == "MICROPHONE") {
                                audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (showPermissionDialog == "CAMERA") "Allow Camera" else "Allow Microphone",
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPermissionDialog = null }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }

        // Clear Chat Confirmation
        if (showClearDialog) {
            AlertDialog(
                onDismissRequest = { showClearDialog = false },
                title = { Text("Reset PRITHVI AI Chat?", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                text = { Text("This will clear your conversation history and start a fresh session.", fontSize = 13.sp) },
                confirmButton = {
                    Button(
                        onClick = {
                            showClearDialog = false
                            onClearChat?.invoke()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AgWarningOrange)
                    ) {
                        Text("Clear", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }

@Composable
fun ModernChatMessageBubble(
    msg: ChatMessage,
    tellMeMoreLabel: String = "💡 Tell me more",
    onTellMeMore: (() -> Unit)? = null,
    onCopy: () -> Unit,
    onSpeak: () -> Unit
) {
    var isLiked by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!msg.isUser) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(AgDarkGreen),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_prithvi_ai_illustration),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
        }

        Column(
            modifier = Modifier.widthIn(max = 310.dp),
            horizontalAlignment = if (msg.isUser) Alignment.End else Alignment.Start
        ) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (msg.isUser) 16.dp else 4.dp,
                    bottomEnd = if (msg.isUser) 4.dp else 16.dp
                ),
                color = if (msg.isUser) AgPrimaryGreen else MaterialTheme.colorScheme.surface,
                border = if (!msg.isUser) BorderStroke(1.dp, AgSproutGreen.copy(alpha = 0.5f)) else null,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp)) {
                    Text(
                        text = msg.message,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = if (msg.isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (msg.isUser) FontWeight.Normal else FontWeight.Medium
                    )

                    if (!msg.isUser) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (onTellMeMore != null) {
                                Surface(
                                    onClick = onTellMeMore,
                                    shape = RoundedCornerShape(12.dp),
                                    color = AgContainerGreen,
                                    border = BorderStroke(1.dp, AgSproutGreen),
                                    modifier = Modifier.testTag("tell_me_more_button")
                                ) {
                                    Text(
                                        text = tellMeMoreLabel,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AgDarkGreen,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.width(4.dp))
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = onCopy, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.Gray, modifier = Modifier.size(13.dp))
                                }
                                Spacer(modifier = Modifier.width(2.dp))
                                IconButton(onClick = onSpeak, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = "Speak", tint = AgPrimaryGreen, modifier = Modifier.size(14.dp))
                                }
                                Spacer(modifier = Modifier.width(2.dp))
                                IconButton(onClick = { isLiked = !isLiked }, modifier = Modifier.size(24.dp)) {
                                    Icon(
                                        Icons.Default.ThumbUp,
                                        contentDescription = "Helpful",
                                        tint = if (isLiked) AgPrimaryGreen else Color.Gray,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VoiceCommandDialog(
    currentLanguage: String,
    cropName: String = "Tomato",
    onSelectLanguage: (String) -> Unit,
    onSelectPrompt: (String) -> Unit,
    onStartSpeech: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var activeLang by remember { mutableStateOf(currentLanguage) }
    val prompts = remember(activeLang, cropName) { getVoicePromptsForLanguage(activeLang, cropName) }

    val infiniteTransition = rememberInfiniteTransition(label = "voice_wave")
    val waveScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "waveScale"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.GraphicEq, contentDescription = null, tint = AgPrimaryGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Voice Assistant (Multilingual)", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Speak in any Indian language. PRITHVI AI will respond directly with concise, practical advice.",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Language Selection Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SUPPORTED_LANGUAGES.forEach { lang ->
                        val isSelected = lang.displayName.equals(activeLang, ignoreCase = true)
                        Surface(
                            onClick = {
                                activeLang = lang.displayName
                                onSelectLanguage(lang.displayName)
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) AgPrimaryGreen else MaterialTheme.colorScheme.surfaceVariant,
                            border = if (isSelected) null else BorderStroke(1.dp, Color.LightGray)
                        ) {
                            Text(
                                text = "${lang.nativeName} (${lang.displayName.take(3)})",
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Animated Mic Center Action
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp * waveScale)
                            .clip(CircleShape)
                            .background(AgSproutGreen.copy(alpha = 0.3f))
                    )
                    IconButton(
                        onClick = { onStartSpeech(activeLang) },
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(AgPrimaryGreen)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Tap to Speak",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Text(
                    text = "Tap the mic above to speak now in $activeLang",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgDarkGreen,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Or tap a quick spoken question:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))

                prompts.forEach { p ->
                    Surface(
                        onClick = { onSelectPrompt(p) },
                        shape = RoundedCornerShape(10.dp),
                        color = AgContainerGreen,
                        border = BorderStroke(1.dp, AgSproutGreen.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Mic, contentDescription = null, tint = AgPrimaryGreen, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = p, fontSize = 12.sp, color = AgDarkGreen, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", fontWeight = FontWeight.Bold)
            }
        }
    )
}
