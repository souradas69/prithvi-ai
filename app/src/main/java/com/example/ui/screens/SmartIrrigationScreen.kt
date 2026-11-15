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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.FieldEntity
import com.example.ui.theme.AgDarkGreen
import com.example.ui.theme.AgPrimaryGreen

/**
 * Software-only irrigation planner. This screen intentionally does not display
 * simulated sensor readings or provide pump/device controls. It records a
 * farmer-confirmed watering activity through the existing local data layer.
 */
@Composable
fun SmartIrrigationScreen(
    field: FieldEntity?,
    onMarkWatered: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val fieldName = field?.fieldName ?: "Your field"
    val crop = field?.cropName ?: "your crop"
    val area = field?.area
    val soil = field?.soilInfo?.takeIf { it.isNotBlank() } ?: "Soil type not recorded"
    val method = field?.irrigationMethod?.takeIf { it.isNotBlank() } ?: "Irrigation method not recorded"

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            Text("Irrigation Planner", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Text(
                text = "Practical watering guidance based on your farm information and observations.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF4E6)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = AgDarkGreen, modifier = Modifier.size(20.dp))
                    Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                        Text("Advisory only — no hardware connected", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AgDarkGreen)
                        Text(
                            "PRITHVI does not read soil sensors, monitor water tanks, or control irrigation pumps in this version. Use your own field observations before watering.",
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            color = Color(0xFF49634D),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = AgDarkGreen), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp)) {
                    Text("SELECTED FIELD", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFFBFE58F), letterSpacing = 1.2.sp)
                    Text(fieldName, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(top = 5.dp))
                    Text(
                        buildString {
                            append(crop)
                            if (area != null) append(" • $area acres")
                            append(" • $soil")
                        },
                        fontSize = 11.sp,
                        color = Color(0xFFD5E6D8),
                        modifier = Modifier.padding(top = 5.dp)
                    )
                    Text("Method: $method", fontSize = 10.sp, color = Color(0xFFBFE58F), modifier = Modifier.padding(top = 8.dp))
                }
            }

            Spacer(Modifier.height(17.dp))
            Text("Before you irrigate", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(Modifier.height(9.dp))
            listOf(
                "Check soil moisture by hand at the active root depth; do not rely on a guessed percentage.",
                "Review the latest local weather forecast and postpone watering if useful rainfall is expected.",
                "Consider crop stage, soil texture and the irrigation method when deciding the amount and timing.",
                "Avoid waterlogging. If plants show stress despite moist soil, investigate drainage, roots and disease before adding more water."
            ).forEachIndexed { index, guidance ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                        Box(
                            modifier = Modifier.size(25.dp).background(Color(0xFFEAF4E6), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) { Text("${index + 1}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AgDarkGreen) }
                        Text(guidance, fontSize = 11.sp, lineHeight = 16.sp, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f).padding(start = 10.dp))
                    }
                }
            }

            Spacer(Modifier.height(14.dp))
            Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(15.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = AgPrimaryGreen)
                        Text("  Keep your farm record updated", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        "When you have actually watered this field, record the activity below. This updates your farm history; it does not start or stop a pump.",
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 7.dp, bottom = 12.dp)
                    )
                    Button(
                        onClick = { field?.let { onMarkWatered(it.id) } },
                        enabled = field != null,
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                        Text("  Mark field as watered", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    if (field == null) {
                        Text("Add a farm field to record irrigation activity.", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.WaterDrop, contentDescription = null, tint = AgPrimaryGreen, modifier = Modifier.size(14.dp))
                Text("  Water wisely. Record accurately.", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
