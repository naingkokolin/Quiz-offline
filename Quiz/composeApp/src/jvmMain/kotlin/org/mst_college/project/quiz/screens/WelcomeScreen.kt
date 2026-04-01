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
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import quiz.composeapp.generated.resources.Res
import quiz.composeapp.generated.resources.logo
import settings.SettingsManager
import utils.ImageUtils

@Composable
fun WelcomeScreen(next: () -> Unit) {
    // Settings ကို Load လုပ်မယ်
    val settings = remember { SettingsManager.load() }

    val logoPainter: Painter = if (settings.logoPath != null) {
        val bitmap = ImageUtils.loadImageFromPath(settings.logoPath)
        if (bitmap != null) BitmapPainter(bitmap) else painterResource(Res.drawable.logo)
    } else {
        painterResource(Res.drawable.logo) // Default
    }

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

//            Image(
//                painter = painterResource(Res.drawable.logo),
//                contentDescription = null,
//                modifier = Modifier.size(160.dp)
//            )

            Image(
                painter = logoPainter,
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp)
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
                text = "Empowered by M.S.T College",
                color = Color.LightGray
            )
        }
    }
}