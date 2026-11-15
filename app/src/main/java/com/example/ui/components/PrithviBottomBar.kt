package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Storefront
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.i18n.tr
import com.example.ui.theme.AgDarkGreen
import com.example.ui.theme.AgSproutGreen

enum class BottomTab(val route: String, val label: String, val icon: ImageVector) {
    HOME("home", "Home", Icons.Default.Home),
    MY_FARM("my_farm", "My Farm", Icons.Default.Agriculture),
    PRITHVI_AI("prithvi_ai", "PRITHVI AI", Icons.Default.AutoAwesome),
    MARKET("market", "Market", Icons.Default.Storefront),
    COMMUNITY("community", "Community", Icons.Default.People)
}

@Composable
fun PrithviBottomBar(
    currentTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 12.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomTab.values().forEach { tab ->
                val isSelected = currentTab == tab
                val isCenterAi = tab == BottomTab.PRITHVI_AI

                if (isCenterAi) {
                    // Visually prominent center AI button
                    Box(
                        modifier = Modifier
                            .testTag("nav_prithvi_ai_center")
                            .clip(CircleShape)
                            .background(AgDarkGreen)
                            .clickable { onTabSelected(tab) }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = "PRITHVI AI",
                                tint = AgSproutGreen,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "AI",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }
                    }
                } else {
                    val localizedLabel = tab.label
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .testTag("nav_${tab.route}")
                            .clip(CircleShape)
                            .clickable { onTabSelected(tab) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = localizedLabel,
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = localizedLabel,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
