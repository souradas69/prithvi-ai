package com.example.data.repository

data class MandiPriceInfo(
    val mandiName: String,
    val pricePerQuintal: Int,
    val minPrice: Int,
    val maxPrice: Int,
    val averagePrice: Int,
    val trend: String, // "UP", "DOWN", "STABLE"
    val updateTime: String = "Today, 08:30 AM",
    val distanceKm: Int = 12
)

data class MarketIntelligence(
    val cropName: String,
    val selectedLocation: String,
    val prices: List<MandiPriceInfo>,
    val aiRecommendation: String,
    val lastUpdated: String = "Today, 08:30 AM",
    val sourceName: String = "Agmarknet - Ministry of Agriculture & Farmers Welfare"
)

object MarketRepository {

    fun getMarketData(cropName: String, state: String, district: String): MarketIntelligence {
        val crop = cropName.ifBlank { "Selected crop" }
        val dist = district.ifBlank { "Location not set" }
        val st = state.ifBlank { "State not set" }
        return MarketIntelligence(
            cropName = crop,
            selectedLocation = "$dist, $st",
            prices = emptyList(),
            aiRecommendation = "Live mandi prices are not connected yet. PRITHVI will show prices only after a verified market-data source is configured.",
            lastUpdated = "No live data connected",
            sourceName = "Not configured"
        )
    }

}
