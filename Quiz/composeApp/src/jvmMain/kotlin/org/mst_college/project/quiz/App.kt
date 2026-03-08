package org.mst_college.project.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource
import org.mst_college.project.quiz.navigation.Screen
import org.mst_college.project.quiz.screens.HomeScreen
import org.mst_college.project.quiz.screens.JudgePanel
import org.mst_college.project.quiz.screens.QuizScreen
import org.mst_college.project.quiz.screens.SettingsScreen
import org.mst_college.project.quiz.screens.WelcomeScreen

import quiz.composeapp.generated.resources.Res
import quiz.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    var screen by remember { mutableStateOf(Screen.WELCOME) }

    when (screen) {
        Screen.WELCOME -> WelcomeScreen { screen = Screen.HOME }
        Screen.HOME -> HomeScreen(
            onQuiz = { screen = Screen.QUIZ },
            onJudge = { screen = Screen.JUDGE },
            onSettings = { screen = Screen.SETTINGS }
        )
        Screen.QUIZ -> QuizScreen()
        Screen.JUDGE -> JudgePanel()
        Screen.SETTINGS -> SettingsScreen()
    }
}