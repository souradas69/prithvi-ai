package com.example.ui.components

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.voice.IndianVoiceLanguage
import com.example.data.voice.IndianVoiceRecognitionManager
import com.example.ui.theme.AgContainerGreen
import com.example.ui.theme.AgDarkGreen
import com.example.ui.theme.AgLeafGreen
import com.example.ui.theme.AgPrimaryGreen
import com.example.ui.theme.AgSproutGreen
import com.example.ui.theme.AgWarningOrange

@Composable
fun AccessibleFarmerVoiceDialog(
    initialLanguage: String,
    cropName: String = "Tomato",
    isListening: Boolean,
    transcribedText: String,
    onLanguageSelected: (IndianVoiceLanguage) -> Unit,
    onStartListening: (IndianVoiceLanguage) -> Unit,
    onStopListening: () -> Unit,
    onConfirmSendQuery: (String, IndianVoiceLanguage) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedLanguage by remember {
        mutableStateOf(IndianVoiceRecognitionManager.getLanguageByDisplayName(initialLanguage))
    }
    var queryText by remember(transcribedText) {
        mutableStateOf(transcribedText)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_mic")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = if (isListening) 1.25f else 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isListening) 550 else 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "micPulse"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier
            .fillMaxWidth()
            .testTag("accessible_farmer_voice_dialog"),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(AgPrimaryGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = "Voice Assistant",
                            tint = AgPrimaryGreen,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "बोलकर पूछें / Voice Query",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "AI Voice Assistant in 12 Indian Languages",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp).testTag("close_voice_dialog_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Big Accessible Animated Microphone Hub
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isListening) Color(0xFFE0F2FE) else AgContainerGreen
                    ),
                    border = BorderStroke(
                        1.5.dp,
                        if (isListening) Color(0xFF0284C7) else AgSproutGreen
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isListening) "🎤 Speak now..." else "Speak your question in English",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isListening) Color(0xFF0369A1) else AgDarkGreen,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        // Central Accessible Mic Touch Button (Minimum 68dp)
                        Box(
                            modifier = Modifier
                                .size(88.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Outer pulsing aura
                            Box(
                                modifier = Modifier
                                    .size(80.dp * pulseScale)
                                    .clip(CircleShape)
                                    .background(
                                        if (isListening) Color(0xFF38BDF8).copy(alpha = 0.35f)
                                        else AgPrimaryGreen.copy(alpha = 0.2f)
                                    )
                            )

                            // Inner mic button
                            Surface(
                                onClick = {
                                    if (isListening) {
                                        onStopListening()
                                    } else {
                                        onStartListening(selectedLanguage)
                                    }
                                },
                                shape = CircleShape,
                                color = if (isListening) Color(0xFF0284C7) else AgPrimaryGreen,
                                shadowElevation = 6.dp,
                                modifier = Modifier
                                    .size(64.dp)
                                    .testTag("accessible_dialog_mic_button")
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = if (isListening) "Stop Listening" else "Tap to Speak",
                                        tint = Color.White,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (isListening) "Listening in English..." else "Tap microphone to speak your question",
                            fontSize = 11.5.sp,
                            color = if (isListening) Color(0xFF0284C7) else Color.DarkGray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Transcribed Speech Output / Edit Box
                AnimatedVisibility(
                    visible = queryText.isNotBlank(),
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .testTag("transcribed_text_card"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, AgPrimaryGreen)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.GraphicEq,
                                        contentDescription = null,
                                        tint = AgPrimaryGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Transcribed Voice Query:",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AgDarkGreen
                                    )
                                }
                                IconButton(
                                    onClick = { queryText = "" },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = "Clear", tint = Color.Gray, modifier = Modifier.size(16.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            OutlinedTextField(
                                value = queryText,
                                onValueChange = { queryText = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("transcribed_query_input"),
                                shape = RoundedCornerShape(10.dp),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                                maxLines = 3
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    if (queryText.isNotBlank()) {
                                        onConfirmSendQuery(queryText, selectedLanguage)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .testTag("send_voice_query_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen)
                            ) {
                                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Ask PRITHVI AI",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Quick Local Language Spoken Questions (Tap to Ask)
                Text(
                    text = "Quick Spoken Questions (Tap to Ask):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val sampleQueries = listOf(
                        "What should I do if my tomato plants are getting yellow leaves?",
                        "What is the fertilizer schedule for paddy?",
                        "Is it going to rain tomorrow?"
                    )
                    sampleQueries.forEachIndexed { index, sample ->
                        Surface(
                            onClick = {
                                queryText = sample
                                onConfirmSendQuery(sample, selectedLanguage)
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = AgContainerGreen.copy(alpha = 0.7f),
                            border = BorderStroke(1.dp, AgSproutGreen.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("voice_sample_query_$index")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(AgPrimaryGreen),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = sample,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = AgDarkGreen,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    tint = AgPrimaryGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("dismiss_voice_dialog_button")
            ) {
                Text("Close", fontWeight = FontWeight.Bold)
            }
        }
    )
}
