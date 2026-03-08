package org.mst_college.project.quiz.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mst_college.project.quiz.ui.components.AnimatedQuestion
import org.mst_college.project.quiz.ui.components.CircularTimer
import utils.SoundPlayer

@Composable
fun QuizScreen(onBack: () -> Unit) {
    var isStarted by remember { mutableStateOf(false) }
    var showAnswer by remember { mutableStateOf(false) }
    var isTimeUp by remember { mutableStateOf(false) }
    var currentQuestionIndex by remember { mutableStateOf(1) } // မေးခွန်းနံပါတ်

    val primaryGradient = Brush.verticalGradient(listOf(Color(0xFF1A237E), Color(0xFF121212)))

    Box(modifier = Modifier.fillMaxSize().background(primaryGradient)) {
        // --- Back Button ---
        IconButton(onClick = onBack, modifier = Modifier.padding(20.dp).align(Alignment.TopStart)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!isStarted) {
                Button(
                    onClick = {
                        isStarted = true
                        SoundPlayer.play("start.wav") // စတဲ့အသံ
                    },
                    modifier = Modifier.size(180.dp).clip(CircleShape),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50))
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                        Icon(Icons.Default.PlayArrow, contentDescription = null, size = 50.dp, tint = Color.White)
                        Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White)
                        Text("START QUIZ", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                // --- Quiz Content (Start နှိပ်မှ ပေါ်မယ်) ---
                Text(
                    "Question #$currentQuestionIndex",
                    fontSize = 18.sp,
                    color = Color.Cyan,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(10.dp))

                AnimatedQuestion("What is SQL Injection?")

                Spacer(Modifier.height(30.dp))

                // Options
                val correctAnswer = "C"
                Column(modifier = Modifier.widthIn(max = 600.dp)) {
                    QuizOption("A", "Encryption", showAnswer, correctAnswer == "A")
                    QuizOption("B", "Firewall", showAnswer, correctAnswer == "B")
                    QuizOption("C", "Database attack", showAnswer, correctAnswer == "C")
                    QuizOption("D", "Protocol", showAnswer, correctAnswer == "D")
                }

                Spacer(Modifier.height(40.dp))

                // Timer (Show Answer နှိပ်ပြီးရင် ပျောက်သွားမယ်)
                if (!showAnswer) {
                    CircularTimer(
                        onFinish = {
                            isTimeUp = true
                            SoundPlayer.play("end.wav")
                        },
                        playCountdown = { SoundPlayer.play("alarm10.wav") }
                    )
                }

                Spacer(Modifier.height(30.dp))

                // --- Control Buttons Flow ---
                Row {
                    if (isTimeUp && !showAnswer) {
                        Button(
                            onClick = {
                                showAnswer = true
                                SoundPlayer.play("reveal.wav")
                            },
                            modifier = Modifier.height(50.dp).width(200.dp),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF9800))
                        ) {
                            Text("SHOW ANSWER", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (showAnswer) {
                        Button(
                            onClick = {
                                // Reset for next question
                                showAnswer = false
                                isTimeUp = false
                                currentQuestionIndex++
                            },
                            modifier = Modifier.height(50.dp).width(200.dp),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2196F3))
                        ) {
                            Text("NEXT QUESTION", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuizOption(letter: String, text: String, showAnswer: Boolean, isCorrect: Boolean) {
    // အဖြေမှန်ရင် အစိမ်း၊ မဟုတ်ရင် မှိန်သွားမယ်
    val backgroundColor = when {
        showAnswer && isCorrect -> Color(0xFF2E7D32) // Dark Green
        showAnswer && !isCorrect -> Color.White.copy(alpha = 0.1f) // Dimmed
        else -> Color.White.copy(alpha = 0.15f) // Default
    }

    val textColor = if (showAnswer && !isCorrect) Color.Gray else Color.White
    val borderColor = if (showAnswer && isCorrect) Color.Green else Color.Transparent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp)),
        elevation = 0.dp,
        backgroundColor = backgroundColor,
        border = if (borderColor != Color.Transparent) androidx.compose.foundation.BorderStroke(2.dp, borderColor) else null
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(if (showAnswer && isCorrect) Color.Green else Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(letter, fontWeight = FontWeight.ExtraBold, color = if (showAnswer && isCorrect) Color.Black else Color.White)
            }

            Spacer(Modifier.width(20.dp))

            Text(text, fontSize = 20.sp, color = textColor, fontWeight = FontWeight.Medium)
        }
    }
}