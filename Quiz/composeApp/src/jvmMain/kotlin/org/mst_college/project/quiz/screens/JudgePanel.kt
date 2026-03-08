package org.mst_college.project.quiz.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import model.Question
import utils.QuestionManager
import utils.QuestionManager.importQuestions

@Composable
fun JudgePanel(onBack: () -> Unit) {
    var category by remember { mutableStateOf("") }
    var question by remember { mutableStateOf("") }
    var optionA by remember { mutableStateOf("") }
    var optionB by remember { mutableStateOf("") }
    var optionC by remember { mutableStateOf("") }
    var optionD by remember { mutableStateOf("") }
    var correct by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(20.dp)
    ) {
        // --- Header with Back Button ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
            }
            Spacer(Modifier.width(10.dp))
            Text("Judge Panel", style = MaterialTheme.typography.h4, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(20.dp))

        // --- Input Fields ---
        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category (e.g., IT, History)") },
                    modifier = Modifier.width(300.dp)
                )

                Spacer(Modifier.height(15.dp))

                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text("Question Text") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(15.dp))

                // Options in Grid-like Layout
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        OptionField("A", optionA) { optionA = it }
                        OptionField("C", optionC) { optionC = it }
                    }
                    Spacer(Modifier.width(20.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        OptionField("B", optionB) { optionB = it }
                        OptionField("D", optionD) { optionD = it }
                    }
                }

                Spacer(Modifier.height(15.dp))

                OutlinedTextField(
                    value = correct,
                    onValueChange = { if (it.length <= 1) correct = it.uppercase() },
                    label = { Text("Correct Answer (A, B, C, or D)") },
                    modifier = Modifier.width(250.dp)
                )

                Spacer(Modifier.height(30.dp))

                // --- Buttons ---
                Row {
                    Button(
                        onClick = {
                            if (category.isNotBlank() && question.isNotBlank()) {
                                val q = Question(category, question, optionA, optionB, optionC, optionD, correct)
                                QuestionManager.saveQuestion(q)
                                // Clear fields
                                question = ""; optionA = ""; optionB = ""; optionC = ""; optionD = ""; correct = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50)),
                        modifier = Modifier.height(50.dp).width(180.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text("Add Question", color = Color.White)
                    }

                    Spacer(Modifier.width(15.dp))

                    OutlinedButton(
                        onClick = { importQuestions() },
                        modifier = Modifier.height(50.dp).width(180.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.FileUpload, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Import JSON")
                    }
                }
            }
        }
    }
}

@Composable
fun OptionField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Option $label") },
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
    )
}