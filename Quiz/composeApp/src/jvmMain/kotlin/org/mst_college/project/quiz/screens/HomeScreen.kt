package org.mst_college.project.quiz.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.User
import org.jetbrains.compose.resources.painterResource
import quiz.composeapp.generated.resources.Res
import quiz.composeapp.generated.resources.logo
import settings.SettingsManager
import utils.ImageUtils
import kotlin.system.exitProcess

@Composable
fun HomeScreen(
    loggedInUser: User,
    onQuiz: () -> Unit,
    onJudge: () -> Unit,
    onSettings: () -> Unit
) {
    val settings = remember { SettingsManager.load() }

    val logoPainter: Painter = if (settings.logoPath != null) {
        val bitmap = ImageUtils.loadImageFromPath(settings.logoPath)
        if (bitmap != null) BitmapPainter(bitmap) else painterResource(Res.drawable.logo)
    } else {
        painterResource(Res.drawable.logo) // Default
    }

    val backgroundGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF0D47A1), Color(0xFF001233))
    )

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- Left Side: Logo & Title ---
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = logoPainter,
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = settings.title.uppercase(),
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )

            Text(
                text = "Tournament Edition v1.0",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 18.sp
            )
        }

        // --- Right Side: Menu Buttons ---
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MenuButton("Start Quiz", Icons.Default.PlayArrow, Color(0xFF4CAF50), onQuiz)

            if (loggedInUser.role == "ADMIN") {
                MenuButton("Judge Panel", Icons.Default.Groups, Color(0xFF2196F3), onJudge)
                MenuButton("Settings", Icons.Default.Settings, Color(0xFF607D8B), onSettings)
            }

            Divider(color = Color.White.copy(alpha = 0.2f), thickness = 1.dp, modifier = Modifier.width(280.dp).padding(vertical = 10.dp))

            MenuButton("Quit App", Icons.AutoMirrored.Filled.ExitToApp, Color(0xFFD32F2F), {
                exitProcess(0)
            })
        }
    }
}

@Composable
fun MenuButton(
    text: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.width(320.dp).height(65.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
        elevation = ButtonDefaults.elevation(defaultElevation = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}