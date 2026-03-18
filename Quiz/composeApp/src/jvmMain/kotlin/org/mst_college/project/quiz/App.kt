package org.mst_college.project.quiz

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import org.mst_college.project.quiz.navigation.Screen
import org.mst_college.project.quiz.screens.*

enum class Screen {
    Login, Register, Home, Quiz, JudgePanel
}

@Composable
fun App() {
    val navigationStack = remember { mutableStateListOf(Screen.WELCOME) }
    val currentScreen = navigationStack.last()

    val focusRequester = remember { FocusRequester() }

    val goBack = {
        if (navigationStack.size > 1) {
            navigationStack.removeAt(navigationStack.size - 1)
        }
    }

    val navigateTo = { nextScreen: Screen ->
        navigationStack.add(nextScreen)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .focusable() // Keyboard event လက်ခံနိုင်ရန်
            .onKeyEvent { event ->
                if (event.key == Key.Escape && event.type == KeyEventType.KeyUp) {
                    goBack()
                    true // Event ကို ဒီမှာတင် ရပ်လိုက်ပြီဟု သတ်မှတ်
                } else {
                    false
                }
            }
    ) {
        when (currentScreen) {
            Screen.WELCOME -> WelcomeScreen {
                navigateTo(Screen.HOME)
            }

            Screen.HOME -> HomeScreen(
                onQuiz = { navigateTo(Screen.QUIZ) },
                onJudge = { navigateTo(Screen.JUDGE) },
                onSettings = { navigateTo(Screen.SETTINGS) }
            )

            // Screen တစ်ခုချင်းစီကို goBack function လှမ်းပေးလိုက်သည်
            Screen.QUIZ -> QuizScreen(onBack = goBack)
            Screen.JUDGE -> JudgePanel(onBack = goBack)
            Screen.SETTINGS -> SettingsScreen(onBack = goBack)
        }

        // App စဖွင့်သည်နှင့် Keyboard focus ရအောင် လုပ်ပေးရမည်
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}