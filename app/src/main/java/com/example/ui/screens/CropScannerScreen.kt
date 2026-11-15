package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Coronavirus
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.R
import com.example.data.entity.CropScanEntity
import com.example.ui.components.FarmingBackground
import com.example.ui.theme.AgLeafGreen
import com.example.ui.theme.AgPrimaryGreen
import com.example.ui.theme.AgWarningOrange
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executors

/**
 * PRITHVI AI — Universal Crop Scanner
 *
 * Automatically analyzes single plant/crop images across all crops without requiring
 * manual crop pre-selection (no Tomato/Rice/Wheat/Potato selection required).
 * Detects crop type, plant part, health status, and visible disease/pest/deficiency issues.
 */
@Composable
fun CropScannerScreen(
    scansHistory: List<CropScanEntity>,
    isAnalyzing: Boolean,
    onAnalyzeImage: (bitmap: Bitmap) -> Unit,
    onShareToCommunity: (CropScanEntity) -> Unit,
    onNavigateToChat: (() -> Unit)? = null,
    isDarkMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showShareConfirmDialog by remember { mutableStateOf(false) }

    // CameraX state
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }
    var isFlashEnabled by remember { mutableStateOf(false) }
    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    var imageCaptureInstance by remember { mutableStateOf<ImageCapture?>(null) }
    var cameraControlInstance by remember { mutableStateOf<Camera?>(null) }

    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }

    // Camera Permission Launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (!isGranted) {
            Toast.makeText(
                context,
                "Camera permission required to scan crop leaves",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Fallback System Camera Launcher
    val systemCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            capturedBitmap = bitmap
        }
    }

    // Gallery Picker Launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                if (bitmap != null) {
                    capturedBitmap = bitmap
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Could not open selected image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val latestScan = scansHistory.firstOrNull() ?: CropScanEntity(
        cropName = "Crop",
        problemName = "Universal Scanner Ready",
        confidencePercent = 0,
        symptoms = "Capture or upload a clear plant image to begin an AI-assisted visual assessment.",
        possibleCauses = "No cause has been assessed yet.",
        recommendedSteps = "Take a clear photo in natural light, showing the affected plant part.",
        prevention = "Maintain regular crop inspections and good field hygiene.",
        isSevere = false,
        plantPart = "Leaf / Plant",
        healthStatus = "Not assessed",
        immediateAction = "Capture or upload a crop photo.",
        whenToSeekExpert = "Consult local Krishi Vigyan Kendra (KVK) for complex plant health issues."
    )

    FarmingBackground(isDarkMode = isDarkMode) {
        Box(modifier = modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header Bar
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(0.5.dp, AgPrimaryGreen.copy(alpha = 0.2f)),
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = AgPrimaryGreen.copy(alpha = 0.15f),
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.QrCodeScanner,
                                            contentDescription = null,
                                            tint = AgPrimaryGreen,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Universal Crop Scanner",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Universal plant & disease auto-identification",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Surface(
                                shape = CircleShape,
                                color = AgPrimaryGreen.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, AgPrimaryGreen)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = AgPrimaryGreen,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "PRITHVI AI",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AgPrimaryGreen
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Navigation Tabs
                        ScrollableTabRow(
                            selectedTabIndex = selectedTab,
                            edgePadding = 0.dp,
                            containerColor = Color.Transparent,
                            contentColor = AgPrimaryGreen,
                            divider = {}
                        ) {
                            Tab(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.PhotoCamera,
                                            contentDescription = null,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Camera Scanner", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            )
                            Tab(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Science,
                                            contentDescription = null,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Diagnostic Report", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            )
                            Tab(
                                selected = selectedTab == 2,
                                onClick = { selectedTab = 2 },
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.History,
                                            contentDescription = null,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Scan History (${scansHistory.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            )
                        }
                    }
                }

                // Content View for Selected Tab
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    when (selectedTab) {
                        0 -> UniversalCameraTab(
                            hasCameraPermission = hasCameraPermission,
                            onRequestCameraPermission = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                            isFlashEnabled = isFlashEnabled,
                            onToggleFlash = {
                                isFlashEnabled = !isFlashEnabled
                                cameraControlInstance?.cameraControl?.enableTorch(isFlashEnabled)
                            },
                            lensFacing = lensFacing,
                            onFlipCamera = {
                                lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                                    CameraSelector.LENS_FACING_FRONT
                                } else {
                                    CameraSelector.LENS_FACING_BACK
                                }
                            },
                            onCaptureReady = { captureUseCase -> imageCaptureInstance = captureUseCase },
                            onCameraReady = { camera -> cameraControlInstance = camera },
                            onTriggerShutter = {
                                val imageCapture = imageCaptureInstance
                                if (imageCapture != null) {
                                    try {
                                        imageCapture.takePicture(
                                            cameraExecutor,
                                            object : ImageCapture.OnImageCapturedCallback() {
                                                override fun onCaptureSuccess(image: ImageProxy) {
                                                    val rawBitmap = image.toBitmap()
                                                    val rotationDegrees = image.imageInfo.rotationDegrees
                                                    val rotatedBitmap = if (rotationDegrees != 0) {
                                                        val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
                                                        Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.width, rawBitmap.height, matrix, true)
                                                    } else {
                                                        rawBitmap
                                                    }
                                                    image.close()

                                                    ContextCompat.getMainExecutor(context).execute {
                                                        capturedBitmap = rotatedBitmap
                                                    }
                                                }

                                                override fun onError(exception: ImageCaptureException) {
                                                    ContextCompat.getMainExecutor(context).execute {
                                                        systemCameraLauncher.launch(null)
                                                    }
                                                }
                                            }
                                        )
                                    } catch (e: Exception) {
                                        systemCameraLauncher.launch(null)
                                    }
                                } else {
                                    systemCameraLauncher.launch(null)
                                }
                            },
                            onPickGallery = { galleryLauncher.launch("image/*") },
                            onUseSystemCamera = { systemCameraLauncher.launch(null) },
                            capturedBitmap = capturedBitmap,
                            onClearCapturedPhoto = { capturedBitmap = null },
                            onScanCrop = { bitmap ->
                                onAnalyzeImage(bitmap)
                                selectedTab = 1
                            }
                        )

                        1 -> UniversalDiagnosticReportTab(
                            latestScan = latestScan,
                            isAnalyzing = isAnalyzing,
                            capturedBitmap = capturedBitmap,
                            onRetake = { selectedTab = 0 },
                            onShareToCommunity = { showShareConfirmDialog = true },
                            onNavigateToChat = onNavigateToChat,
                            onReAnalyze = {
                                val targetBitmap = capturedBitmap ?: BitmapFactory.decodeResource(context.resources, R.drawable.img_app_icon)
                                if (targetBitmap != null) {
                                    onAnalyzeImage(targetBitmap)
                                }
                            }
                        )

                        2 -> UniversalScanHistoryTab(
                            scans = scansHistory,
                            onSelectScan = {
                                selectedTab = 1
                            },
                            onStartNewScan = { selectedTab = 0 }
                        )
                    }
                }
            }

            // Share Confirmation Dialog
            if (showShareConfirmDialog) {
                AlertDialog(
                    onDismissRequest = { showShareConfirmDialog = false },
                    icon = { Icon(Icons.Default.Share, contentDescription = null, tint = AgPrimaryGreen) },
                    title = { Text("Share Scan to Farmer Community?", fontWeight = FontWeight.Bold) },
                    text = {
                        Text("This will share your universal crop diagnostic result (${latestScan.cropName} - ${latestScan.problemName}) with fellow farmers and agronomists in the community feed for helpful feedback.")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                onShareToCommunity(latestScan)
                                showShareConfirmDialog = false
                                Toast.makeText(context, "Scan shared to community feed", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen)
                        ) {
                            Text("Share Now")
                        }
                    },
                    dismissButton = {
                        OutlinedButton(onClick = { showShareConfirmDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

// ==========================================
// UNIVERSAL CAMERA-FIRST TAB
// ==========================================
@Composable
fun UniversalCameraTab(
    hasCameraPermission: Boolean,
    onRequestCameraPermission: () -> Unit,
    isFlashEnabled: Boolean,
    onToggleFlash: () -> Unit,
    lensFacing: Int,
    onFlipCamera: () -> Unit,
    onCaptureReady: (ImageCapture) -> Unit,
    onCameraReady: (Camera) -> Unit,
    onTriggerShutter: () -> Unit,
    onPickGallery: () -> Unit,
    onUseSystemCamera: () -> Unit,
    capturedBitmap: Bitmap?,
    onClearCapturedPhoto: () -> Unit,
    onScanCrop: (Bitmap) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .verticalScroll(scrollState)
    ) {
        // Banner informing farmer of automatic identification
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = AgPrimaryGreen.copy(alpha = 0.12f),
            border = BorderStroke(1.dp, AgPrimaryGreen.copy(alpha = 0.3f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = AgPrimaryGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Single Photo Universal Scanner — No crop selection needed. AI automatically detects the crop and health condition.",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 15.sp
                )
            }
        }

        // IF PHOTO IS ALREADY CAPTURED -> SHOW IMAGE PREVIEW WITH "SCAN CROP" BUTTON
        if (capturedBitmap != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black),
                border = BorderStroke(1.5.dp, AgPrimaryGreen)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        bitmap = capturedBitmap.asImageBitmap(),
                        contentDescription = "Captured Crop Image Preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Top Badge: Photo Captured
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.75f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF00E676),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Photo Captured • Ready to Scan",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Retake icon top right
                    IconButton(
                        onClick = onClearCapturedPhoto,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Retake Photo",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Primary "Scan Crop" Button
            Button(
                onClick = { onScanCrop(capturedBitmap) },
                modifier = Modifier
                    .testTag("scan_crop_button")
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Scan Crop with PRITHVI AI",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onClearCapturedPhoto,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Retake / Capture Another Photo", fontSize = 12.sp)
            }
        } else {
            // CAMERA PREVIEW & CAPTURE CONTROLS
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (hasCameraPermission) {
                        // CameraX Live Viewfinder Surface
                        UniversalCameraXPreview(
                            modifier = Modifier.fillMaxSize(),
                            lensFacing = lensFacing,
                            onImageCaptureReady = onCaptureReady,
                            onCameraBound = onCameraReady
                        )

                        // Target Reticle Overlay
                        UniversalScannerHudOverlay()

                        // Controls Overlay (Top)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                                .align(Alignment.TopCenter),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.6f)
                            ) {
                                IconButton(onClick = onToggleFlash, modifier = Modifier.size(38.dp)) {
                                    Icon(
                                        imageVector = if (isFlashEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                                        contentDescription = "Toggle Flashlight",
                                        tint = if (isFlashEnabled) Color(0xFFFFD54F) else Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.Black.copy(alpha = 0.65f),
                                border = BorderStroke(0.8.dp, AgPrimaryGreen.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF00E676))
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "LIVE CAMERA FEED",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            Surface(
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.6f)
                            ) {
                                IconButton(onClick = onFlipCamera, modifier = Modifier.size(38.dp)) {
                                    Icon(
                                        imageVector = Icons.Default.Cameraswitch,
                                        contentDescription = "Flip Camera",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        // Shutter Bar Overlay (Bottom of Viewfinder)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp, start = 20.dp, end = 20.dp)
                                .align(Alignment.BottomCenter),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Upload Photo Button
                            Surface(
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.65f),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
                                modifier = Modifier
                                    .testTag("upload_photo_button")
                                    .clickable { onPickGallery() }
                            ) {
                                Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Image,
                                        contentDescription = "Upload Photo from Gallery",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            // Primary Shutter "Take Photo" Button
                            Box(
                                modifier = Modifier
                                    .testTag("take_photo_button")
                                    .size(74.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.3f))
                                    .clickable { onTriggerShutter() },
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(AgPrimaryGreen),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = "Take Photo",
                                        tint = Color.White,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }

                            // System Camera Fallback Button
                            Surface(
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.65f),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
                                modifier = Modifier
                                    .testTag("system_camera_button")
                                    .clickable { onUseSystemCamera() }
                            ) {
                                Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.PhotoCamera,
                                        contentDescription = "System Camera",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        // Camera Permission Request View
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = AgPrimaryGreen.copy(alpha = 0.2f),
                                modifier = Modifier.size(64.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.PhotoCamera,
                                        contentDescription = null,
                                        tint = AgPrimaryGreen,
                                        modifier = Modifier.size(34.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Camera Access Required",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "PRITHVI AI needs camera access to inspect leaf textures, lesion margins, spore patterns, and crop symptoms in real time.",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = onRequestCameraPermission,
                                colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.height(44.dp)
                            ) {
                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Grant Camera Permission", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedButton(
                                onClick = onPickGallery,
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
                            ) {
                                Icon(imageVector = Icons.Default.Image, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Upload Photo Instead", color = Color.White, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons Row: "Take Photo" & "Upload Photo"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onUseSystemCamera,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen)
                ) {
                    Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Take Photo", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onPickGallery,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.2.dp, AgPrimaryGreen)
                ) {
                    Icon(imageVector = Icons.Default.Image, contentDescription = null, tint = AgPrimaryGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Upload Photo", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AgPrimaryGreen)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Guidelines & Best Practices Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            border = BorderStroke(0.6.dp, AgPrimaryGreen.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = AgPrimaryGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tips for Accurate Crop Scanning",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "• Hold the camera 15–20 cm away from the leaf or plant part.\n• Capture both the affected lesion/spot and adjacent healthy green tissue.\n• Use the flash/torch toggle in low light or morning shade.\n• AI supports rice, wheat, corn, tomato, potato, cotton, mustard, pulses, fruits, and all general crops automatically.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// ==========================================
// CAMERA X LIVE PREVIEW COMPONENT
// ==========================================
@Composable
fun UniversalCameraXPreview(
    modifier: Modifier = Modifier,
    lensFacing: Int,
    onImageCaptureReady: (ImageCapture) -> Unit,
    onCameraBound: (Camera) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }

            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val imageCapture = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()

                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(lensFacing)
                        .build()

                    cameraProvider.unbindAll()
                    val camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )

                    onImageCaptureReady(imageCapture)
                    onCameraBound(camera)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )
}

// ==========================================
// SCANNER HUD / RETICLE OVERLAY
// ==========================================
@Composable
fun UniversalScannerHudOverlay() {
    val infiniteTransition = rememberInfiniteTransition(label = "scanner_laser")
    val laserOffset by infiniteTransition.animateFloat(
        initialValue = -80f,
        targetValue = 80f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_y"
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(230.dp)
                .border(2.dp, AgPrimaryGreen.copy(alpha = 0.8f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(2.dp)
                    .offset(y = laserOffset.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                Color(0xFF00E676),
                                Color.White,
                                Color(0xFF00E676),
                                Color.Transparent
                            )
                        )
                    )
            )

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.Black.copy(alpha = 0.6f),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Point camera at any crop or leaf",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }
    }
}

// ==========================================
// DIAGNOSTIC REPORT VIEW
// ==========================================
@Composable
fun UniversalDiagnosticReportTab(
    latestScan: CropScanEntity,
    isAnalyzing: Boolean,
    capturedBitmap: Bitmap?,
    onRetake: () -> Unit,
    onShareToCommunity: () -> Unit,
    onNavigateToChat: (() -> Unit)?,
    onReAnalyze: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .verticalScroll(scrollState)
    ) {
        if (isAnalyzing) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, AgPrimaryGreen)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = AgPrimaryGreen,
                        strokeWidth = 3.5.dp,
                        modifier = Modifier.size(52.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "PRITHVI AI Universal Scanner Analyzing...",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = AgPrimaryGreen
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Checking image clarity, identifying crop species, and assessing plant health against global agricultural standards.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(4.dp),
                        color = AgPrimaryGreen
                    )
                }
            }
        } else if (latestScan.isQualityIssue) {
            // IMAGE QUALITY CHECK FAILURE
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.5.dp, AgWarningOrange)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = CircleShape,
                        color = AgWarningOrange.copy(alpha = 0.15f),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = AgWarningOrange,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (latestScan.healthStatus == "Analysis unavailable") "AI Service Unavailable" else "Image Quality Notice",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = latestScan.qualityMessage.ifBlank {
                            "Please take a closer, well-lit photo of the affected leaf or plant."
                        },
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = onRetake,
                        colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(46.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (latestScan.healthStatus == "Analysis unavailable") "Try Again" else "Capture Clearer Photo", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        } else {
            // VALID SCAN RESULT
            // Top Preview Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (capturedBitmap != null) {
                        Image(
                            bitmap = capturedBitmap.asImageBitmap(),
                            contentDescription = "Analyzed Crop Leaf",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = androidx.compose.ui.res.painterResource(id = R.drawable.img_app_icon),
                            contentDescription = "Sample Leaf",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Confidence badge
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.75f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF00E676),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (latestScan.confidencePercent > 0) "${latestScan.confidencePercent}% Match" else "Awaiting scan",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Bottom Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                                )
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${latestScan.cropName} • ${latestScan.plantPart}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Button(
                            onClick = onRetake,
                            modifier = Modifier.height(32.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Scan Again", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 1. 🌱 CROP DETECTED CARD
            UniversalFarmerCard(
                title = "🌱 Crop Detected",
                accentColor = AgPrimaryGreen
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = latestScan.cropName,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Plant Part: ${latestScan.plantPart}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (latestScan.healthStatus.contains("healthy", ignoreCase = true)) {
                            AgPrimaryGreen.copy(alpha = 0.15f)
                        } else {
                            AgWarningOrange.copy(alpha = 0.15f)
                        }
                    ) {
                        Text(
                            text = latestScan.healthStatus,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (latestScan.healthStatus.contains("healthy", ignoreCase = true)) {
                                AgPrimaryGreen
                            } else {
                                AgWarningOrange
                            },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 2. 🩺 POSSIBLE PROBLEM CARD
            UniversalFarmerCard(
                title = "🩺 Possible Problem",
                accentColor = if (latestScan.isSevere) AgWarningOrange else MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = latestScan.problemName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Preliminary visual diagnosis. Always cross-examine multiple leaves across the plot.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3. 📊 CONFIDENCE CARD
            UniversalFarmerCard(
                title = "📊 Confidence",
                accentColor = Color(0xFF0288D1)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (latestScan.confidencePercent > 0) "${latestScan.confidencePercent}% Match" else "Not available",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = when {
                            latestScan.confidencePercent >= 85 -> "High confidence"
                            latestScan.confidencePercent in 1..84 -> "Limited confidence"
                            else -> "Not assessed"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { latestScan.confidencePercent / 100f },
                    modifier = Modifier.fillMaxWidth().height(6.dp),
                    color = AgPrimaryGreen
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4. 🔎 SYMPTOMS CARD
            UniversalFarmerCard(
                title = "🔎 Symptoms",
                accentColor = Color(0xFF7B1FA2)
            ) {
                Text(
                    text = latestScan.symptoms,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 5. 🔬 LIKELY CAUSE CARD
            UniversalFarmerCard(
                title = "🔬 Likely Cause",
                accentColor = Color(0xFFE65100)
            ) {
                Text(
                    text = latestScan.possibleCauses,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 6. 🚨 IMMEDIATE ACTION CARD
            UniversalFarmerCard(
                title = "🚨 Immediate Action",
                accentColor = Color(0xFFC2185B)
            ) {
                Text(
                    text = latestScan.immediateAction.ifBlank {
                        "1. Inspect adjacent plants in the field.\n2. Avoid overhead irrigation to minimize leaf wetness."
                    },
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 7. 💊 RECOMMENDED ACTION / TREATMENT CARD
            UniversalFarmerCard(
                title = "💊 Recommended Action",
                accentColor = AgPrimaryGreen
            ) {
                Text(
                    text = latestScan.recommendedSteps,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = AgPrimaryGreen.copy(alpha = 0.1f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Safety Note: Always check product label directions and consult local regulations before applying chemical sprays.",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 8. 🛡️ PREVENTION CARD
            UniversalFarmerCard(
                title = "🛡️ Prevention",
                accentColor = AgLeafGreen
            ) {
                Text(
                    text = latestScan.prevention,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 9. 👨‍🌾 WHEN TO SEEK EXPERT HELP
            UniversalFarmerCard(
                title = "👨‍🌾 When to Seek Expert Help",
                accentColor = Color(0xFF00897B)
            ) {
                Text(
                    text = latestScan.whenToSeekExpert.ifBlank {
                        "If symptoms spread to more than 15-20% of your crop area or cause rapid wilting, consult your local Krishi Vigyan Kendra (KVK) or extension officer immediately."
                    },
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (onNavigateToChat != null) {
                    Button(
                        onClick = onNavigateToChat,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen)
                    ) {
                        Icon(imageVector = Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Ask Agronomist", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedButton(
                    onClick = onShareToCommunity,
                    modifier = Modifier
                        .testTag("share_scan_community_button")
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, AgPrimaryGreen)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = AgPrimaryGreen, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share Diagnosis", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AgPrimaryGreen)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==========================================
// FARMER-FRIENDLY RESULT CARD CONTAINER
// ==========================================
@Composable
fun UniversalFarmerCard(
    title: String,
    accentColor: Color,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(6.dp))
            content()
        }
    }
}

// ==========================================
// SCAN HISTORY TAB
// ==========================================
@Composable
fun UniversalScanHistoryTab(
    scans: List<CropScanEntity>,
    onSelectScan: (CropScanEntity) -> Unit,
    onStartNewScan: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }

    if (scans.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = AgPrimaryGreen.copy(alpha = 0.15f),
                modifier = Modifier.size(60.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = AgPrimaryGreen,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "No Previous Scans Yet",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Scanned crops and disease reports will be stored here for future reference and comparison.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onStartNewScan,
                colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Start First Scan")
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(scans) { scan ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectScan(scan) },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(0.8.dp, AgPrimaryGreen.copy(alpha = 0.3f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = AgPrimaryGreen.copy(alpha = 0.15f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Spa,
                                    contentDescription = null,
                                    tint = AgPrimaryGreen,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = scan.cropName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = scan.problemName,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                            Text(
                                text = dateFormat.format(Date(scan.timestamp)),
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = AgPrimaryGreen.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = if (scan.confidencePercent > 0) "${scan.confidencePercent}%" else "—",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgPrimaryGreen,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
