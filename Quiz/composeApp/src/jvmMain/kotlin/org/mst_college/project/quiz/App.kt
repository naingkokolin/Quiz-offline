package org.mst_college.project.quiz

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import org.mst_college.project.quiz.navigation.Screen
import org.mst_college.project.quiz.screens.*

@Composable
fun App() {
    val navigationStack = remember { mutableStateListOf(Screen.Login) }
    val currentScreen = navigationStack.last()

    var currentUser by remember { mutableStateOf<model.User?>(null) }

    val focusRequester = remember { FocusRequester() }

    val goBack = {
        if (navigationStack.size > 1) {
            navigationStack.removeAt(navigationStack.size - 1)
        }
    }

    val navigateTo = { nextScreen: Screen ->
        navigationStack.add(nextScreen)
    }

    val loginSuccess = { user: model.User ->
        currentUser = user
        navigationStack.clear()
        navigationStack.add(Screen.Welcome)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { event ->
                if (event.key == Key.Escape && event.type == KeyEventType.KeyUp && navigationStack.size > 1) {
                    goBack()
                    true
                } else {
                    false
                }
            }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            when (currentScreen) {
                Screen.Login -> LoginScreen(
                    onLoginSuccess = { user -> loginSuccess(user) },
                    onNavigateToRegister = { navigateTo(Screen.Register) }
                )

                Screen.Register -> RegisterScreen(
                    onRegisterSuccess = { goBack() },
                    onNavigateToLogin = { goBack() }
                )

                Screen.Welcome -> WelcomeScreen {
                    navigationStack.clear()
                    navigationStack.add(Screen.Home)
                }

                Screen.Home -> {
                    currentUser?.let { user ->
                        HomeScreen(
                            loggedInUser = user,
                            onQuiz = { navigateTo(Screen.Quiz) },
                            onJudge = { navigateTo(Screen.JudgePanel) },
                            onSettings = { navigateTo(Screen.Settings) }
                        )
                    } ?: run {
                        navigationStack.clear()
                        navigationStack.add(Screen.Login)
                    }
                }

                Screen.Quiz -> QuizScreen(onBack = goBack)
                Screen.JudgePanel -> JudgePanel(onBack = goBack)
                Screen.Settings -> SettingsScreen(onBack = goBack)
            }
        }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}