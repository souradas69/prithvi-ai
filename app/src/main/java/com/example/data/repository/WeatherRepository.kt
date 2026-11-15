package com.example.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AgriPlanningGuide(
    val tempImpact: String,
    val tempCategory: String,
    val humidityImpact: String,
    val humidityCategory: String,
    val irrigationAdvice: String,
    val sprayingAdvice: String,
    val sowingHarvestAdvice: String
)

data class WeatherData(
    val locationName: String,
    val tempC: Int,
    val tempF: Int = (tempC * 9 / 5) + 32,
    val condition: String,
    val humidityPercent: Int,
    val windSpeedKmh: Int,
    val rainProbabilityPercent: Int,
    val precipitationMm: Double = 0.0,
    val pressureHpa: Int = 1013,
    val uvIndex: Double = 5.5,
    val soilTemp0cmC: Double = tempC.toDouble() - 1.5,
    val isRealtime: Boolean = true,
    val lastUpdatedTime: String = "Just now",
    val sunrise: String = "05:42 AM",
    val sunset: String = "06:35 PM",
    val forecast7Days: List<DayForecast>,
    val severeAlert: String? = null,
    val aiInsight: String,
    val agriPlanning: AgriPlanningGuide
)

data class DayForecast(
    val dayName: String,
    val maxTempC: Int,
    val minTempC: Int,
    val rainPercent: Int,
    val condition: String
)

object WeatherRepository {

