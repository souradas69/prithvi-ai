package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.auth.AuthUser
import com.example.data.entity.FarmerProfileEntity
import com.example.data.entity.FieldEntity
import com.example.data.i18n.LanguageRegistry
import com.example.data.i18n.PrithviI18n
import com.example.data.i18n.tr
import com.example.ui.screens.INDIAN_STATES
import com.example.ui.theme.AgDarkGreen
import com.example.ui.theme.AgPrimaryGreen
import com.example.ui.theme.AgSproutGreen

val CROPS_LIST = listOf("Tomato", "Paddy (Rice)", "Wheat", "Potato", "Cotton", "Chili", "Maize", "Onion", "Sugarcane", "Soybean", "Mustard", "Banana")
val CROP_STAGES = listOf("Sowing / Planting", "Vegetative", "Flowering", "Fruiting / Grain Formation", "Harvesting")
val SOIL_TYPES = listOf("Alluvial Soil", "Black Soil", "Red & Yellow Soil", "Laterite Soil", "Sandy Loam", "Clay Loam")
val IRRIGATION_METHODS = listOf("Drip Irrigation", "Sprinkler", "Flood Irrigation", "Rainfed")

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AccountCentreDialog(
    profile: FarmerProfileEntity?,
    activeField: FieldEntity?,
    onDismiss: () -> Unit,
    onSaveAccountInfo: (
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
        irrigationMethod: String
    ) -> Unit,
    onSwitchAccount: () -> Unit,
    onLanguageChanged: (String) -> Unit = {},
    authUser: AuthUser? = null,
    isFirebaseConfigured: Boolean = false,
    onSignOut: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    // State for Name, Phone, Language
    var name by remember(profile) { mutableStateOf(profile?.name ?: "Ramesh Kumar") }
    var phone by remember(profile) { mutableStateOf(profile?.phone ?: "9876543210") }
    var selectedLanguage by remember(profile) { mutableStateOf("English") }
    var langExpanded by remember { mutableStateOf(false) }

    // Additional Preferred Languages for AI
    var preferredLanguages by remember(profile) {
        mutableStateOf(listOf("English"))
    }

    // State for Location / Place
    var selectedState by remember(profile) { mutableStateOf(profile?.state ?: "West Bengal") }
    var stateExpanded by remember { mutableStateOf(false) }
    var district by remember(profile) { mutableStateOf(profile?.district ?: "Hooghly") }
    var village by remember(profile) { mutableStateOf(profile?.village ?: "Singur") }

    // State for Field & Crop
    var cropName by remember(activeField) { mutableStateOf(activeField?.cropName ?: "Tomato") }
    var cropExpanded by remember { mutableStateOf(false) }
    var cropVariety by remember(activeField) { mutableStateOf(activeField?.cropVariety ?: "Hybrid Arka Rakshak") }
    var growthStage by remember(activeField) { mutableStateOf(activeField?.growthStage ?: "Vegetative") }
    var stageExpanded by remember { mutableStateOf(false) }
    var fieldAreaText by remember(activeField) { mutableStateOf((activeField?.area ?: 2.0).toString()) }
    var soilType by remember(activeField) { mutableStateOf(activeField?.soilInfo ?: "Alluvial Soil") }
    var soilExpanded by remember { mutableStateOf(false) }
    var irrigationMethod by remember(activeField) { mutableStateOf(activeField?.irrigationMethod ?: "Drip Irrigation") }
    var irrExpanded by remember { mutableStateOf(false) }

    var saveSuccessMessage by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(AgDarkGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = AgSproutGreen,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "ACCOUNT CENTRE",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AgDarkGreen,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "PRITHVI AI Farmer Profile & Settings",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("account_centre_close")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Account Centre",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Current Profile Summary Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0C381E))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_app_icon),
                                contentDescription = "Farmer Photo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "📱 +91 $phone • 🌐 $selectedLanguage",
                                fontSize = 11.sp,
                                color = AgSproutGreen
                            )
                            Text(
                                text = "🌾 $cropName ($growthStage) • 📍 $village, $district",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Success Toast/Banner
                saveSuccessMessage?.let { msg ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFD1E7DD),
                        border = BorderStroke(1.dp, Color(0xFFA3CFBB))
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color(0xFF0F5132),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = msg,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F5132)
                            )
                        }
                    }
                }

                // Tabs for editing sections
                val tabTitles = listOf("👤 Personal", "📍 Location", "🌾 Crop", "💾 Saved Data", "🔐 Account")

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = AgDarkGreen,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = AgPrimaryGreen,
                            height = 3.dp
                        )
                    }
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    when (selectedTab) {
                        0 -> {
                            // PERSONAL DETAILS & MULTILINGUAL SYSTEM
                            Text(
                                text = "Personal Details & Language Preference",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgDarkGreen,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            // Name Field
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Farmer Full Name") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = AgDarkGreen) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("account_center_name_input")
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Phone Field
                            OutlinedTextField(
                                value = phone,
                                onValueChange = { phone = it },
                                label = { Text("Mobile Phone Number") },
                                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = AgDarkGreen) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("account_center_phone_input")
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Removed language selection as per instructions

                        }

                        1 -> {
                            // LOCATION / PLACE DETAILS
                            Text(
                                text = "Farm Location & Region",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgDarkGreen,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            // State Selector
                            ExposedDropdownMenuBox(
                                expanded = stateExpanded,
                                onExpandedChange = { stateExpanded = !stateExpanded }
                            ) {
                                OutlinedTextField(
                                    value = selectedState,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("State / UT") },
                                    leadingIcon = { Icon(Icons.Default.Place, contentDescription = null, tint = AgDarkGreen) },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = stateExpanded) },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                )
                                ExposedDropdownMenu(
                                    expanded = stateExpanded,
                                    onDismissRequest = { stateExpanded = false }
                                ) {
                                    INDIAN_STATES.forEach { st ->
                                        DropdownMenuItem(
                                            text = { Text(st) },
                                            onClick = {
                                                selectedState = st
                                                stateExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // District
                            OutlinedTextField(
                                value = district,
                                onValueChange = { district = it },
                                label = { Text("District") },
                                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = AgDarkGreen) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("account_center_district_input")
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Village / Tehsil
                            OutlinedTextField(
                                value = village,
                                onValueChange = { village = it },
                                label = { Text("Village / Gram Panchayat / Tehsil") },
                                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = AgDarkGreen) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("account_center_village_input")
                            )
                        }

                        2 -> {
                            // FIELD & CROP DETAILS
                            Text(
                                text = "Active Crop & Field Information",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgDarkGreen,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            // Crop Name Selector
                            ExposedDropdownMenuBox(
                                expanded = cropExpanded,
                                onExpandedChange = { cropExpanded = !cropExpanded }
                            ) {
                                OutlinedTextField(
                                    value = cropName,
                                    onValueChange = { cropName = it },
                                    label = { Text("Primary Crop Name") },
                                    leadingIcon = { Icon(Icons.Default.Agriculture, contentDescription = null, tint = AgDarkGreen) },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cropExpanded) },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                )
                                ExposedDropdownMenu(
                                    expanded = cropExpanded,
                                    onDismissRequest = { cropExpanded = false }
                                ) {
                                    CROPS_LIST.forEach { crp ->
                                        DropdownMenuItem(
                                            text = { Text(crp) },
                                            onClick = {
                                                cropName = crp
                                                cropExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Crop Variety
                            OutlinedTextField(
                                value = cropVariety,
                                onValueChange = { cropVariety = it },
                                label = { Text("Crop Variety / Seed Type") },
                                leadingIcon = { Icon(Icons.Default.Psychology, contentDescription = null, tint = AgDarkGreen) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Growth Stage
                            ExposedDropdownMenuBox(
                                expanded = stageExpanded,
                                onExpandedChange = { stageExpanded = !stageExpanded }
                            ) {
                                OutlinedTextField(
                                    value = growthStage,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Current Growth Stage") },
                                    leadingIcon = { Icon(Icons.Default.RotateRight, contentDescription = null, tint = AgDarkGreen) },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = stageExpanded) },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                )
                                ExposedDropdownMenu(
                                    expanded = stageExpanded,
                                    onDismissRequest = { stageExpanded = false }
                                ) {
                                    CROP_STAGES.forEach { stg ->
                                        DropdownMenuItem(
                                            text = { Text(stg) },
                                            onClick = {
                                                growthStage = stg
                                                stageExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Land Area
                            OutlinedTextField(
                                value = fieldAreaText,
                                onValueChange = { fieldAreaText = it },
                                label = { Text("Land Area (Acres)") },
                                leadingIcon = { Icon(Icons.Default.Agriculture, contentDescription = null, tint = AgDarkGreen) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Soil Type
                            ExposedDropdownMenuBox(
                                expanded = soilExpanded,
                                onExpandedChange = { soilExpanded = !soilExpanded }
                            ) {
                                OutlinedTextField(
                                    value = soilType,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Soil Type") },
                                    leadingIcon = { Icon(Icons.Default.Science, contentDescription = null, tint = AgDarkGreen) },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = soilExpanded) },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                )
                                ExposedDropdownMenu(
                                    expanded = soilExpanded,
                                    onDismissRequest = { soilExpanded = false }
                                ) {
                                    SOIL_TYPES.forEach { sl ->
                                        DropdownMenuItem(
                                            text = { Text(sl) },
                                            onClick = {
                                                soilType = sl
                                                soilExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Irrigation Method
                            ExposedDropdownMenuBox(
                                expanded = irrExpanded,
                                onExpandedChange = { irrExpanded = !irrExpanded }
                            ) {
                                OutlinedTextField(
                                    value = irrigationMethod,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Irrigation Method") },
                                    leadingIcon = { Icon(Icons.Default.WaterDrop, contentDescription = null, tint = AgDarkGreen) },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = irrExpanded) },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                )
                                ExposedDropdownMenu(
                                    expanded = irrExpanded,
                                    onDismissRequest = { irrExpanded = false }
                                ) {
                                    IRRIGATION_METHODS.forEach { ir ->
                                        DropdownMenuItem(
                                            text = { Text(ir) },
                                            onClick = {
                                                irrigationMethod = ir
                                                irrExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        3 -> {
                            // DEDICATED PERSISTENT SAVED PROFILE & DATA SECTION
                            Text(
                                text = "💾 Saved Profile & Synchronized Data",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgDarkGreen,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            Text(
                                text = "Review your farmer identity and all verified agricultural parameters saved in PRITHVI AI:",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            // Card 1: Identity & Credentials
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF6FAF7)),
                                border = BorderStroke(1.dp, Color(0xFFCDE2D1))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("IDENTITY & CREDENTIALS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AgPrimaryGreen)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    SavedDataRow("Full Name", name)
                                    SavedDataRow("Phone Number", "+91 $phone")
                                    SavedDataRow("Account Email", authUser?.email ?: profile?.email?.ifBlank { "Unregistered" } ?: "Not set")
                                    SavedDataRow("User ID (UID)", authUser?.uid ?: profile?.uid?.ifBlank { "Local-farmer-01" } ?: "Local-farmer-01")
                                    SavedDataRow("Sync Mode", if (isFirebaseConfigured) "☁️ Firebase Cloud Firestore" else "💾 Local Room DB (Encrypted)")
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Card 2: Language & Multi-Dialect Profile
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF6FAF7)),
                                border = BorderStroke(1.dp, Color(0xFFCDE2D1))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("INTERFACE LANGUAGE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AgPrimaryGreen)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    SavedDataRow("Application language", "English")
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Card 3: Agricultural Parameters
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF6FAF7)),
                                border = BorderStroke(1.dp, Color(0xFFCDE2D1))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("FARM & CROP PARAMETERS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AgPrimaryGreen)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    SavedDataRow("State & District", "$selectedState, $district")
                                    SavedDataRow("Village / Tehsil", village)
                                    SavedDataRow("Active Crop", "$cropName ($cropVariety)")
                                    SavedDataRow("Growth Stage", growthStage)
                                    SavedDataRow("Cultivated Area", "$fieldAreaText Acres")
                                    SavedDataRow("Soil Type", soilType)
                                    SavedDataRow("Irrigation Method", irrigationMethod)
                                }
                            }
                        }

                        4 -> {
                            // SWITCH ACCOUNT / AUTH MANAGEMENT
                            Text(
                                text = "Account Management & Authentication",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgDarkGreen,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, AgPrimaryGreen),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F9F4))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(AgDarkGreen),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("✓", color = Color.White, fontWeight = FontWeight.Bold)
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = if (authUser != null) "Logged in with ${if (isFirebaseConfigured) "Firebase Auth" else "Persistent Profile"}" else "Active Farmer Account",
                                                fontSize = 11.sp,
                                                color = AgPrimaryGreen,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "$name (+91 $phone)",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = AgDarkGreen
                                            )
                                            if (authUser?.email?.isNotBlank() == true) {
                                                Text(
                                                    text = "📧 ${authUser.email}",
                                                    fontSize = 11.sp,
                                                    color = AgDarkGreen
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Location: $village, $district, $selectedState",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Primary Crop: $cropName ($growthStage)",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    onDismiss()
                                    onSwitchAccount()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("account_center_switch_login_button"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C381E))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SwapHoriz,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Log In with Another Phone / Email",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedButton(
                                onClick = {
                                    onSignOut()
                                    onDismiss()
                                    onSwitchAccount()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("account_center_logout_button"),
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, Color(0xFFD32F2F))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Logout,
                                    contentDescription = null,
                                    tint = Color(0xFFD32F2F)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Log Out of Current Session",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD32F2F)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom Action Bar (Save / Close)
                if (selectedTab != 4) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Cancel")
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Button(
                            onClick = {
                                val areaVal = fieldAreaText.toDoubleOrNull() ?: 2.0
                                onSaveAccountInfo(
                                    name.ifBlank { "Ramesh Kumar" },
                                    phone.ifBlank { "9876543210" },
                                    selectedLanguage,
                                    selectedState,
                                    district.ifBlank { "Hooghly" },
                                    village.ifBlank { "Singur" },
                                    cropName,
                                    cropVariety.ifBlank { "Hybrid" },
                                    growthStage,
                                    areaVal,
                                    soilType,
                                    irrigationMethod
                                )
                                onLanguageChanged(selectedLanguage)
                                saveSuccessMessage = "Account, place and field details updated successfully!"
                            },
                            modifier = Modifier.testTag("account_center_save_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AgDarkGreen)
                        ) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save Changes", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedDataRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AgDarkGreen)
    }
}
