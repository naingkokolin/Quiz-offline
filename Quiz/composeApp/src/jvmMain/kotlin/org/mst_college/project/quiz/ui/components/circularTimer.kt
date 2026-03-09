package org.mst_college.project.quiz.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import settings.SettingsManager

@Composable
fun CircularTimer(
    totalTime: Int = SettingsManager.load().timerSeconds, // Settings ကလာမယ့် အချိန်
    key: Int,            // မေးခွန်းနံပါတ်ကို key အဖြစ်သုံးမယ်
    onFinish: () -> Unit,
    playCountdown: () -> Unit
) {
    var timeLeft by remember(key) { mutableStateOf(totalTime) }

    val progress by animateFloatAsState(
        targetValue = (timeLeft.toFloat() / totalTime.toFloat()).coerceIn(0f, 1f),
        animationSpec = tween(1000)
    )

    // key ပြောင်းသွားရင် ဒီ Effect က အစကနေ ပြန်ပွင့်လာမယ်
    LaunchedEffect(key) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--

            if (timeLeft == 10) {
                playCountdown()
            }
        }
        onFinish()
    }

    // အချိန်နည်းလာရင် အရောင်ကို အနီဘက် ပြောင်းပေးမယ်
    val timerColor = when {
        timeLeft > 10 -> Color(0xFF00E676) // Green
        timeLeft > 5 -> Color(0xFFFFD600)  // Yellow
        else -> Color(0xFFFF1744)          // Red
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(220.dp)) {
        Canvas(Modifier.matchParentSize()) {
            // Background Circle (မှိန်မှိန်လေး)
            drawArc(
                color = Color.White.copy(alpha = 0.1f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )

            // Progress Arc with Gradient
            drawArc(
                brush = Brush.sweepGradient(
                    0.0f to timerColor.copy(alpha = 0.5f),
                    1.0f to timerColor
                ),
                startAngle = -90f,
                sweepAngle = 360 * progress,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Timer Text with Shadow/Glow effect လိုမျိုး
        Text(
            text = "$timeLeft",
            fontSize = 56.sp,
            fontWeight = FontWeight.ExtraBold,
            color = timerColor
        )
    }
}