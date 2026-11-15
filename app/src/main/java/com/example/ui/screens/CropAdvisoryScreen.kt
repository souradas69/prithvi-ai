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
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Science
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
import com.example.data.entity.FieldEntity
import com.example.ui.theme.AgContainerGreen
import com.example.ui.theme.AgDarkGreen
import com.example.ui.theme.AgLeafGreen
import com.example.ui.theme.AgPrimaryGreen
import com.example.ui.theme.AgSproutGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CropAdvisoryScreen(
    fields: List<FieldEntity>,
    modifier: Modifier = Modifier
) {
    val activeField = fields.firstOrNull() ?: FieldEntity(
        id = 1,
        farmId = 1,
        fieldName = "Main Field",
        area = 2.0,
        cropName = "Tomato",
        cropVariety = "Sona PK",
        plantingDateEpochDays = System.currentTimeMillis() / (1000 * 60 * 60 * 24) - 35,
        growthStage = "Vegetative",
        soilInfo = "Alluvial Loam",
        irrigationMethod = "Drip Irrigation",
        waterSource = "Borewell",
        notes = ""
    )

    var selectedTab by remember { mutableStateOf("Nutrients") }

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
                text = "Crop Growth Advisory",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AgDarkGreen
            )
            Text(
                text = "Tailored for ${activeField.cropName} (${activeField.cropVariety}) • ${activeField.growthStage} Stage",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            // Stage Overview Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AgDarkGreen)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Current Stage: ${activeField.growthStage}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Estimated 20 days remaining until Flowering transition.",
                        fontSize = 12.sp,
                        color = AgSproutGreen,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Advisory Topic Selector Tabs
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                listOf("Nutrients", "Irrigation", "Pests", "Diseases", "Harvesting").forEach { tab ->
                    val isSelected = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isSelected) AgPrimaryGreen else AgContainerGreen)
                            .clickable { selectedTab = tab }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = tab,
                            color = if (isSelected) Color.White else AgDarkGreen,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            // Advisory Content Based on Selected Tab
            when (selectedTab) {
                "Nutrients" -> {
                    AdvisoryCard(
                        title = "Nutrient & Fertilizer Schedule (Vegetative Phase)",
                        points = listOf(
                            "Top dress Nitrogen (Urea 25 kg/acre) to encourage strong foliar branching.",
                            "Incorporate Micronutrient Foliar Spray (Zinc & Boron 2g/L water) to prevent blossom drop.",
                            "Apply Organic Neem Cake (100 kg/acre) around stem bases to enhance soil microbial activity."
                        )
                    )
                }
                "Irrigation" -> {
                    AdvisoryCard(
                        title = "Irrigation Management",
                        points = listOf(
                            "Maintain 65-70% soil moisture capacity during active branch expansion.",
                            "Apply 35 minutes of drip irrigation every 2 days during morning hours.",
                            "Avoid evening overhead sprinkling to prevent high leaf dampness and fungal germination."
                        )
                    )
                }
                "Pests" -> {
                    AdvisoryCard(
                        title = "Pest Monitoring & Defense",
                        points = listOf(
                            "Inspect leaf undersides for Whitefly & Leaf Miner trail marks.",
                            "Install 4 Yellow Sticky Traps per acre for early pest detection.",
                            "Spray Neem Oil emulsion (5ml/L water) at 7-day intervals as a natural repellent."
                        )
                    )
                }
                "Diseases" -> {
                    AdvisoryCard(
                        title = "Disease Management",
                        points = listOf(
                            "Watch out for Early Blight leaf spots on lower foliage during humid spells.",
                            "Remove lower bottom leaves touching moist soil.",
                            "If leaf spot exceeds 5% area, apply Copper Oxychloride (2.5g/L water)."
                        )
                    )
                }
                "Harvesting" -> {
                    AdvisoryCard(
                        title = "Harvest Preparation & Quality",
                        points = listOf(
                            "Harvest at breaker stage (pink/red tip) for long-distance mandi transport.",
                            "Store harvested produce in ventilated wooden/plastic crates in cool shaded areas.",
                            "Check local Mandi price trends in PRITHVI AI Market module before picking."
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun AdvisoryCard(title: String, points: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AgDarkGreen)
            Spacer(modifier = Modifier.height(12.dp))
            points.forEach { point ->
                Row(
                    modifier = Modifier.padding(bottom = 10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = AgPrimaryGreen,
                        modifier = Modifier.size(18.dp).padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = point, fontSize = 13.sp, color = AgDarkGreen, lineHeight = 18.sp)
                }
            }
        }
    }
}
