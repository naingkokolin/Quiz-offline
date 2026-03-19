package org.mst_college.project.quiz.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onNavigateToRegister: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val bgGradient = Brush.verticalGradient(listOf(Color(0xFF1A237E), Color(0xFF121212)))

    Box(modifier = Modifier.fillMaxSize().background(bgGradient), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.width(400.dp).padding(20.dp),
            shape = RoundedCornerShape(24.dp),
            backgroundColor = Color.White.copy(alpha = 0.1f),
            elevation = 0.dp
        ) {
            Column(modifier = Modifier.padding(30.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Welcome Back", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Login to your account", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)

                Spacer(Modifier.height(30.dp))

                AuthTextField(value = email, label = "Email", onValueChange = { email = it }, icon = Icons.Default.Email)
                Spacer(Modifier.height(15.dp))
                AuthTextField(value = password, label = "Password", onValueChange = { password = it }, icon = Icons.Default.Lock, isPassword = true)

                Spacer(Modifier.height(30.dp))

                Button(
                    onClick = onLoginSuccess,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF3F51B5))
                ) {
                    Text("LOGIN", color = Color.White, fontWeight = FontWeight.Bold)
                }

                TextButton(onClick = onNavigateToRegister) {
                    Text("Don't have an account? Register", color = Color.Cyan)
                }
            }
        }
    }
}