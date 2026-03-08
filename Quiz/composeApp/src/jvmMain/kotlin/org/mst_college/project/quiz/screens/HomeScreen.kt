package org.mst_college.project.quiz.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onQuiz: () -> Unit,
    onJudge: () -> Unit,
    onSettings: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            onClick = onQuiz,
            modifier = Modifier.width(220.dp).height(50.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
        ) {
            Text("Start Quiz", style = TextStyle(fontWeight = FontWeight.Bold))
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = onJudge, modifier = Modifier.width(220.dp)) {
            Text("Judge Panel")
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = onSettings, modifier = Modifier.width(220.dp)) {
            Text("Settings")
        }
    }
}