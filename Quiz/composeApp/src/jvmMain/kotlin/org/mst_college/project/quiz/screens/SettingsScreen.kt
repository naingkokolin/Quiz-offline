package org.mst_college.project.quiz.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import settings.Settings
import settings.SettingsManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var logoPath by remember { mutableStateOf("") }
    var questionLimit by remember { mutableStateOf("10") }
    var timerSeconds by remember { mutableStateOf("30") }
    var isDarkMode by remember { mutableStateOf(true) }

    // Load existing settings
    LaunchedEffect(Unit) {
        val s = SettingsManager.load()
        title = s.title
        logoPath = s.logoPath
        questionLimit = s.questionLimit.toString()
        timerSeconds = s.timerSeconds.toString()
        isDarkMode = s.isDarkMode
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 40.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(Modifier.height(10.dp))

            // --- General Section ---
            SettingsSectionTitle("General Appearance")

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Application Title") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Title, null) }
            )

            OutlinedTextField(
                value = logoPath,
                onValueChange = { logoPath = it },
                label = { Text("Logo Image Path (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Image, null) }
            )

            // Dark Mode Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DarkMode, null)
                    Spacer(Modifier.width(10.dp))
                    Text("Dark Mode Theme", fontSize = 16.sp)
                }
                Switch(checked = isDarkMode, onCheckedChange = { isDarkMode = it })
            }

            HorizontalDivider()

            // --- Quiz Rules Section ---
            SettingsSectionTitle("Quiz Rules")

            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                OutlinedTextField(
                    value = questionLimit,
                    onValueChange = { if (it.all { char -> char.isDigit() }) questionLimit = it },
                    label = { Text("Questions per Round") },
                    modifier = Modifier.weight(1f),
                    leadingIcon = { Icon(Icons.Default.FormatListNumbered, null) }
                )

                OutlinedTextField(
                    value = timerSeconds,
                    onValueChange = { if (it.all { char -> char.isDigit() }) timerSeconds = it },
                    label = { Text("Timer (Seconds)") },
                    modifier = Modifier.weight(1f),
                    leadingIcon = { Icon(Icons.Default.Timer, null) }
                )
            }

            Spacer(Modifier.height(30.dp))

            // --- Save Button ---
            Button(
                onClick = {
                    SettingsManager.save(
                        Settings(
                            title = title,
                            logoPath = logoPath,
                            questionLimit = questionLimit.toIntOrNull() ?: 10,
                            timerSeconds = timerSeconds.toIntOrNull() ?: 30,
                            isDarkMode = isDarkMode
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = ShapeDefaults.Medium
            ) {
                Icon(Icons.Default.Save, null)
                Spacer(Modifier.width(8.dp))
                Text("Save All Settings", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(50.dp))
        }
    }
}

@Composable
fun SettingsSectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}