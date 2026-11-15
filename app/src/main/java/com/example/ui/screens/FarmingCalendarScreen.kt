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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
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
import com.example.ui.theme.AgContainerGreen
import com.example.ui.theme.AgDarkGreen
import com.example.ui.theme.AgPrimaryGreen
import com.example.ui.theme.AgSproutGreen

@Composable
fun FarmingCalendarScreen(
    cropName: String,
    modifier: Modifier = Modifier
) {
    val events = listOf(
        "Aug 12" to "Drip Fertigation & Micronutrient Spray",
        "Aug 15" to "Pest Sticky Trap Inspection",
        "Aug 20" to "Secondary Soil Weeding & Earthing Up",
        "Sep 02" to "Flowering Stage Transition Check",
        "Sep 15" to "Fruit Set Inspection & Calcium Spray",
        "Oct 05" to "Estimated First Picking / Harvest"
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
            Text(
                text = "Farming Calendar & Reminders",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AgDarkGreen
            )
            Text(
                text = "Stage-based action timeline for $cropName crop.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
            )

            events.forEach { (date, title) ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(AgContainerGreen)
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Text(text = date, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AgPrimaryGreen)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AgDarkGreen)
                            Text(text = "Recommended by PRITHVI AI", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = AgPrimaryGreen, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}
