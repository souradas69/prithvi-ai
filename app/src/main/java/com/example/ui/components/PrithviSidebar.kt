package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.i18n.tr
import com.example.ui.theme.AgDarkGreen
import com.example.ui.theme.AgSproutGreen

import androidx.compose.material.icons.filled.ManageAccounts

enum class SidebarItem(val id: String, val label: String, val icon: ImageVector) {
    HOME("home", "Home", Icons.Default.Home),
    MY_FARM("my_farm", "My Farm", Icons.Default.Agriculture),
    ACCOUNT_CENTRE("account_centre", "Account Centre", Icons.Default.ManageAccounts),
    CROP_ADVISORY("crop_advisory", "Crop Advisory", Icons.Default.Psychology),
    SCAN_CROP("scan_crop", "Scan My Crop", Icons.Default.QrCodeScanner),
    WEATHER("weather", "Weather", Icons.Default.Cloud),
    IRRIGATION("irrigation", "Irrigation Planner", Icons.Default.WaterDrop),
    MARKET("market", "Market Prices", Icons.Default.Storefront),
    GOVT_SCHEMES("govt_schemes", "Government Schemes", Icons.Default.AccountBalance),
    SOIL_HEALTH("soil_health", "Soil Health", Icons.Default.Science),
    FARM_FINANCE("farm_finance", "Farm Finance", Icons.Default.MonetizationOn),
    CALENDAR("calendar", "Farming Calendar", Icons.Default.CalendarMonth),
    COMMUNITY("community", "Farmer Community", Icons.Default.People),
    PRITHVI_AI("prithvi_ai", "PRITHVI AI Assistant", Icons.Default.AutoAwesome),
    SETTINGS("settings", "Settings & Profile", Icons.Default.Settings)
}

@Composable
fun PrithviSidebar(
    selectedItem: SidebarItem,
    onItemSelected: (SidebarItem) -> Unit,
    farmerName: String,
    farmerLocation: String,
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(260.dp)
            .fillMaxHeight(),
        color = AgDarkGreen,
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // App Branding
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Agriculture,
                    contentDescription = null,
                    tint = AgSproutGreen,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "PRITHVI AI",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Smart Farming Assistant",
                        fontSize = 11.sp,
                        color = AgSproutGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sidebar Items
            SidebarItem.values().forEach { item ->
                val isSelected = selectedItem == item
                val localizedLabel = item.label
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) AgSproutGreen.copy(alpha = 0.2f) else Color.Transparent)
                        .clickable { onItemSelected(item) }
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = localizedLabel,
                        tint = if (isSelected) AgSproutGreen else Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = localizedLabel,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) AgSproutGreen else Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dark/Light Theme Switch Button in Sidebar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.1f))
                    .clickable { onToggleDarkMode() }
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Toggle Theme",
                    tint = AgSproutGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = if (isDarkMode) "Light Mode" else "Dark Mode",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}
