package com.example.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.entity.FarmEntity
import com.example.data.entity.FieldEntity
import com.example.data.i18n.tr
import com.example.ui.theme.AgContainerGreen
import com.example.ui.theme.AgDarkGreen
import com.example.ui.theme.AgLeafGreen
import com.example.ui.theme.AgPrimaryGreen
import com.example.ui.theme.AgSproutGreen

@Composable
fun MyFarmScreen(
    farms: List<FarmEntity>,
    fields: List<FieldEntity>,
    onAddField: (farmId: Long, name: String, area: Double, crop: String, variety: String, stage: String) -> Unit,
    onNavigateToAdvisory: () -> Unit,
    onNavigateToIrrigation: () -> Unit,
    onNavigateToSoilHealth: () -> Unit,
    onNavigateToFinance: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddFieldDialog by remember { mutableStateOf(false) }

    val activeField = fields.firstOrNull() ?: FieldEntity(
        id = 1,
        farmId = 1,
        fieldName = "Tomato Field",
        area = 2.0,
        cropName = "Tomato",
        cropVariety = "Sona PK",
        plantingDateEpochDays = System.currentTimeMillis() / (1000 * 60 * 60 * 24) - 35,
        growthStage = "Vegetative",
        soilInfo = "Alluvial Loam",
        irrigationMethod = "Drip Irrigation",
        waterSource = "Borewell",
        notes = "Drip fertigation scheduled every 3 days."
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Screen Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = tr("my_farm_title"),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgDarkGreen
                    )
                    Button(
                        onClick = { showAddFieldDialog = true },
                        modifier = Modifier.testTag("add_field_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(tr("add_field"), fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Active Field Highlight Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AgDarkGreen)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = activeField.fieldName,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${activeField.area} Acres • ${activeField.cropName} (${activeField.cropVariety})",
                                    fontSize = 13.sp,
                                    color = AgSproutGreen,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(AgSproutGreen.copy(alpha = 0.2f))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Stage: ${activeField.growthStage}",
                                    color = AgSproutGreen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Crop Growth Stage Progression Timeline
                        Text(
                            text = "Crop Stage Timeline",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val stages = listOf("Sowing", "Germination", "Vegetative", "Flowering", "Fruiting", "Harvest")
                        val currentStageIdx = when (activeField.growthStage.lowercase()) {
                            "sowing" -> 0
                            "germination" -> 1
                            "vegetative" -> 2
                            "flowering" -> 3
                            "fruiting" -> 4
                            "harvest" -> 5
                            else -> 2
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            stages.forEachIndexed { idx, stage ->
                                val isReached = idx <= currentStageIdx
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(if (isReached) AgSproutGreen else Color.White.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Grass,
                                            contentDescription = null,
                                            tint = if (isReached) AgDarkGreen else Color.Gray,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Text(
                                        text = stage,
                                        fontSize = 9.sp,
                                        color = if (isReached) Color.White else Color.White.copy(alpha = 0.5f),
                                        fontWeight = if (isReached) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Field Action Categories List
                Text(
                    text = "Field Modules & Management",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgDarkGreen,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                FieldActionTile("AI Crop Advisory", "Tailored irrigation, nutrients, and pest recommendations", Icons.Default.Agriculture, onNavigateToAdvisory)
                FieldActionTile("Irrigation Planner", "Manual watering guidance and activity log", Icons.Default.WaterDrop, onNavigateToIrrigation)
                FieldActionTile("Soil Health Card", "N-P-K nutrient breakdown & organic carbon", Icons.Default.Science, onNavigateToSoilHealth)
                FieldActionTile("Expenses & Harvest Profit", "Record seeds, labour, water costs & sales", Icons.Default.MonetizationOn, onNavigateToFinance)
                FieldActionTile("Farming Calendar", "Stage-based reminders & harvest plan", Icons.Default.CalendarMonth, onNavigateToCalendar)

                Spacer(modifier = Modifier.height(20.dp))

                // All Registered Fields List
                Text(
                    text = "Registered Fields (${fields.size.coerceAtLeast(1)})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgDarkGreen,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (fields.isEmpty()) {
                    FieldCard(field = activeField)
                } else {
                    fields.forEach { f ->
                        FieldCard(field = f)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
            }
        }

        // Add Field Dialog
        if (showAddFieldDialog) {
            var fName by remember { mutableStateOf("") }
            var fArea by remember { mutableStateOf("1.5") }
            var fCrop by remember { mutableStateOf("Wheat") }
            var fVariety by remember { mutableStateOf("HD 2967") }
            var fStage by remember { mutableStateOf("Vegetative") }

            AlertDialog(
                onDismissRequest = { showAddFieldDialog = false },
                title = { Text("Add New Field", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = fName,
                            onValueChange = { fName = it },
                            label = { Text("Field Name") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = fArea,
                            onValueChange = { fArea = it },
                            label = { Text("Area (Acres)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = fCrop,
                            onValueChange = { fCrop = it },
                            label = { Text("Crop Name (e.g., Wheat, Rice)") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = fVariety,
                            onValueChange = { fVariety = it },
                            label = { Text("Crop Variety") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (fName.isNotBlank() && fCrop.isNotBlank()) {
                                onAddField(1L, fName, fArea.toDoubleOrNull() ?: 1.0, fCrop, fVariety, fStage)
                                showAddFieldDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen)
                    ) {
                        Text("Add Field")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showAddFieldDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun FieldActionTile(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AgContainerGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = AgPrimaryGreen, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AgDarkGreen)
                Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
fun FieldCard(field: FieldEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AgContainerGreen)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Agriculture, contentDescription = null, tint = AgPrimaryGreen, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = field.fieldName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = AgDarkGreen)
                Text(text = "${field.cropName} (${field.cropVariety}) • ${field.area} Acres", fontSize = 12.sp, color = AgPrimaryGreen)
                Text(text = "Soil: ${field.soilInfo} • Method: ${field.irrigationMethod}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
