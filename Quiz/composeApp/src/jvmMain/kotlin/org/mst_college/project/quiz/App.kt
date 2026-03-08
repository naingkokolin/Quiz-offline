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

@Composable
fun App() {
    // Navigation History ကို သိမ်းမည့် Stack
    val navigationStack = remember { mutableStateListOf(Screen.WELCOME) }
    val currentScreen = navigationStack.last()

    // Keyboard Focus အတွက် Requester
    val focusRequester = remember { FocusRequester() }

    // Back ပြန်သွားမည့် Logic
    val goBack = {
        if (navigationStack.size > 1) {
            navigationStack.removeAt(navigationStack.size - 1)
        }
    }

    // ရှေ့သို့သွားမည့် Logic
    val navigateTo = { nextScreen: Screen ->
        navigationStack.add(nextScreen)
    }

    // အပြင်ဆုံးက Box နဲ့ပတ်ပြီး Keyboard Event ကို ဖမ်းမယ်
    Box(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .focusable() // Keyboard event လက်ခံနိုင်ရန်
            .onKeyEvent { event ->
                // Esc Key ကို နှိပ်ပြီး ပြန်လွှတ်လိုက်လျှင် (KeyUp) Back သွားမယ်
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