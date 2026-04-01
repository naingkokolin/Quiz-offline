package org.mst_college.project.quiz.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import settings.Settings
import settings.SettingsManager
import java.awt.FileDialog
import java.awt.Frame
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var logoPath by remember { mutableStateOf<String?>(null) }
    var questionLimit by remember { mutableStateOf("10") }
    var timerSeconds by remember { mutableStateOf("30") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Load existing settings
    LaunchedEffect(Unit) {
        val s = SettingsManager.load()
        title = s.title
        logoPath = s.logoPath
        questionLimit = s.questionLimit.toString()
        timerSeconds = s.timerSeconds.toString()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Application Logo", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = logoPath ?: "No logo selected",
                        onValueChange = { }, // Read-only ဖြစ်အောင် ထားမယ်
                        readOnly = true,
                        modifier = Modifier.weight(1f),
                        leadingIcon = { Icon(Icons.Default.Image, null) }
                    )

                    Button(
                        onClick = {
                            // Windows File Explorer ကို ခေါ်တဲ့ Logic
                            val fileDialog = FileDialog(null as Frame?, "Select Logo Image", FileDialog.LOAD)
                            fileDialog.file = "*.jpg;*.png;*.jpeg" // ပုံတွေပဲ ရွေးခိုင်းမယ်
                            fileDialog.isVisible = true

                            if (fileDialog.directory != null && fileDialog.file != null) {
                                logoPath = "${fileDialog.directory}${fileDialog.file}"
                            }
                        },
                        shape = ShapeDefaults.Small
                    ) {
                        Text("Browse")
                    }
                }
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
                        )
                    )

                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Settings saved successfully!",
                                duration = SnackbarDuration.Short
                            )
                        }
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