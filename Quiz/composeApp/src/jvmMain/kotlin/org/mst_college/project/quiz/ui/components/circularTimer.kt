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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun CircularTimer(
    totalTime: Int = 60,
    onFinish: () -> Unit,
    playCountdown: () -> Unit
) {

    var timeLeft by remember { mutableStateOf(totalTime) }

    val progress by animateFloatAsState(
//        targetValue = timeLeft / totalTime.toFloat(),
        targetValue = (timeLeft.toFloat() / totalTime.toFloat()).coerceIn(0f, 1f),
        animationSpec = tween(1000)
    )

    LaunchedEffect(Unit) {

        while (timeLeft > 0) {
            delay(1000)
            timeLeft--

            if (timeLeft == 10) {
                playCountdown()
            }
        }

        onFinish()
    }

    Box(contentAlignment = Alignment.Center) {

        Canvas(Modifier.size(220.dp)) {

            drawArc(
                color = Color.DarkGray,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(14f)
            )

            drawArc(
                color = Color.Red,
                startAngle = -90f,
                sweepAngle = 360 * progress,
                useCenter = false,
                style = Stroke(14f)
            )
        }

        Text(
            text = "$timeLeft",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
    }
}