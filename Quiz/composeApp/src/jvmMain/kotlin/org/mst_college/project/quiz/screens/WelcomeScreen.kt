package org.mst_college.project.quiz.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import quiz.composeapp.generated.resources.Res
import quiz.composeapp.generated.resources.logo
import settings.SettingsManager

@Composable
fun WelcomeScreen(next: () -> Unit) {
    // Settings ကို Load လုပ်မယ်
    val settings = remember { SettingsManager.load() }

    LaunchedEffect(Unit) {
        delay(2500)
        next()
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF141E30), Color(0xFF243B55))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(160.dp)
            )

            Spacer(Modifier.height(20.dp))

            // ပုံသေစာသားနေရာမှာ settings.title ကို အစားထိုးလိုက်တယ်
            Text(
                text = settings.title,
                fontSize = 36.sp,
                color = Color.White
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Empowered by Naing Ko Ko Lin",
                color = Color.LightGray
            )
        }
    }
}