package org.mst_college.project.quiz.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import settings.Settings
import settings.SettingsManager

@Composable
fun SettingsScreen() {

    var title by remember { mutableStateOf("") }
    var logoPath by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val s = SettingsManager.load()
        title = s.title
        logoPath = s.logoPath
    }

    Column(Modifier.padding(30.dp)) {

        Text("Application Title")

        TextField(
            value = title,
            onValueChange = { title = it }
        )

        Spacer(Modifier.height(16.dp))

        Text("Logo Image Path")

        TextField(
            value = logoPath,
            onValueChange = { logoPath = it }
        )

        Spacer(Modifier.height(20.dp))

        Button(onClick = {

            SettingsManager.save(
                Settings(
                    title = title,
                    logoPath = logoPath
                )
            )

        }) {
            Text("Save")
        }
    }
}