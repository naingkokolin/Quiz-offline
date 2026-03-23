package org.mst_college.project.quiz.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import utils.UserManager

@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit, onNavigateToLogin: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val bgGradient = Brush.verticalGradient(listOf(Color(0xFF4527A0), Color(0xFF121212)))

    Box(modifier = Modifier.fillMaxSize().background(bgGradient), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.width(400.dp).padding(20.dp),
            shape = RoundedCornerShape(24.dp),
            backgroundColor = Color.White.copy(alpha = 0.1f)
        ) {
            Column(modifier = Modifier.padding(30.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Create Account", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(Modifier.height(25.dp))

                AuthTextField(value = name, label = "Full Name", onValueChange = { name = it }, icon = Icons.Default.Person)
                Spacer(Modifier.height(15.dp))
                AuthTextField(value = username, label = "Username", onValueChange = { username= it }, icon = Icons.Default.Email)
                Spacer(Modifier.height(15.dp))
                AuthTextField(value = password, label = "Password", onValueChange = { password = it }, icon = Icons.Default.Lock, isPassword = true)

                Spacer(Modifier.height(30.dp))

                Button(
                    onClick = {
                        if (username.isBlank() || password.isBlank()) {
                            errorMessage = "Fields cannot be empty!"
                        } else {
                            val isRegistered = UserManager.register(username, password)
                            if (isRegistered) {
                                onRegisterSuccess()
                            } else {
                                errorMessage = "Username already exists!"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF7E57C2))
                ) {
                    Text("REGISTER", color = Color.White, fontWeight = FontWeight.Bold)
                }

                if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = Color.Red, modifier = Modifier.padding(top = 10.dp))
                }

                TextButton(onClick = onNavigateToLogin) {
                    Text("Already have an account? Login", color = Color.Cyan)
                }
            }
        }
    }
}

@Composable
fun AuthTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White.copy(alpha = 0.7f)) },
        leadingIcon = { Icon(icon, null, tint = Color.Cyan) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        visualTransformation = if (isPassword) androidx.compose.ui.text.input.PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Cyan,
            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
            textColor = Color.White,
            cursorColor = Color.Cyan
        )
    )
}