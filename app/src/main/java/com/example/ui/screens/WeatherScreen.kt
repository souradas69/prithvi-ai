package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.WeatherData
import com.example.ui.components.WeatherDashboardWidget
import com.example.ui.theme.AgContainerGreen
import com.example.ui.theme.AgDarkGreen
import com.example.ui.theme.AgPrimaryGreen
import com.example.ui.theme.AgSproutGreen
import com.example.ui.theme.AgWarningOrange

@Composable
fun WeatherScreen(
    weather: WeatherData,
    cropName: String,
    isWeatherLoading: Boolean = false,
    onRefreshWeather: () -> Unit = {},
    isDarkMode: Boolean = false,
    modifier: Modifier = Modifier
) {
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
                text = "Agri-Weather & Real-Time Local Forecast",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AgDarkGreen
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            ) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = AgPrimaryGreen, modifier = Modifier.size(14.dp))
                Text(
                    text = weather.locationName,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            // Severe Warning Alert Banner if any
            if (weather.severeAlert != null) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Thunderstorm, contentDescription = null, tint = Color.Red, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = weather.severeAlert, fontSize = 12.sp, color = Color(0xFFC62828), fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Real-Time Weather Dashboard Widget
            WeatherDashboardWidget(
                weather = weather,
                cropName = cropName,
                isRefreshing = isWeatherLoading,
                onRefresh = onRefreshWeather,
                onViewFullForecast = {},
                isDarkMode = isDarkMode,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // AI Weather & Crop Impact Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AgContainerGreen)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = AgPrimaryGreen, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "PRITHVI Weather Advice for $cropName", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AgPrimaryGreen)
                        Text(text = weather.aiInsight, fontSize = 13.sp, color = AgDarkGreen, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 7-Day Forecast Title
            Text(
                text = "7-Day Farm Forecast",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AgDarkGreen,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (weather.forecast7Days.isEmpty()) {
                Text("No verified forecast is available. Refresh later or consult your local meteorological service.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            weather.forecast7Days.forEach { day ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = day.dayName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AgDarkGreen, modifier = Modifier.width(60.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Opacity, contentDescription = null, tint = AgPrimaryGreen, modifier = Modifier.size(14.dp))
                            Text(text = "${day.rainPercent}%", fontSize = 12.sp, color = AgPrimaryGreen, modifier = Modifier.padding(start = 2.dp))
                        }
                        Text(text = day.condition, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "${day.maxTempC}° / ${day.minTempC}°C", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AgDarkGreen)
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherDetailTile(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(top = 2.dp))
    }
}
