package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.WeatherData
import com.example.ui.theme.AgContainerGreen
import com.example.ui.theme.AgDarkGreen
import com.example.ui.theme.AgLeafGreen
import com.example.ui.theme.AgPrimaryGreen
import com.example.ui.theme.AgSoftYellow
import com.example.ui.theme.AgSproutGreen
import com.example.ui.theme.AgWarningOrange

@Composable
fun WeatherDashboardWidget(
    weather: WeatherData,
    cropName: String = "Crop",
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    onViewFullForecast: () -> Unit = {},
    isDarkMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    var expandedPlanning by remember { mutableStateOf(true) }

    val widgetBg = if (isDarkMode) {
        Brush.linearGradient(listOf(Color(0xFF132A1D), Color(0xFF0C1B12)))
    } else {
        Brush.linearGradient(listOf(Color(0xFF0F3822), Color(0xFF1B5E20)))
    }

    Card(
        modifier = modifier
            .testTag("weather_dashboard_widget")
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(widgetBg)
                .padding(16.dp)
        ) {
            Column {
                // Widget Header: Location & Live Status Tag
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = AgSproutGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = weather.locationName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (weather.isRealtime) Color(0xFF2E7D32) else Color(0xFFE65100),
                                    modifier = Modifier.padding(top = 2.dp)
                                ) {
                                    Text(
                                        text = if (weather.isRealtime) "LIVE WEATHER API" else "WEATHER UNAVAILABLE",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Updated: ${weather.lastUpdatedTime}",
                                    fontSize = 10.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    // Live Refresh Action Button
                    IconButton(
                        onClick = onRefresh,
                        enabled = !isRefreshing,
                        modifier = Modifier
                            .testTag("refresh_weather_button")
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                    ) {
                        if (isRefreshing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = AgSproutGreen,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh Weather",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Real-time Temperature & Humidity Hero Box
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Temperature Column
                    Column {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = if (weather.isRealtime) "${weather.tempC}°C" else "—",
                                fontSize = 38.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = if (weather.isRealtime) " / ${weather.tempF}°F" else "",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Icon(
                                imageVector = if (weather.condition.contains("Sunny", ignoreCase = true)) Icons.Default.WbSunny else Icons.Default.Cloud,
                                contentDescription = null,
                                tint = AgSproutGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (weather.isRealtime) weather.condition else "No live weather data",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AgSproutGreen
                            )
                        }
                    }

                    // CRITICAL AGRI METRIC: HUMIDITY HIGHLIGHT CARD
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                        modifier = Modifier.width(155.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Opacity,
                                    contentDescription = "Humidity",
                                    tint = Color(0xFF38BDF8),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "HUMIDITY",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White.copy(alpha = 0.8f),
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Text(
                                text = if (weather.isRealtime) "${weather.humidityPercent}% RH" else "—",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )

                            // Color-coded Humidity Progress Bar
                            val humFraction = if (weather.isRealtime) (weather.humidityPercent / 100f).coerceIn(0f, 1f) else 0f
                            val humColor = when {
                                weather.humidityPercent > 75 -> Color(0xFFEF4444) // Fungal risk
                                weather.humidityPercent in 50..75 -> Color(0xFF22C55E) // Ideal
                                else -> Color(0xFFF59E0B) // Low RH
                            }

                            LinearProgressIndicator(
                                progress = { humFraction },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(5.dp)
                                    .clip(CircleShape),
                                color = humColor,
                                trackColor = Color.White.copy(alpha = 0.2f)
                            )

                            Text(
                                text = if (!weather.isRealtime) "No verified humidity reading" else when {
                                    weather.humidityPercent > 75 -> "⚠️ High Spore Risk"
                                    weather.humidityPercent in 50..75 -> "✅ Ideal Moisture"
                                    else -> "🌵 High Evaporation"
                                },
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = humColor,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Secondary Metrics Row: Rain, Wind, UV, Pressure
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.1f))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricMiniBadge("Rain Prob", if (weather.isRealtime) "${weather.rainProbabilityPercent}%" else "—", Icons.Default.WaterDrop)
                    MetricMiniBadge("Wind Speed", if (weather.isRealtime) "${weather.windSpeedKmh} km/h" else "—", Icons.Default.Air)
                    MetricMiniBadge("Soil Temp", "Not measured", Icons.Default.Grass)
                    MetricMiniBadge("UV Index", if (weather.isRealtime) "${weather.uvIndex}" else "—", Icons.Default.WbSunny)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Agricultural Planning Section Header (Interactive Expandable)
                Surface(
                    onClick = { expandedPlanning = !expandedPlanning },
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.15f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = AgSproutGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "🌾 AGRICULTURAL PLANNING MATRIX ($cropName)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Text(
                            text = if (expandedPlanning) "Hide ▲" else "View ▼",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgSproutGreen
                        )
                    }
                }

                // Expandable Agricultural Impact Details
                AnimatedVisibility(visible = expandedPlanning) {
                    Column(modifier = Modifier.padding(top = 10.dp)) {

                        // Temperature Impact Card
                        AgriDetailRow(
                            category = weather.agriPlanning.tempCategory,
                            impact = weather.agriPlanning.tempImpact,
                            icon = Icons.Default.Thermostat,
                            accentColor = Color(0xFFF97316)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Humidity Impact Card
                        AgriDetailRow(
                            category = weather.agriPlanning.humidityCategory,
                            impact = weather.agriPlanning.humidityImpact,
                            icon = Icons.Default.Opacity,
                            accentColor = Color(0xFF06B6D4)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Actionable Planning Recommendations Row
                        Text(
                            text = "💡 RECOMMENDED FARM ACTIONS TODAY:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AgSproutGreen,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        PlanningChip(text = weather.agriPlanning.irrigationAdvice)
                        Spacer(modifier = Modifier.height(4.dp))
                        PlanningChip(text = weather.agriPlanning.sprayingAdvice)
                        Spacer(modifier = Modifier.height(4.dp))
                        PlanningChip(text = weather.agriPlanning.sowingHarvestAdvice)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Action Button: View Full 7-Day Forecast
                Button(
                    onClick = onViewFullForecast,
                    modifier = Modifier
                        .testTag("open_weather_forecast_button")
                        .fillMaxWidth()
                        .height(42.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AgSproutGreen)
                ) {
                    Text(
                        text = "VIEW FULL 7-DAY FORECAST & METRICS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgDarkGreen
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = AgDarkGreen,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricMiniBadge(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = label,
                fontSize = 9.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun AgriDetailRow(category: String, impact: String, icon: androidx.compose.ui.graphics.vector.ImageVector, accentColor: Color) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.White.copy(alpha = 0.08f),
        border = BorderStroke(0.8.dp, accentColor.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
                Text(
                    text = impact,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun PlanningChip(text: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color.White.copy(alpha = 0.12f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}
