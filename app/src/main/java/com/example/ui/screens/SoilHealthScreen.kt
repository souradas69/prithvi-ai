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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.SoilHealthEntity
import com.example.ui.theme.AgContainerGreen
import com.example.ui.theme.AgDarkGreen
import com.example.ui.theme.AgPrimaryGreen
import com.example.ui.theme.AgSproutGreen

@Composable
fun SoilHealthScreen(
    records: List<SoilHealthEntity>,
    onAddRecord: (ph: Double, n: Double, p: Double, k: Double, oc: Double, date: String, notes: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    val latestRecord = records.firstOrNull() ?: SoilHealthEntity(
        phLevel = 6.8,
        nitrogenKgHa = 240.0,
        phosphorusKgHa = 18.5,
        potassiumKgHa = 150.0,
        organicCarbonPercent = 0.55,
        testDate = "August 2026",
        notes = "Slight Nitrogen deficiency detected."
    )

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Soil Health Card",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgDarkGreen
                )

                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New Test", fontSize = 12.sp)
                }
            }

            Text(
                text = "Last Tested: ${latestRecord.testDate} • Laboratory Analysis",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
            )

            // Soil Parameters Grid Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = AgDarkGreen)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SoilMetricTile("pH Level", "${latestRecord.phLevel}", "Optimal (6.5-7.5)")
                        SoilMetricTile("Organic Carbon", "${latestRecord.organicCarbonPercent}%", "Moderate (>0.5%)")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SoilMetricTile("Nitrogen (N)", "${latestRecord.nitrogenKgHa.toInt()} kg/ha", "Low (<280)")
                        SoilMetricTile("Phosphorus (P)", "${latestRecord.phosphorusKgHa} kg/ha", "Medium (10-25)")
                        SoilMetricTile("Potassium (K)", "${latestRecord.potassiumKgHa.toInt()} kg/ha", "Medium (110-280)")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // AI Soil Amendment Guidance
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
                        Text(text = "PRITHVI Soil Fertilizer Recommendation", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AgPrimaryGreen)
                        Text(
                            text = "Nitrogen is slightly low (${latestRecord.nitrogenKgHa.toInt()} kg/ha). Apply 25 kg Urea per acre along with Farmyard Manure (FYM) to boost organic carbon.",
                            fontSize = 13.sp,
                            color = AgDarkGreen,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }

        if (showDialog) {
            var phText by remember { mutableStateOf("6.8") }
            var nText by remember { mutableStateOf("240") }
            var pText by remember { mutableStateOf("18.5") }
            var kText by remember { mutableStateOf("150") }
            var ocText by remember { mutableStateOf("0.55") }

            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Record Soil Test Result", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        OutlinedTextField(value = phText, onValueChange = { phText = it }, label = { Text("pH Level") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp))
                        OutlinedTextField(value = nText, onValueChange = { nText = it }, label = { Text("Nitrogen (kg/ha)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp))
                        OutlinedTextField(value = pText, onValueChange = { pText = it }, label = { Text("Phosphorus (kg/ha)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp))
                        OutlinedTextField(value = kText, onValueChange = { kText = it }, label = { Text("Potassium (kg/ha)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp))
                        OutlinedTextField(value = ocText, onValueChange = { ocText = it }, label = { Text("Organic Carbon %") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp))
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onAddRecord(
                                phText.toDoubleOrNull() ?: 6.8,
                                nText.toDoubleOrNull() ?: 240.0,
                                pText.toDoubleOrNull() ?: 18.5,
                                kText.toDoubleOrNull() ?: 150.0,
                                ocText.toDoubleOrNull() ?: 0.55,
                                "August 2026",
                                "Added manually"
                            )
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen)
                    ) { Text("Save Test") }
                },
                dismissButton = { OutlinedButton(onClick = { showDialog = false }) { Text("Cancel") } }
            )
        }
    }
}

@Composable
fun SoilMetricTile(title: String, val1: String, val2: String) {
    Column {
        Text(text = title, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
        Text(text = val1, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AgSproutGreen, modifier = Modifier.padding(top = 2.dp))
        Text(text = val2, fontSize = 10.sp, color = Color.White)
    }
}
