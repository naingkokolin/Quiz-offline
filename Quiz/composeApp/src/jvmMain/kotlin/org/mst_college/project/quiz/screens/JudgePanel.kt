package org.mst_college.project.quiz.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.Question
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Composable
fun JudgePanel() {

    var category by remember { mutableStateOf("") }
    var question by remember { mutableStateOf("") }

    var optionA by remember { mutableStateOf("") }
    var optionB by remember { mutableStateOf("") }
    var optionC by remember { mutableStateOf("") }
    var optionD by remember { mutableStateOf("") }

    var correct by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(40.dp)
    ) {

        Text("Judge Panel", style = MaterialTheme.typography.h4)

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") }
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = question,
            onValueChange = { question = it },
            label = { Text("Question") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = optionA,
            onValueChange = { optionA = it },
            label = { Text("Option A") }
        )

        OutlinedTextField(
            value = optionB,
            onValueChange = { optionB = it },
            label = { Text("Option B") }
        )

        OutlinedTextField(
            value = optionC,
            onValueChange = { optionC = it },
            label = { Text("Option C") }
        )

        OutlinedTextField(
            value = optionD,
            onValueChange = { optionD = it },
            label = { Text("Option D") }
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = correct,
            onValueChange = { correct = it.uppercase() },
            label = { Text("Correct Answer (A/B/C/D)") }
        )

        Spacer(Modifier.height(20.dp))

        Row {

            Button(onClick = {

                val q = Question(
                    category,
                    question,
                    optionA,
                    optionB,
                    optionC,
                    optionD,
                    correct
                )

                saveQuestion(q)

                category = ""
                question = ""
                optionA = ""
                optionB = ""
                optionC = ""
                optionD = ""
                correct = ""

            }) {
                Text("Add Question")
            }

            Spacer(Modifier.width(10.dp))

            Button(onClick = {
                importQuestions()
            }) {
                Text("Import JSON")
            }
        }
    }
}

fun saveQuestion(question: Question) {

    val file = File("questions.json")

    val list = if (file.exists()) {
        Json.decodeFromString<List<Question>>(file.readText())
    } else {
        emptyList()
    }

    val updated = list + question

    file.writeText(Json.encodeToString(updated))
}

fun importQuestions() {

    val file = File("questions_import.json")

    if (!file.exists()) return

    val questions =
        Json.decodeFromString<List<Question>>(file.readText())

    val database = File("questions.json")

    val current = if (database.exists()) {
        Json.decodeFromString<List<Question>>(database.readText())
    } else {
        emptyList()
    }

    val updated = current + questions

    database.writeText(Json.encodeToString(updated))
}