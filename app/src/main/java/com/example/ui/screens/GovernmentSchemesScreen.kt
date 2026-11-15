package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.OpenInNew
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
import com.example.data.repository.GovtScheme
import com.example.data.repository.GovernmentSchemeRepository
import com.example.ui.theme.AgContainerGreen
import com.example.ui.theme.AgDarkGreen
import com.example.ui.theme.AgPrimaryGreen
import com.example.ui.theme.AgSproutGreen

@Composable
fun GovernmentSchemesScreen(
    state: String,
    modifier: Modifier = Modifier
) {
    var filter by remember { mutableStateOf("All") }
    val schemes = GovernmentSchemeRepository.getSchemesForFarmer(state)

    val filteredSchemes = schemes.filter {
        when (filter) {
            "Central" -> it.isCentral
            "State" -> !it.isCentral
            else -> true
        }
    }

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
                text = "Government Schemes & Subsidies",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AgDarkGreen
            )
            Text(
                text = "Central & $state State farmer welfare programs, eligibility & documents.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
            )

            // Filter Chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                listOf("All", "Central", "State").forEach { item ->
                    val selected = filter == item
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (selected) AgPrimaryGreen else AgContainerGreen)
                            .clickable { filter = item }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = item,
                            color = if (selected) Color.White else AgDarkGreen,
                            fontSize = 13.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            filteredSchemes.forEach { scheme ->
                GovtSchemeCard(scheme)
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}

@Composable
fun GovtSchemeCard(scheme: GovtScheme) {
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
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (scheme.isCentral) AgDarkGreen else AgPrimaryGreen)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (scheme.isCentral) "Central Govt Scheme" else "${scheme.stateName} State Scheme",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(text = scheme.lastUpdated, fontSize = 10.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = scheme.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AgDarkGreen)
            Text(text = scheme.shortDescription, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(AgContainerGreen)
                    .padding(12.dp)
            ) {
                Column {
                    Text(text = "Key Benefits", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AgPrimaryGreen)
                    Text(text = scheme.benefits, fontSize = 13.sp, color = AgDarkGreen, modifier = Modifier.padding(top = 2.dp))

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Eligibility Criteria", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AgPrimaryGreen)
                    Text(text = scheme.eligibility, fontSize = 13.sp, color = AgDarkGreen, modifier = Modifier.padding(top = 2.dp))

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Required Documents Checklist", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AgPrimaryGreen)
                    scheme.requiredDocuments.forEach { doc ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = AgPrimaryGreen, modifier = Modifier.size(14.dp))
                            Text(text = doc, fontSize = 12.sp, color = AgDarkGreen, modifier = Modifier.padding(start = 6.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Source: ${scheme.officialSource}", fontSize = 11.sp, color = Color.Gray)
                Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, tint = AgPrimaryGreen, modifier = Modifier.size(18.dp))
            }
        }
    }
}
