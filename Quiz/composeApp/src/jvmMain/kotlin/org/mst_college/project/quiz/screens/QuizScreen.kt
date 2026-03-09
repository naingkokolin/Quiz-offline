package org.mst_college.project.quiz.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import model.Question
import org.mst_college.project.quiz.ui.components.CircularTimer
import utils.QuestionManager
import utils.SoundPlayer

@Composable
fun QuizScreen(onBack: () -> Unit) {
    val allQuestions = remember { mutableStateListOf<Question>() }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var isStarted by remember { mutableStateOf(false) }
    var showAnswer by remember { mutableStateOf(false) }
    var isTimeUp by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val loadedQuestions = QuestionManager.getAllQuestions()
        if (loadedQuestions.isNotEmpty()) {
            val shuffled = loadedQuestions.shuffled()
            val limit = settings.SettingsManager.load().questionLimit
            val limitedQuestions = if (shuffled.size > limit) shuffled.take(limit) else shuffled
            allQuestions.addAll(limitedQuestions)
        }
    }

    val primaryGradient = Brush.verticalGradient(listOf(Color(0xFF0D47A1), Color(0xFF000000)))

    Box(modifier = Modifier.fillMaxSize().background(primaryGradient)) {
        IconButton(onClick = onBack, modifier = Modifier.padding(20.dp).align(Alignment.TopStart)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }

        if (allQuestions.isEmpty() && isStarted) {
            Text("No questions found!", color = Color.White, modifier = Modifier.align(Alignment.Center))
        } else if (isStarted && currentQuestionIndex < allQuestions.size) {

            val currentQ = allQuestions[currentQuestionIndex]

            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Question Counter
                Text(
                    "QUESTION ${currentQuestionIndex + 1} OF ${allQuestions.size}",
                    fontSize = 20.sp,
                    color = Color.Cyan.copy(alpha = 0.8f),
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )

                Spacer(Modifier.height(30.dp))

                AnimatedVisibility(
                    visible = !isTimeUp,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = currentQ.question,
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = 45.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(Modifier.height(40.dp))
                    }
                }

                Column(modifier = Modifier.widthIn(max = 850.dp)) {
                    QuizOption("A", currentQ.option_a, showAnswer, currentQ.correct_answer == "A")
                    QuizOption("B", currentQ.option_b, showAnswer, currentQ.correct_answer == "B")
                    QuizOption("C", currentQ.option_c, showAnswer, currentQ.correct_answer == "C")
                    QuizOption("D", currentQ.option_d, showAnswer, currentQ.correct_answer == "D")
                }

                Spacer(Modifier.height(50.dp))

                // Timer or Control Buttons
                Box(contentAlignment = Alignment.Center, modifier = Modifier.height(200.dp)) {
                    if (!showAnswer) {
                        CircularTimer(
                            totalTime = settings.SettingsManager.load().timerSeconds,
                            key = currentQuestionIndex,
                            onFinish = {
                                isTimeUp = true
                            },
                            playCountdown = { SoundPlayer.play("10sec-countdown.wav") }
                        )
                    }

                    // Show Answer / Next Buttons
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (isTimeUp && !showAnswer) {
                            Button(
                                onClick = { showAnswer = true },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF9800)),
                                modifier = Modifier.height(65.dp).width(280.dp),
                                shape = RoundedCornerShape(32.dp),
                                elevation = ButtonDefaults.elevation(10.dp)
                            ) {
                                Text("SHOW ANSWER", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }

                        if (showAnswer) {
                            Button(
                                onClick = {
                                    if (currentQuestionIndex < allQuestions.size - 1) {
                                        showAnswer = false
                                        isTimeUp = false
                                        currentQuestionIndex++
                                        SoundPlayer.play("start.wav")
                                    } else {
                                        onBack()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2196F3)),
                                modifier = Modifier.height(65.dp).width(280.dp),
                                shape = RoundedCornerShape(32.dp)
                            ) {
                                Text(
                                    if (currentQuestionIndex < allQuestions.size - 1) "NEXT QUESTION" else "FINISH QUIZ",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                StartButton {
                    if (allQuestions.isNotEmpty()) {
                        isStarted = true
                        SoundPlayer.play("start.wav")
                    }
                }
            }
        }
    }
}

@Composable
fun QuizOption(letter: String, text: String, showAnswer: Boolean, isCorrect: Boolean) {
    val backgroundColor = when {
        showAnswer && isCorrect -> Color(0xFF1B5E20) // Dark Green
        showAnswer && !isCorrect -> Color.White.copy(alpha = 0.05f) // Dimmed
        else -> Color.White.copy(alpha = 0.12f) // Default
    }

    val textColor = if (showAnswer && !isCorrect) Color.Gray else Color.White
    val borderColor = if (showAnswer && isCorrect) Color.Green else Color.Transparent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .heightIn(min = 70.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = backgroundColor,
        elevation = if (showAnswer && isCorrect) 12.dp else 0.dp,
        border = if (showAnswer && isCorrect) androidx.compose.foundation.BorderStroke(3.dp, Color.Green) else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 30.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$letter:",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = if (showAnswer && isCorrect) Color.Green else Color.Cyan
            )
            Spacer(Modifier.width(20.dp))
            Text(
                text = text,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (showAnswer && !isCorrect) Color.Gray else Color.White
            )
        }
    }
}

@Composable
fun StartButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(220.dp)
            .clip(CircleShape),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF4CAF50),
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.elevation(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Start",
                modifier = Modifier.size(80.dp)
            )
            Text(
                "START QUIZ",
                style = androidx.compose.ui.text.TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    letterSpacing = 2.sp
                )
            )
        }
    }
}