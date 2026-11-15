package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AgContainerGreen
import com.example.ui.theme.AgDarkGreen
import com.example.ui.theme.AgLeafGreen
import com.example.ui.theme.AgPrimaryGreen
import com.example.ui.theme.AgSproutGreen

private val INDIAN_LANGUAGES = listOf("English")
val INDIAN_STATES = listOf("West Bengal", "Uttar Pradesh", "Bihar", "Punjab", "Haryana", "Maharashtra", "Gujarat", "Tamil Nadu", "Andhra Pradesh", "Karnataka", "Madhya Pradesh", "Rajasthan", "Odisha", "Kerala", "Assam")

val CROP_LIST = listOf("Tomato", "Potato", "Wheat", "Rice / Paddy", "Cotton", "Sugarcane", "Onion", "Maize", "Chilli", "Soybean", "Mustard", "Brinjal")
val SOIL_TYPES = listOf("Alluvial / Loam", "Black Soil (Regur)", "Red & Yellow Soil", "Laterite Soil", "Sandy Loam", "Clayey Soil")
val IRRIGATION_METHODS = listOf("Drip Irrigation", "Canal Irrigation", "Borewell / Tube Well", "Sprinkler", "Rainfed")

val FARMING_GOALS = listOf(
    "Better crop growth",
    "Higher productivity",
    "Disease prevention",
    "Pest management",
    "Irrigation & Water",
    "Soil health",
    "Fertilizer guidance",
    "Reduce farming costs",
    "Find better markets",
    "Understand crop prices",
    "Government schemes",
    "Weather planning"
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    onOnboardingComplete: (
        name: String,
        phone: String,
        language: String,
        state: String,
        district: String,
        village: String,
        farmName: String,
        farmArea: Double,
        unit: String,
        soilType: String,
        irrigationMethod: String,
        cropName: String,
        cropVariety: String,
        growthStage: String,
        goals: String
    ) -> Unit
) {
    var step by remember { mutableStateOf(0) } // 0: Welcome, 1: Profile, 2: Farm, 3: Field & Crop, 4: Goals

    // Step 1: Profile
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("English") }
    var state by remember { mutableStateOf("West Bengal") }
    var district by remember { mutableStateOf("Hooghly") }
    var village by remember { mutableStateOf("Singur") }

    // Step 2: Farm
    var farmName by remember { mutableStateOf("My Green Farm") }
    var farmAreaText by remember { mutableStateOf("2.0") }
    var unit by remember { mutableStateOf("Acres") }
    var soilType by remember { mutableStateOf("Alluvial / Loam") }
    var irrigationMethod by remember { mutableStateOf("Drip Irrigation") }

    // Step 3: Field & Crop
    var cropName by remember { mutableStateOf("Tomato") }
    var cropVariety by remember { mutableStateOf("Sona PK") }
    var growthStage by remember { mutableStateOf("Vegetative") }

    // Step 4: Goals
    val selectedGoals = remember { mutableStateListOf("Better crop growth", "Disease prevention", "Irrigation & Water", "Find better markets") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header Progress
            if (step > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { if (step > 1) step-- else step = 0 },
                        modifier = Modifier.testTag("onboarding_back_button")
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                        Text(
                            text = "Step $step of 4",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgPrimaryGreen
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { step / 4f },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                            color = AgSproutGreen,
                            trackColor = AgContainerGreen
                        )
                    }
                    Text(
                        text = "Skip",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .testTag("onboarding_skip_button")
                            .clickable {
                                onOnboardingComplete(
                                    name.ifBlank { "Farmer" },
                                    phone.ifBlank { "9876543210" },
                                    language,
                                    state,
                                    district,
                                    village,
                                    farmName,
                                    farmAreaText.toDoubleOrNull() ?: 2.0,
                                    unit,
                                    soilType,
                                    irrigationMethod,
                                    cropName,
                                    cropVariety,
                                    growthStage,
                                    selectedGoals.joinToString(",")
                                )
                            }
                            .padding(8.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (step) {
                    0 -> {
                        // Refreshed First-Time Login Interface matching reference image
                        PrithviLoginScreen(
                            onPerformLogin = { enteredPhone, selectedLang ->
                                phone = enteredPhone
                                language = selectedLang
                                onOnboardingComplete(
                                    name.ifBlank { "Ramesh Kumar" },
                                    phone,
                                    language,
                                    state,
                                    district,
                                    village,
                                    farmName,
                                    farmAreaText.toDoubleOrNull() ?: 2.0,
                                    unit,
                                    soilType,
                                    irrigationMethod,
                                    cropName,
                                    cropVariety,
                                    growthStage,
                                    selectedGoals.joinToString(",")
                                )
                            },
                            onProceedToSetup = { enteredPhone, selectedLang ->
                                phone = enteredPhone
                                language = selectedLang
                                step = 1
                            }
                        )
                    }

                    1 -> {
                        // Farmer Profile Step
                        Text(
                            text = "Tell us about yourself",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgDarkGreen,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "This helps PRITHVI personalize crop & weather recommendations for your exact location.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 16.dp)
                        )

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Farmer Full Name") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            modifier = Modifier.testTag("input_farmer_name").fillMaxWidth().padding(bottom = 12.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Mobile Number") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.testTag("input_farmer_phone").fillMaxWidth().padding(bottom = 12.dp),
                            singleLine = true
                        )

                        Text("Interface language: English", fontSize = 12.sp, color = AgDarkGreen, modifier = Modifier.padding(bottom = 14.dp))

                        // State & District
                        OutlinedTextField(
                            value = state,
                            onValueChange = { state = it },
                            label = { Text("State") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                            modifier = Modifier.testTag("input_farmer_state").fillMaxWidth().padding(bottom = 12.dp),
                            singleLine = true
                        )

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = district,
                                onValueChange = { district = it },
                                label = { Text("District") },
                                modifier = Modifier.testTag("input_farmer_district").weight(1f).padding(end = 6.dp),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = village,
                                onValueChange = { village = it },
                                label = { Text("Village / Locality") },
                                modifier = Modifier.testTag("input_farmer_village").weight(1f).padding(start = 6.dp),
                                singleLine = true
                            )
                        }
                    }

                    2 -> {
                        // Farm Setup Step
                        Text(
                            text = "Tell us about your farm",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgDarkGreen,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "Specify farm size, soil, and water irrigation details.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 16.dp)
                        )

                        OutlinedTextField(
                            value = farmName,
                            onValueChange = { farmName = it },
                            label = { Text("Farm Name") },
                            modifier = Modifier.testTag("input_farm_name").fillMaxWidth().padding(bottom = 12.dp),
                            singleLine = true
                        )

                        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                            OutlinedTextField(
                                value = farmAreaText,
                                onValueChange = { farmAreaText = it },
                                label = { Text("Farm Area") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.testTag("input_farm_area").weight(1f).padding(end = 6.dp),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = unit,
                                onValueChange = { unit = it },
                                label = { Text("Unit (Acres/Bigha)") },
                                modifier = Modifier.testTag("input_farm_unit").weight(1f).padding(start = 6.dp),
                                singleLine = true
                            )
                        }

                        Text("Soil Type", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                        ) {
                            SOIL_TYPES.forEach { soil ->
                                val selected = soilType == soil
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (selected) AgPrimaryGreen else AgContainerGreen)
                                        .clickable { soilType = soil }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = soil,
                                        color = if (selected) Color.White else AgDarkGreen,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }

                        Text("Irrigation Method", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                        ) {
                            IRRIGATION_METHODS.forEach { method ->
                                val selected = irrigationMethod == method
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (selected) AgPrimaryGreen else AgContainerGreen)
                                        .clickable { irrigationMethod = method }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = method,
                                        color = if (selected) Color.White else AgDarkGreen,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }

                    3 -> {
                        // Field & Crop Setup
                        Text(
                            text = "What are you growing?",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgDarkGreen,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "Select your current primary crop and growth stage.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 16.dp)
                        )

                        Text("Select Crop", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                        ) {
                            CROP_LIST.forEach { crop ->
                                val selected = cropName == crop
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (selected) AgPrimaryGreen else AgContainerGreen)
                                        .clickable { cropName = crop }
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = crop,
                                        color = if (selected) Color.White else AgDarkGreen,
                                        fontSize = 13.sp,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = cropVariety,
                            onValueChange = { cropVariety = it },
                            label = { Text("Crop Variety (Optional)") },
                            modifier = Modifier.testTag("input_crop_variety").fillMaxWidth().padding(bottom = 12.dp),
                            singleLine = true
                        )

                        Text("Current Growth Stage", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                        ) {
                            listOf("Germination", "Vegetative", "Flowering", "Fruiting", "Harvesting").forEach { stage ->
                                val selected = growthStage == stage
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (selected) AgDarkGreen else AgContainerGreen)
                                        .clickable { growthStage = stage }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = stage,
                                        color = if (selected) Color.White else AgDarkGreen,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }

                    4 -> {
                        // Goals Step
                        Text(
                            text = "What would you like PRITHVI to help you with?",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgDarkGreen,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "Select all that apply to customize your daily dashboard.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 16.dp)
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                        ) {
                            FARMING_GOALS.forEach { goal ->
                                val isSelected = selectedGoals.contains(goal)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(if (isSelected) AgPrimaryGreen else MaterialTheme.colorScheme.surface)
                                        .border(
                                            1.dp,
                                            if (isSelected) AgPrimaryGreen else AgContainerGreen,
                                            RoundedCornerShape(16.dp)
                                        )
                                        .clickable {
                                            if (isSelected) selectedGoals.remove(goal) else selectedGoals.add(goal)
                                        }
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp).padding(end = 4.dp)
                                            )
                                        }
                                        Text(
                                            text = goal,
                                            color = if (isSelected) Color.White else AgDarkGreen,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Navigation Action
            if (step > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        if (step < 4) {
                            step++
                        } else {
                            onOnboardingComplete(
                                name.ifBlank { "Farmer" },
                                phone.ifBlank { "9876543210" },
                                language,
                                state,
                                district,
                                village,
                                farmName,
                                farmAreaText.toDoubleOrNull() ?: 2.0,
                                unit,
                                soilType,
                                irrigationMethod,
                                cropName,
                                cropVariety,
                                growthStage,
                                selectedGoals.joinToString(",")
                            )
                        }
                    },
                    modifier = Modifier
                        .testTag("onboarding_next_button")
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen)
                ) {
                    Text(
                        text = if (step == 4) "Save Profile & Launch PRITHVI AI" else "Continue",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (step == 4) Icons.Default.CheckCircle else Icons.Default.ArrowForward,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

/**
 * Re-designed First Login Interface matching the user's reference image
 */
@Composable
fun PrithviLoginScreen(
    onPerformLogin: (phone: String, language: String) -> Unit,
    onProceedToSetup: (phone: String, language: String) -> Unit
) {
    var mobileNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("English") }
    var isLanguageDropdownExpanded by remember { mutableStateOf(false) }
    var isCreateAccountMode by remember { mutableStateOf(false) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWide = maxWidth >= 700.dp

        if (isWide) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Hero Banner
                Box(
                    modifier = Modifier
                        .weight(0.48f)
                        .fillMaxHeight()
                ) {
                    LoginLeftBanner()
                }

                // Right Login Form Card
                Box(
                    modifier = Modifier
                        .weight(0.52f)
                ) {
                    LoginFormCard(
                        mobileNumber = mobileNumber,
                        onMobileNumberChange = { mobileNumber = it },
                        password = password,
                        onPasswordChange = { password = it },
                        isPasswordVisible = isPasswordVisible,
                        onTogglePasswordVisible = { isPasswordVisible = !isPasswordVisible },
                        selectedLanguage = selectedLanguage,
                        isLanguageDropdownExpanded = isLanguageDropdownExpanded,
                        onToggleLanguageDropdown = { isLanguageDropdownExpanded = !isLanguageDropdownExpanded },
                        onSelectLanguage = {
                            selectedLanguage = it
                            isLanguageDropdownExpanded = false
                        },
                        isCreateAccountMode = isCreateAccountMode,
                        onToggleCreateAccountMode = { isCreateAccountMode = !isCreateAccountMode },
                        onPerformLogin = onPerformLogin,
                        onProceedToSetup = onProceedToSetup
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoginLeftBanner()

                Spacer(modifier = Modifier.height(12.dp))

                LoginFormCard(
                    mobileNumber = mobileNumber,
                    onMobileNumberChange = { mobileNumber = it },
                    password = password,
                    onPasswordChange = { password = it },
                    isPasswordVisible = isPasswordVisible,
                    onTogglePasswordVisible = { isPasswordVisible = !isPasswordVisible },
                    selectedLanguage = selectedLanguage,
                    isLanguageDropdownExpanded = isLanguageDropdownExpanded,
                    onToggleLanguageDropdown = { isLanguageDropdownExpanded = !isLanguageDropdownExpanded },
                    onSelectLanguage = {
                        selectedLanguage = it
                        isLanguageDropdownExpanded = false
                    },
                    isCreateAccountMode = isCreateAccountMode,
                    onToggleCreateAccountMode = { isCreateAccountMode = !isCreateAccountMode },
                    onPerformLogin = onPerformLogin,
                    onProceedToSetup = onProceedToSetup
                )
            }
        }
    }
}

@Composable
fun LoginLeftBanner() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFE3EFE5))
            .padding(16.dp)
    ) {
        // Tagline
        Text(
            text = "Smart Farming.\nBetter Crops.\nStronger Farmers.",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF0C381E),
            lineHeight = 26.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Farmer Photo Image
        Image(
            painter = painterResource(id = R.drawable.img_login_farmer),
            contentDescription = "Farmer Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(20.dp))
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Curved Feature List Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0A3C1D))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // 3x2 Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    FeatureIconCell(icon = Icons.Default.Psychology, label = "AI Advisory")
                    FeatureIconCell(icon = Icons.Default.Cloud, label = "Weather Updates")
                    FeatureIconCell(icon = Icons.Default.ShowChart, label = "Market Prices")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    FeatureIconCell(icon = Icons.Default.LocalFlorist, label = "Disease Detection")
                    FeatureIconCell(icon = Icons.Default.AccountBalance, label = "Government Schemes")
                    FeatureIconCell(icon = Icons.Default.People, label = "Farmer Community")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "“ PRITHVI AI is like a digital partner for every Indian Farmer. ”",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic,
                    color = AgSproutGreen,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun FeatureIconCell(icon: ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(88.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 12.sp
        )
    }
}

