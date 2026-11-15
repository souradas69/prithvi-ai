package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.MandiPriceInfo
import com.example.data.repository.MarketIntelligence
import com.example.data.repository.MarketRepository
import com.example.ui.theme.AgContainerGreen
import com.example.ui.theme.AgDarkGreen
import com.example.ui.theme.AgPrimaryGreen
import com.example.ui.theme.AgSproutGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MarketScreen(
    currentCrop: String,
    state: String,
    district: String,
    modifier: Modifier = Modifier
) {
    var selectedCrop by remember { mutableStateOf(currentCrop.ifBlank { "Tomato" }) }
    val marketData = MarketRepository.getMarketData(selectedCrop, state, district)

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Mandi Market Information",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AgDarkGreen
            )
            Text(
                text = "${marketData.lastUpdated} • Source: ${marketData.sourceName}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
            )

            // Crop Selector Chips
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                listOf("Tomato", "Potato", "Wheat", "Rice", "Cotton", "Onion", "Chilli").forEach { crop ->
                    val isSelected = selectedCrop.equals(crop, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isSelected) AgPrimaryGreen else AgContainerGreen)
                            .clickable { selectedCrop = crop }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = crop,
                            color = if (isSelected) Color.White else AgDarkGreen,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            // AI Market Strategy Recommendation
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AgDarkGreen)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = AgSproutGreen, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "PRITHVI Market Recommendation", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AgSproutGreen)
                        Text(text = marketData.aiRecommendation, fontSize = 13.sp, color = Color.White, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }

            // Mandi Prices List
            Text(
                text = "Nearby Mandis for $selectedCrop",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AgDarkGreen,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            if (marketData.prices.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = AgPrimaryGreen, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Verified prices are not available yet", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AgDarkGreen)
                        Text("We will not display estimated or sample mandi prices as live market data. Connect an approved Agmarknet or e-NAM source to enable this section.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 5.dp))
                    }
                }
            } else {
                marketData.prices.forEach { mandi ->
                    MandiPriceCard(mandi)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
fun MandiPriceCard(mandi: MandiPriceInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = mandi.mandiName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AgDarkGreen)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = AgPrimaryGreen, modifier = Modifier.size(12.dp))
                        Text(text = "${mandi.distanceKm} km away", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val (icon, color) = when (mandi.trend) {
                        "UP" -> Icons.Default.TrendingUp to AgPrimaryGreen
                        "DOWN" -> Icons.Default.TrendingDown to Color.Red
                        else -> Icons.Default.TrendingFlat to Color.Gray
                    }
                    Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = mandi.trend, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(AgContainerGreen)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Market Price", fontSize = 11.sp, color = AgDarkGreen)
                    Text(text = "₹${mandi.pricePerQuintal} / quintal", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AgPrimaryGreen)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Min / Max Range", fontSize = 11.sp, color = AgDarkGreen)
                    Text(text = "₹${mandi.minPrice} - ₹${mandi.maxPrice}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AgDarkGreen)
                }
            }
        }
    }
}