    suspend fun fetchRealtimeWeatherData(
        state: String,
        district: String,
        village: String,
        cropName: String
    ): WeatherData = withContext(Dispatchers.IO) {
        val location = if (district.isNotBlank()) "$district, $state" else state.ifBlank { "Hooghly, West Bengal" }
        val (lat, lon) = getCoordinatesForRegion(state, district)

        try {
            val urlString = "https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon" +
                    "&current=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m,precipitation,surface_pressure" +
                    "&daily=temperature_2m_max,temperature_2m_min,precipitation_probability_max,uv_index_max" +
                    "&timezone=auto"

            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            if (connection.responseCode == 200) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()

                val json = JSONObject(response.toString())
                val current = json.getJSONObject("current")
                val daily = json.getJSONObject("daily")

                val tempC = current.getDouble("temperature_2m").toInt()
                val humidity = current.getInt("relative_humidity_2m")
                val windSpeed = current.getDouble("wind_speed_10m").toInt()
                val precip = current.optDouble("precipitation", 0.0)
                val code = current.optInt("weather_code", 0)
                val pressure = current.optDouble("surface_pressure", 1013.0).toInt()

                val conditionStr = mapWmoCodeToCondition(code, precip)

                val dailyMaxArr = daily.getJSONArray("temperature_2m_max")
                val dailyMinArr = daily.getJSONArray("temperature_2m_min")
                val dailyRainArr = daily.getJSONArray("precipitation_probability_max")
                val uvArr = daily.optJSONArray("uv_index_max")

                val rainProbToday = if (dailyRainArr.length() > 0) dailyRainArr.getInt(0) else (precip * 20).toInt().coerceIn(0, 100)
                val uvIndex = if (uvArr != null && uvArr.length() > 0) uvArr.getDouble(0) else 6.0

                val daysOfWeek = listOf("Today", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                val forecast = mutableListOf<DayForecast>()
                val count = minOf(7, dailyMaxArr.length())
                for (i in 0 until count) {
                    val maxT = dailyMaxArr.getDouble(i).toInt()
                    val minT = dailyMinArr.getDouble(i).toInt()
                    val rProb = if (i < dailyRainArr.length()) dailyRainArr.getInt(i) else 20
                    val dayName = if (i == 0) "Today" else daysOfWeek[i % daysOfWeek.size]
                    val dayCond = if (rProb > 50) "Light Rain" else if (rProb > 25) "Partly Cloudy" else "Sunny & Clear"
                    forecast.add(DayForecast(dayName, maxT, minT, rProb, dayCond))
                }

                val currentTimeStr = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
                val planningGuide = buildAgriPlanningGuide(tempC, humidity, rainProbToday, windSpeed, cropName)
                val alert = if (rainProbToday > 65 || windSpeed > 35) {
                    "⚠️ Severe Weather Warning: High precipitation ($rainProbToday%) or wind ($windSpeed km/h) expected in $district."
                } else null

                val aiInsight = buildAiInsight(tempC, humidity, rainProbToday, cropName)

                return@withContext WeatherData(
                    locationName = location,
                    tempC = tempC,
                    condition = conditionStr,
                    humidityPercent = humidity,
                    windSpeedKmh = windSpeed,
                    rainProbabilityPercent = rainProbToday,
                    precipitationMm = precip,
                    pressureHpa = pressure,
                    uvIndex = uvIndex,
                    soilTemp0cmC = 0.0,
                    isRealtime = true,
                    lastUpdatedTime = currentTimeStr,
                    forecast7Days = forecast,
                    severeAlert = alert,
                    aiInsight = aiInsight,
                    agriPlanning = planningGuide
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Fallback offline data
        return@withContext getWeatherDataForLocation(state, district, village, cropName)
    }

    fun getWeatherDataForLocation(state: String, district: String, village: String, cropName: String): WeatherData {
        val location = if (district.isNotBlank()) "$district, $state" else state.ifBlank { "Farm location not set" }
        val unavailable = "Live weather data is currently unavailable. Refresh the forecast or consult a trusted local weather source before making weather-dependent farm decisions."
        val planning = AgriPlanningGuide(
            tempImpact = "Weather data unavailable.",
            tempCategory = "Not available",
            humidityImpact = "Weather data unavailable.",
            humidityCategory = "Not available",
            irrigationAdvice = "Check soil moisture manually and review a current local forecast.",
            sprayingAdvice = "Do not schedule spraying based on unavailable weather data.",
            sowingHarvestAdvice = "Consult a current local forecast before scheduling field work."
        )
        return WeatherData(
            locationName = location,
            tempC = 0,
            condition = "Unavailable",
            humidityPercent = 0,
            windSpeedKmh = 0,
            rainProbabilityPercent = 0,
            precipitationMm = 0.0,
            pressureHpa = 0,
            uvIndex = 0.0,
            soilTemp0cmC = 0.0,
            isRealtime = false,
            lastUpdatedTime = "Not available",
            forecast7Days = emptyList(),
            severeAlert = null,
            aiInsight = unavailable,
            agriPlanning = planning
        )
    }

    private fun buildAgriPlanningGuide(tempC: Int, humidity: Int, rainProb: Int, windSpeed: Int, cropName: String): AgriPlanningGuide {
        val tempCat = when {
            tempC in 20..30 -> "✅ Optimal Photosynthesis Range ($tempC°C)"
            tempC > 32 -> "🔥 Heat Stress Warning ($tempC°C)"
            else -> "❄️ Mild Cool Temperatures ($tempC°C)"
        }

        val tempImpact = when {
            tempC in 20..30 -> "Ideal enzyme activity & stomatal conductance for $cropName. Rapid vegetative development."
            tempC > 32 -> "High temperature increases transpiration rate. Possible pollen desiccation if unwatered."
            else -> "Slower metabolic growth rate. Reduce nitrogen fertilizer dosage until ambient warming."
        }

        val humCat = when {
            humidity > 75 -> "⚠️ High Fungal Spore Hazard ($humidity% RH)"
            humidity in 50..75 -> "✅ Optimal Moisture Balance ($humidity% RH)"
            else -> "🌵 Dry Evapotranspiration Hazard ($humidity% RH)"
        }

        val humImpact = when {
            humidity > 75 -> "Elevated leaf wetness risk. Prime environment for Phytophthora late blight, downy mildew & rust spores in $cropName."
            humidity in 50..75 -> "Favorable humidity level supporting healthy leaf turgor and balanced nutrient uptake."
            else -> "Low air moisture accelerates water loss from leaves. Ensure root zone moisture is replenished."
        }

        val irrAdvice = when {
            rainProb > 50 -> "🌧️ Postpone Irrigation: High rain probability ($rainProb%). Prevent soil saturation & root rot."
            tempC > 30 || humidity < 50 -> "💧 Irrigation Required: Apply 15-20 mm drip irrigation early morning (06:00 AM) to maintain root hydration."
            else -> "💧 Normal Irrigation Schedule: Standard 10 mm watering sufficient today."
        }

        val sprayAdvice = when {
            windSpeed > 20 -> "🚫 Hold Pesticide Spraying: Wind speed ($windSpeed km/h) causes spray drift."
            rainProb > 40 -> "🚫 Delay Chemical Application: Impending rain ($rainProb%) will wash off foliar sprays."
            else -> "✅ Favorable Spraying Window: Early morning (06:30 - 09:00 AM) or late afternoon."
        }

        val sowingAdvice = when {
            tempC in 22..30 && rainProb in 15..40 -> "🌱 Excellent Sowing / Field Transplanting Window for $cropName."
            else -> "🚜 Routine Field Maintenance: Hoeing, weeding & mulching recommended."
        }

        return AgriPlanningGuide(
            tempImpact = tempImpact,
            tempCategory = tempCat,
            humidityImpact = humImpact,
            humidityCategory = humCat,
            irrigationAdvice = irrAdvice,
            sprayingAdvice = sprayAdvice,
            sowingHarvestAdvice = sowingAdvice
        )
    }

    private fun buildAiInsight(tempC: Int, humidity: Int, rainProb: Int, cropName: String): String {
        return when {
            rainProb > 50 -> "High rain probability ($rainProb%). Postpone irrigation today for your $cropName crop and inspect field drainage channels."
            tempC > 33 -> "High temperatures ($tempC°C) may stress your $cropName crop. Ensure light early morning drip irrigation."
            humidity > 78 -> "High relative humidity ($humidity%) creates ideal conditions for fungal leaf spot in $cropName. Monitor leaves closely."
            else -> "Optimal weather conditions ($tempC°C, $humidity% RH) for $cropName growth today."
        }
    }

    private fun mapWmoCodeToCondition(code: Int, precip: Double): String {
        return when (code) {
            0 -> "Sunny & Clear"
            1, 2 -> "Partly Cloudy"
            3 -> "Overcast Sky"
            45, 48 -> "Foggy / Mist"
            51, 53, 55 -> "Light Drizzle"
            61, 63, 65 -> if (precip > 5.0) "Heavy Rainfall" else "Moderate Rain"
            80, 81, 82 -> "Rain Showers"
            95, 96, 99 -> "Thunderstorm Alert"
            else -> "Partly Cloudy"
        }
    }

    private fun getCoordinatesForRegion(state: String, district: String): Pair<Double, Double> {
        val key = (state + " " + district).lowercase()
        return when {
            key.contains("punjab") || key.contains("ludhiana") -> Pair(30.90, 75.85)
            key.contains("uttar pradesh") || key.contains("lucknow") || key.contains("kanpur") -> Pair(26.84, 80.94)
            key.contains("maharashtra") || key.contains("nashik") || key.contains("pune") -> Pair(19.99, 73.78)
            key.contains("karnataka") || key.contains("mandya") || key.contains("bengaluru") -> Pair(12.52, 76.89)
            key.contains("tamil nadu") || key.contains("coimbatore") -> Pair(11.01, 76.95)
            key.contains("bihar") || key.contains("patna") -> Pair(25.59, 85.13)
            key.contains("madhya pradesh") || key.contains("indore") -> Pair(22.71, 75.85)
            key.contains("gujarat") || key.contains("anand") -> Pair(22.56, 72.92)
            key.contains("rajasthan") || key.contains("jaipur") -> Pair(26.91, 75.78)
            key.contains("haryana") || key.contains("karnal") -> Pair(29.68, 76.99)
            key.contains("telangana") || key.contains("hyderabad") -> Pair(17.38, 78.48)
            key.contains("andhra") || key.contains("guntur") -> Pair(16.30, 80.43)
            key.contains("kerala") || key.contains("wayanad") -> Pair(11.68, 76.13)
            key.contains("odisha") || key.contains("cuttack") -> Pair(20.46, 85.88)
            else -> Pair(22.88, 88.24)
        }
    }
}