@Composable
fun LoginFormCard(
    mobileNumber: String,
    onMobileNumberChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onTogglePasswordVisible: () -> Unit,
    selectedLanguage: String,
    isLanguageDropdownExpanded: Boolean,
    onToggleLanguageDropdown: () -> Unit,
    onSelectLanguage: (String) -> Unit,
    isCreateAccountMode: Boolean,
    onToggleCreateAccountMode: () -> Unit,
    onPerformLogin: (phone: String, language: String) -> Unit,
    onProceedToSetup: (phone: String, language: String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(1.dp, Color(0xFFE2EFE4))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text("ENGLISH", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF477B48), letterSpacing = 1.1.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Logo & Title Header
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_app_icon),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "PRITHVI",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0C381E),
                    letterSpacing = 1.sp
                )
                Text(
                    text = "FARMER AI",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20),
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(modifier = Modifier.width(24.dp).height(1.dp).background(Color.Gray.copy(alpha = 0.35f)))
                    Text(
                        text = " Your Smart Farming Assistant ",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    Box(modifier = Modifier.width(24.dp).height(1.dp).background(Color.Gray.copy(alpha = 0.35f)))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Welcome Form Head
            Text(
                text = if (isCreateAccountMode) "Create Account" else "Welcome Back!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
            Text(
                text = if (isCreateAccountMode) "Join PRITHVI AI to empower your farm dashboard" else "Login to continue to your farm dashboard",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
            )

            // Mobile Field
            OutlinedTextField(
                value = mobileNumber,
                onValueChange = onMobileNumberChange,
                placeholder = { Text("Mobile Number", fontSize = 13.sp, color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = Color(0xFF0C381E)
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0C381E),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color(0xFFFAFDFB),
                    unfocusedContainerColor = Color(0xFFFAFDFB)
                ),
                modifier = Modifier.fillMaxWidth().testTag("login_mobile_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                placeholder = { Text("Password", fontSize = 13.sp, color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color(0xFF0C381E)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = onTogglePasswordVisible) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Password",
                            tint = Color.Gray
                        )
                    }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0C381E),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color(0xFFFAFDFB),
                    unfocusedContainerColor = Color(0xFFFAFDFB)
                ),
                modifier = Modifier.fillMaxWidth().testTag("login_password_input")
            )

            if (!isCreateAccountMode) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Forgot Password?",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0C381E),
                        modifier = Modifier.clickable { }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Action Button
            Button(
                onClick = {
                    val validPhone = mobileNumber.ifBlank { "9876543210" }
                    if (isCreateAccountMode) {
                        onProceedToSetup(validPhone, selectedLanguage)
                    } else {
                        onPerformLogin(validPhone, selectedLanguage)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("login_submit_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C381E))
            ) {
                Text(
                    text = if (isCreateAccountMode) "Continue Setup" else "Login",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // OR Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0xFFE0E0E0)))
                Text(
                    text = " OR ",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0xFFE0E0E0)))
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Login with OTP
            OutlinedButton(
                onClick = {
                    val validPhone = mobileNumber.ifBlank { "9876543210" }
                    onPerformLogin(validPhone, selectedLanguage)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("login_otp_button"),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color(0xFF0C381E))
            ) {
                Icon(
                    imageVector = Icons.Default.Smartphone,
                    contentDescription = null,
                    tint = Color(0xFF0C381E),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Login with OTP",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0C381E)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Continue with Google
            OutlinedButton(
                onClick = {
                    onPerformLogin("9876543210", selectedLanguage)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("login_google_button"),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEA4335)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("G", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Continue with Google",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF374151)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isCreateAccountMode) "Already have an account? " else "New to PRITHVI AI? ",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = if (isCreateAccountMode) "Login Here" else "Create New Account",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0C381E),
                    modifier = Modifier.clickable { onToggleCreateAccountMode() }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Privacy & Security Pill
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF5EC))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF1B5E20),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Your data is safe and secure with us.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0C381E)
                )
                Text(
                    text = "We respect your privacy.",
                    fontSize = 11.sp,
                    color = Color(0xFF2E7D32)
                )
            }
        }
    }
}
