package org.mst_college.project.quiz.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mst_college.project.quiz.ui.components.AnimatedQuestion
import org.mst_college.project.quiz.ui.components.CircularTimer
import utils.SoundPlayer

@Composable
fun QuizScreen() {

    var showAnswer by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "Network Security",
            fontSize = 22.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(20.dp))

        AnimatedQuestion(
            "What is SQL Injection?"
        )

        Spacer(Modifier.height(30.dp))

        Column {

            QuizOption("A", "Encryption")
            QuizOption("B", "Firewall")
            QuizOption("C", "Database attack")
            QuizOption("D", "Protocol")
        }

        Spacer(Modifier.height(40.dp))

        CircularTimer(
            onFinish = { SoundPlayer.play("end.wav") },
            playCountdown = { SoundPlayer.play("alarm10.wav") }
        )

        Spacer(Modifier.height(20.dp))

        Row {

            Button(onClick = { showAnswer = true }) {
                Text("Show Answer")
            }

            Spacer(Modifier.width(12.dp))

            Button(onClick = { /* next question */ }) {
                Text("Next Question")
            }
        }

        if (showAnswer) {
            Text(
                "Correct Answer: C",
                fontSize = 26.sp,
                color = Color.Green
            )
        }
    }
}

@Composable
fun QuizOption(letter: String, text: String) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Row(
            modifier = Modifier.padding(16.dp)
        ) {

            Text("$letter)", fontWeight = FontWeight.Bold)

            Spacer(Modifier.width(10.dp))

            Text(text)
        }
    }
}