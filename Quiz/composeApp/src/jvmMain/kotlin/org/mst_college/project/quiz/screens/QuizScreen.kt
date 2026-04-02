package org.mst_college.project.quiz.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.text.style.TextAlign
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

    var showExitDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val loadedQuestions = QuestionManager.getAllQuestions().filter { !it.isUsed }

        if (loadedQuestions.isNotEmpty()) {
            val shuffled = loadedQuestions.shuffled()
            val limit = settings.SettingsManager.load().questionLimit
            val limitedQuestions = if (shuffled.size > limit) shuffled.take(limit) else shuffled
            allQuestions.addAll(limitedQuestions)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            SoundPlayer.stop()
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Quit Quiz?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to exit? Your progress for this session will be lost.") },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        SoundPlayer.stop()
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) { Text("Exit", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) { Text("Cancel") }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    val primaryGradient = Brush.verticalGradient(listOf(Color(0xFF2E1A6B), Color(0xFF100A2C)))

    Box(modifier = Modifier.fillMaxSize().background(primaryGradient)) {
        IconButton(
            onClick = {
                if (isStarted) showExitDialog = true else onBack()
            }, modifier = Modifier.padding(20.dp).align(Alignment.TopStart)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }

        if (allQuestions.isEmpty() && isStarted) {
            Text("No questions found!", color = Color.White, modifier = Modifier.align(Alignment.Center))
        } else if (isStarted && currentQuestionIndex < allQuestions.size) {

            key(currentQuestionIndex) {
                val currentQ = allQuestions[currentQuestionIndex]

                Column(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Question Counter
                    Text(
                        "QUESTION ${currentQuestionIndex + 1} OF ${allQuestions.size}",
                        fontSize = 24.sp,
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
                        Text(
                            text = currentQ.question,
                            fontSize = 46.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = 45.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(Modifier.height(40.dp))

                    // Answer / Options Area
                    Box(modifier = Modifier.height(400.dp).widthIn(max = 850.dp), contentAlignment = Alignment.Center) {
                        this@Column.AnimatedVisibility(
                            visible = !isTimeUp && !showAnswer,
                            enter = fadeIn(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column {
                                QuizOption("A", currentQ.option_a)
                                QuizOption("B", currentQ.option_b)
                                QuizOption("C", currentQ.option_c)
                                QuizOption("D", currentQ.option_d)
                            }
                        }

                        this@Column.AnimatedVisibility(
                            visible = showAnswer,
                            enter = scaleIn() + fadeIn(),
                            exit = scaleOut(animationSpec = tween(100)) + fadeOut()
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(240.dp)
                                        .background(Color(0xFF4CAF50).copy(alpha = 0.9f), shape = CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = currentQ.correct_answer ?: "?",
                                        fontSize = 140.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                }
                                Spacer(Modifier.height(20.dp))
                                Text(
                                    "CORRECT ANSWER",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF4CAF50)
                                )
                            }
                        }

                        if (isTimeUp && !showAnswer) {
                            Text(
                                "TIME IS UP!",
                                fontSize = 60.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Red
                            )
                        }
                    }

                    Spacer(Modifier.height(30.dp))

                    // Control Area
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.height(180.dp)) {
                        if (!isTimeUp && !showAnswer) {
                            CircularTimer(
                                totalTime = settings.SettingsManager.load().timerSeconds,
                                key = currentQuestionIndex,
                                onFinish = {
                                    isTimeUp = true
//                                    SoundPlayer.play("timeup.wav")
                                },
                                playCountdown = { SoundPlayer.play("10sec-countdown.wav") }
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (isTimeUp && !showAnswer) {
                                Button(
                                    onClick = {
                                        showAnswer = true
                                        val currentQ = allQuestions[currentQuestionIndex]
                                        QuestionManager.markAsUsed(listOf(currentQ)) },
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
fun QuizOption(letter: String, text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .heightIn(min = 70.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.White.copy(alpha = 0.1f),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 30.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "$letter:", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.Cyan)
            Spacer(Modifier.width(20.dp))
            Text(text = text, fontSize = 26.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}

@Composable
fun StartButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(220.dp).clip(CircleShape),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50), contentColor = Color.White),
        elevation = ButtonDefaults.elevation(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Start", modifier = Modifier.size(80.dp))
            Text("START QUIZ", fontWeight = FontWeight.Bold, fontSize = 20.sp, letterSpacing = 2.sp)
        }
    }
}