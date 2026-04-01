package org.mst_college.project.quiz.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.User
import utils.UserManager

@Composable
fun LoginScreen(onLoginSuccess: (User) -> Unit, onNavigateToRegister: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

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

                AuthTextField(value = username, label = "Username", onValueChange = { username = it }, icon = Icons.Default.AccountCircle)
                Spacer(Modifier.height(15.dp))
                AuthTextField(value = password, label = "Password", onValueChange = { password = it }, icon = Icons.Default.Lock, isPassword = true)

                Spacer(Modifier.height(30.dp))

                Button(
                    onClick = {
                        if (username.isBlank() || password.isBlank()) {
                            errorMessage = "Please enter both username and password"
                        } else {
                            // UserManager ရဲ့ login function ကို လှမ်းခေါ်မယ်
                            val user = UserManager.login(username, password)

                            if (user != null) {
                                // Login အောင်မြင်ရင် Welcome Screen ကို သွားမယ်
                                onLoginSuccess(user)
                            } else {
                                // Username မှားတာ သို့မဟုတ် Password မကိုက်တာ
                                errorMessage = "Invalid username or password"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF3F51B5))
                ) {
                    Text("LOGIN", color = Color.White, fontWeight = FontWeight.Bold)
                }

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }

                TextButton(onClick = onNavigateToRegister) {
                    Text("Don't have an account? Register", color = Color.Cyan)
                }
            }
        }
    }
}