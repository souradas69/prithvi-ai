package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun FarmingBackground(
    isDarkMode: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val bgGradient = if (isDarkMode) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF06150B), // Deep Forest Night
                Color(0xFF030A05), // Deep Dark Emerald
                Color(0xFF010502)  // Pitch Black Green
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFEBF5EE), // Soft Organic Light Green
                Color(0xFFF4FAF5),
                Color(0xFFE8F3EA)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
    ) {
        // Exaggerated Grasses, Fields & Farming Vector Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val lineColor = if (isDarkMode) Color(0xFF10B981).copy(alpha = 0.08f) else Color(0xFF2E7D32).copy(alpha = 0.06f)
            val glowColor = if (isDarkMode) Color(0xFF34D399).copy(alpha = 0.05f) else Color(0xFF81C784).copy(alpha = 0.08f)

            // Draw field contour lines
            val fieldPath = Path().apply {
                moveTo(0f, height * 0.25f)
                cubicTo(width * 0.3f, height * 0.2f, width * 0.7f, height * 0.3f, width, height * 0.22f)
                moveTo(0f, height * 0.55f)
                cubicTo(width * 0.4f, height * 0.5f, width * 0.6f, height * 0.6f, width, height * 0.52f)
                moveTo(0f, height * 0.82f)
                cubicTo(width * 0.2f, height * 0.78f, width * 0.8f, height * 0.85f, width, height * 0.8f)
            }
            drawPath(path = fieldPath, color = lineColor, style = Stroke(width = 3f))

            // Draw stylized grass blades at bottom
            val grassPath = Path().apply {
                val startY = height * 0.92f
                var x = 20f
                while (x < width) {
                    moveTo(x, startY)
                    quadraticTo(x - 15f, startY - 40f, x - 25f, startY - 80f)
                    moveTo(x, startY)
                    quadraticTo(x + 10f, startY - 50f, x + 20f, startY - 95f)
                    moveTo(x, startY)
                    quadraticTo(x + 25f, startY - 35f, x + 35f, startY - 70f)
                    x += 70f
                }
            }
            drawPath(path = grassPath, color = lineColor, style = Stroke(width = 2.5f))

            // Draw subtle glowing agricultural node circles (seeds/sprouts)
            val seedPositions = listOf(
                Offset(width * 0.15f, height * 0.18f),
                Offset(width * 0.85f, height * 0.12f),
                Offset(width * 0.75f, height * 0.45f),
                Offset(width * 0.12f, height * 0.65f),
                Offset(width * 0.88f, height * 0.78f)
            )
            seedPositions.forEach { pos ->
                drawCircle(color = glowColor, radius = 45f, center = pos)
                drawCircle(color = lineColor, radius = 8f, center = pos)
            }
        }

        content()
    }
}
