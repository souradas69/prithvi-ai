package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.style.TextAlign
import com.example.R
import com.example.data.i18n.tr
import com.example.data.entity.FarmerProfileEntity
import com.example.data.entity.FieldEntity
import com.example.data.repository.MarketRepository
import com.example.data.repository.WeatherData
import com.example.data.repository.WeatherRepository
import com.example.ui.components.WeatherDashboardWidget
import com.example.ui.theme.AgContainerGreen
import com.example.ui.theme.AgDarkGreen
import com.example.ui.theme.AgLeafGreen
import com.example.ui.theme.AgPrimaryGreen
import com.example.ui.theme.AgSoftYellow
import com.example.ui.theme.AgSproutGreen
import com.example.ui.theme.AgWarningOrange

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.ui.components.FarmingBackground

@Composable
fun HomeScreen(
    profile: FarmerProfileEntity?,
    fields: List<FieldEntity>,
    weatherData: WeatherData? = null,
    isWeatherLoading: Boolean = false,
    onRefreshWeather: () -> Unit = {},
    onNavigateToScan: () -> Unit,
    onNavigateToIrrigation: () -> Unit,
    onNavigateToMarket: () -> Unit,
    onNavigateToSchemes: () -> Unit,
    onNavigateToChat: () -> Unit,
    onNavigateToAdvisory: () -> Unit,
    onNavigateToWeather: () -> Unit = {},
    onNavigateToCommunity: () -> Unit = {},
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {},
    onOpenAccountCentre: () -> Unit = {},
    onOpenVoiceQuery: () -> Unit = onNavigateToChat,
    modifier: Modifier = Modifier
) {
    val farmerName = profile?.name ?: "Farmer"
    val location = if (profile != null) "${profile.village}, ${profile.district}" else "Hooghly, West Bengal"
    val state = profile?.state ?: "West Bengal"

    val activeField = fields.firstOrNull()
    val cropName = activeField?.cropName ?: "No crop selected"
    val cropArea = activeField?.area?.let { "$it Acres" } ?: "Area not recorded"
    val cropStage = activeField?.growthStage ?: "Not specified"

    val weather = weatherData ?: WeatherRepository.getWeatherDataForLocation(state, profile?.district ?: "Hooghly", profile?.village ?: "Singur", cropName)

    val bannerBrush = if (isDarkMode) {
        Brush.linearGradient(listOf(Color(0xFF142E1F), Color(0xFF0D2115)))
    } else {
        Brush.linearGradient(listOf(AgPrimaryGreen, AgLeafGreen))
    }

    var showNotificationsDialog by remember { mutableStateOf(false) }
    var unreadNotificationCount by remember { mutableStateOf(0) }
    var showWhatShouldIDoDialog by remember { mutableStateOf(false) }

    FarmingBackground(isDarkMode = isDarkMode) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onOpenAccountCentre() }
                        .padding(vertical = 4.dp, horizontal = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(AgPrimaryGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_icon),
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Welcome,",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = farmerName,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = AgPrimaryGreen,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = location,
                                fontSize = 11.sp,
                                color = AgPrimaryGreen,
                                modifier = Modifier.padding(start = 2.dp)
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Corner Account Centre Button
                    Surface(
                        onClick = onOpenAccountCentre,
                        shape = RoundedCornerShape(20.dp),
                        color = if (isDarkMode) Color(0xFF1E3A27) else Color(0xFFE8F5E9),
                        border = BorderStroke(1.dp, AgPrimaryGreen),
                        modifier = Modifier.testTag("account_centre_top_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ManageAccounts,
                                contentDescription = "Account Centre",
                                tint = AgPrimaryGreen,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Account",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgPrimaryGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(2.dp))

                    // Corner Notifications Bell Button with Badge
                    IconButton(
                        onClick = {
                            showNotificationsDialog = true
                            unreadNotificationCount = 0
                        },
                        modifier = Modifier.testTag("notification_bell_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadNotificationCount > 0) {
                                    Badge(
                                        containerColor = Color.Red,
                                        contentColor = Color.White
                                    ) {
                                        Text("$unreadNotificationCount", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Farmer Notifications & Reminders",
                                tint = AgPrimaryGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Dark / Light Theme Toggle Switch Icon
                    IconButton(
                        onClick = onToggleDarkMode,
                        modifier = Modifier.testTag("theme_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode",
                            tint = if (isDarkMode) AgSoftYellow else AgPrimaryGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Farm Action Icons under Ramesh Kumar / Account Centre Header
            Text(
                text = "⚡ QUICK FARM ACTIONS",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AgPrimaryGreen,
                letterSpacing = 0.8.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            ) {
                item {
                    QuickIconChip(
                        title = "Voice Query",
                        subtitle = "Speak to PRITHVI",
                        icon = Icons.Default.Mic,
                        badgeText = "🎤 Speak",
                        gradientColors = listOf(Color(0xFF059669), Color(0xFF047857)),
                        tag = "quick_voice_query",
                        onClick = onOpenVoiceQuery
                    )
                }
                item {
                    QuickIconChip(
                        title = "Irrigation",
                        subtitle = "Smart schedule",
                        icon = Icons.Default.WaterDrop,
                        badgeText = "💧 Smart",
                        gradientColors = listOf(Color(0xFF06B6D4), Color(0xFF0284C7)),
                        tag = "quick_irrigation",
                        onClick = onNavigateToIrrigation
                    )
                }
                item {
                    QuickIconChip(
                        title = "Scan Crop",
                        subtitle = "Detect diseases",
                        icon = Icons.Default.QrCodeScanner,
                        badgeText = "⚡ AI Scan",
                        gradientColors = listOf(Color(0xFF10B981), Color(0xFF059669)),
                        tag = "quick_scan",
                        onClick = onNavigateToScan
                    )
                }
                item {
                    QuickIconChip(
                        title = "Market Prices",
                        subtitle = "Mandi rates",
                        icon = Icons.Default.Storefront,
                        badgeText = "📈 Mandi",
                        gradientColors = listOf(Color(0xFFF97316), Color(0xFFEA580C)),
                        tag = "quick_market",
                        onClick = onNavigateToMarket
                    )
                }
                item {
                    QuickIconChip(
                        title = "Govt Schemes",
                        subtitle = "PM-KISAN etc",
                        icon = Icons.Default.AccountBalance,
                        badgeText = "🏛️ Govt",
                        gradientColors = listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9)),
                        tag = "quick_schemes",
                        onClick = onNavigateToSchemes
                    )
                }
                item {
                    QuickIconChip(
                        title = "Weather",
                        subtitle = "Local forecast",
                        icon = Icons.Default.Cloud,
                        badgeText = "☀️ 28°C",
                        gradientColors = listOf(Color(0xFF38BDF8), Color(0xFF0284C7)),
                        tag = "quick_weather",
                        onClick = onNavigateToWeather
                    )
                }
                item {
                    QuickIconChip(
                        title = "Ask PRITHVI",
                        subtitle = "24/7 AI Chat",
                        icon = Icons.Default.AutoAwesome,
                        badgeText = "✨ 24/7 AI",
                        gradientColors = listOf(Color(0xFFEC4899), Color(0xFF8B5CF6)),
                        tag = "quick_ai_chat",
                        onClick = onNavigateToChat
                    )
                }
                item {
                    QuickIconChip(
                        title = "Community",
                        subtitle = "Farmer forum",
                        icon = Icons.Default.Agriculture,
                        badgeText = "🤝 Chat",
                        gradientColors = listOf(Color(0xFF34D399), Color(0xFF059669)),
                        tag = "quick_community",
                        onClick = onNavigateToCommunity
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Prominent Accessible Voice Assistant Banner for Farmers
            Card(
                onClick = onOpenVoiceQuery,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .testTag("home_accessible_voice_query_banner"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkMode) Color(0xFF163824) else Color(0xFFE8F5E9)
                ),
                border = BorderStroke(1.2.dp, AgPrimaryGreen)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(AgPrimaryGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice Assistant Microphone",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Voice Assistant",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkMode) Color.White else AgDarkGreen
                            )
                        }
                        Text(
                            text = "Tap to speak your question in English",
                            fontSize = 11.5.sp,
                            color = if (isDarkMode) Color(0xFFA7D7B5) else Color(0xFF2E7D32)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = AgPrimaryGreen,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // TODAY'S FARM SECTION
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🌱 TODAY'S FARM",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Prioritized farm intelligence & real-time conditions",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AgPrimaryGreen.copy(alpha = 0.12f),
                    border = BorderStroke(0.8.dp, AgPrimaryGreen.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(AgPrimaryGreen)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Live & Estimated",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgPrimaryGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Main Hero Card Banner (Bright gradient in Light Mode, deep dark green in Dark Mode)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(bannerBrush)
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Active Crop Plot Status",
                                fontSize = 13.sp,
                                color = AgSproutGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "$cropName • $cropArea",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Growth stage: $cropStage",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.85f),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        Image(
                            painter = painterResource(id = R.drawable.img_hero_farmer),
                            contentDescription = "Farm image",
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 4 Metrics Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricTile("Soil Moisture", "Not connected", "No sensor data", Icons.Default.Opacity, Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(6.dp))
                        MetricTile("Weather", if (weather.isRealtime) "${weather.tempC}°C" else "—", if (weather.isRealtime) "Live forecast" else "Unavailable", Icons.Default.Cloud, Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(6.dp))
                        MetricTile("Irrigation", "Advisory", "Check field manually", Icons.Default.WaterDrop, Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(6.dp))
                        MetricTile("Pest Risk", "Not assessed", "Use crop scanner", Icons.Default.BugReport, Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // WHAT SHOULD I DO TODAY? - Highly Visible Primary AI Action Button
            Surface(
                onClick = { showWhatShouldIDoDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("what_should_i_do_today_button"),
                shape = RoundedCornerShape(18.dp),
                color = if (isDarkMode) Color(0xFF1B382B) else Color(0xFFE8F5E9),
                border = BorderStroke(1.5.dp, AgPrimaryGreen),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(AgPrimaryGreen, AgLeafGreen))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🌱 What should I do today?",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = CircleShape,
                                color = AgPrimaryGreen
                            ) {
                                Text(
                                    text = "AI ACTION",
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Tap for prioritized recommendations tailored to your $cropName crop",
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "View Priorities",
                        tint = AgPrimaryGreen,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // PRIORITY INTELLIGENCE FEED (High / Medium / Low)
            Text(
                text = "⚡ TODAY'S ACTION PRIORITIES",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AgPrimaryGreen,
                letterSpacing = 0.8.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth().clickable { onNavigateToIrrigation() },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.WaterDrop, contentDescription = null, tint = AgPrimaryGreen, modifier = Modifier.size(24.dp))
                    Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                        Text("Review irrigation needs", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text("Check soil by hand and consider crop stage and local rainfall before watering. No sensor reading is connected.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = AgPrimaryGreen)
                }
            }
            if (!weather.isRealtime) {
                Text("Live weather is unavailable right now. Refresh or check a local forecast before making weather-dependent decisions.", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 9.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))

            // REAL-TIME LOCAL WEATHER DASHBOARD WIDGET
            WeatherDashboardWidget(
                weather = weather,
                cropName = cropName,
                isRefreshing = isWeatherLoading,
                onRefresh = onRefreshWeather,
                onViewFullForecast = onNavigateToWeather,
                isDarkMode = isDarkMode,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // "What does my farm need today?" Section
            Text(
                text = "What does my farm need today?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // PRITHVI AI Priority Action Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToAdvisory() }
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AgPrimaryGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "PRITHVI Recommendation",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgPrimaryGreen
                        )
                        Text(
                            text = weather.aiInsight,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = AgPrimaryGreen
                    )
                }
            }

            // Quick Action Items
            ActionRow("Open irrigation planner", Icons.Default.WaterDrop, "Water", onNavigateToIrrigation)
            ActionRow("Scan leaves for pest/fungal symptoms", Icons.Default.QrCodeScanner, "Scan", onNavigateToScan)
            ActionRow("Check verified mandi prices", Icons.Default.Storefront, "Market", onNavigateToMarket)
            ActionRow("Explore government schemes", Icons.Default.AccountBalance, "Schemes", onNavigateToSchemes)

            Spacer(modifier = Modifier.height(16.dp))

            // AI Floating Chat Trigger Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onNavigateToChat() },
                colors = CardDefaults.cardColors(containerColor = if (isDarkMode) Color(0xFF142E1F) else AgPrimaryGreen)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = AgSproutGreen,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Ask PRITHVI AI Anything",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Instant advice on pest treatment, soil nutrients, or market selling tips.",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = AgSproutGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }

        if (showNotificationsDialog) {
            NotificationCenterDialog(
                onDismiss = { showNotificationsDialog = false },
                onNavigateToIrrigation = onNavigateToIrrigation,
                onNavigateToScan = onNavigateToScan,
                onNavigateToSchemes = onNavigateToSchemes,
                onNavigateToWeather = onNavigateToWeather
            )
        }

        if (showWhatShouldIDoDialog) {
            WhatShouldIDoTodayDialog(
                cropName = cropName,
                soilMoisture = 0,
                tempC = if (weather.isRealtime) weather.tempC else 0,
                humidity = if (weather.isRealtime) weather.humidityPercent else 0,
                rainProb = if (weather.isRealtime) weather.rainProbabilityPercent else 0,
                mandiName = "Market data unavailable",
                mandiPrice = 0,
                onDismiss = { showWhatShouldIDoDialog = false },
                onNavigateToIrrigation = {
                    showWhatShouldIDoDialog = false
                    onNavigateToIrrigation()
                },
                onNavigateToScan = {
                    showWhatShouldIDoDialog = false
                    onNavigateToScan()
                },
                onNavigateToWeather = {
                    showWhatShouldIDoDialog = false
                    onNavigateToWeather()
                },
                onNavigateToMarket = {
                    showWhatShouldIDoDialog = false
                    onNavigateToMarket()
                },
                onNavigateToChat = {
                    showWhatShouldIDoDialog = false
                    onNavigateToChat()
                }
            )
        }
    }
}

@Composable
fun MetricTile(title: String, val1: String, val2: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.15f))
            .padding(vertical = 8.dp, horizontal = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = title,
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.8f),
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Text(
                text = val1,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 2.dp),
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Text(
                text = val2,
                fontSize = 9.5.sp,
                color = AgSproutGreen,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun ActionRow(title: String, icon: ImageVector, tag: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .testTag("home_action_$tag")
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(AgContainerGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = AgPrimaryGreen, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun QuickIconChip(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badgeText: String? = null,
    gradientColors: List<Color> = listOf(AgPrimaryGreen, AgLeafGreen),
    tag: String,
    onClick: () -> Unit
) {
    val primaryGradientColor = gradientColors.firstOrNull() ?: AgPrimaryGreen

    Surface(
        onClick = onClick,
        modifier = Modifier
            .testTag(tag)
            .width(102.dp),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.5.dp, Brush.linearGradient(listOf(primaryGradientColor.copy(alpha = 0.6f), primaryGradientColor.copy(alpha = 0.2f)))),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                // Vibrant Gradient Icon Circle
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(gradientColors)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            if (badgeText != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = CircleShape,
                    color = primaryGradientColor.copy(alpha = 0.15f),
                    border = BorderStroke(0.8.dp, primaryGradientColor.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = primaryGradientColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.5.dp),
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                textAlign = TextAlign.Center
            )

            Text(
                text = subtitle,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun NotificationCenterDialog(
    onDismiss: () -> Unit,
    onNavigateToIrrigation: () -> Unit,
    onNavigateToScan: () -> Unit,
    onNavigateToSchemes: () -> Unit,
    onNavigateToWeather: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Notifications & reminders", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("There are no verified alerts to show right now.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                Text("PRITHVI will show reminders when they are generated from your saved farm activities or connected, verified data sources. It will not invent sensor, weather, market or scheme alerts.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp))
            }
        },
        confirmButton = { Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen)) { Text("Close", color = Color.White) } }
    )
}

@Composable
fun NotificationItemCard(
    title: String,
    desc: String,
    time: String,
    actionLabel: String,
    onClickAction: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(0.8.dp, AgPrimaryGreen.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(text = time, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = desc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 15.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Surface(
                    onClick = onClickAction,
                    shape = RoundedCornerShape(8.dp),
                    color = AgPrimaryGreen.copy(alpha = 0.15f),
                    border = BorderStroke(0.8.dp, AgPrimaryGreen)
                ) {
                    Text(
                        text = actionLabel,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgPrimaryGreen,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PriorityFeedCard(
    priority: String,
    priorityColor: Color,
    icon: ImageVector,
    title: String,
    description: String,
    actionLabel: String,
    onClick: () -> Unit,
    isDarkMode: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) Color(0xFF1B2E23) else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, priorityColor.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(priorityColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = priorityColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = priorityColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = priority,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = priorityColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                fontSize = 11.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    onClick = onClick,
                    shape = RoundedCornerShape(8.dp),
                    color = AgPrimaryGreen.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, AgPrimaryGreen)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = actionLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgPrimaryGreen
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = AgPrimaryGreen,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WhatShouldIDoTodayDialog(
    cropName: String,
    soilMoisture: Int,
    tempC: Int,
    humidity: Int,
    rainProb: Int,
    mandiName: String,
    mandiPrice: Int,
    onDismiss: () -> Unit,
    onNavigateToIrrigation: () -> Unit,
    onNavigateToScan: () -> Unit,
    onNavigateToWeather: () -> Unit,
    onNavigateToMarket: () -> Unit,
    onNavigateToChat: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Your farm checklist", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
                Text("For your $cropName crop, start with observations rather than assumed sensor or market values.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(12.dp))
                listOf(
                    "Check soil moisture manually at root depth before deciding whether to irrigate.",
                    "Inspect a few plants for new symptoms and use the crop scanner if you find anything unusual.",
                    "Check a current local weather forecast before spraying, irrigating or scheduling field work.",
                    "Confirm today's mandi price from an official market source before deciding when and where to sell."
                ).forEachIndexed { index, item ->
                    Row(modifier = Modifier.padding(vertical = 6.dp), verticalAlignment = Alignment.Top) {
                        Text("${index + 1}.", color = AgPrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(item, modifier = Modifier.padding(start = 8.dp), fontSize = 11.sp, lineHeight = 16.sp)
                    }
                }
                Spacer(Modifier.height(10.dp))
                Surface(onClick = onNavigateToChat, shape = RoundedCornerShape(12.dp), color = AgDarkGreen, modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Mic, contentDescription = null, tint = AgSproutGreen, modifier = Modifier.size(22.dp))
                        Text("  Ask PRITHVI about your crop", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Text("No soil sensor readings or live mandi prices are connected in this version.", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 10.dp))
            }
        },
        confirmButton = { Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen)) { Text("Close", color = Color.White) } }
    )
}

@Composable
fun TaskPriorityCard(
    number: String,
    priority: String,
    priorityColor: Color,
    task: String,
    why: String,
    actionText: String,
    onAction: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, priorityColor.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(priorityColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = number,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = task,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = priorityColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = priority,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = priorityColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Why: $why",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 15.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Surface(
                    onClick = onAction,
                    shape = RoundedCornerShape(8.dp),
                    color = AgPrimaryGreen.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, AgPrimaryGreen)
                ) {
                    Text(
                        text = actionText,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgPrimaryGreen,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}


