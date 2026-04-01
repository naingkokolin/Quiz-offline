package org.mst_college.project.quiz

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
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
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(40.dp)
//                .padding(end = 10.dp),
//            horizontalArrangement = Arrangement.End,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
////            // Minimize Button
////            WindowControlButton(Icons.Default.Remove, Color.Gray) {
////                // Desktop context မှာဆိုရင် window state ကို minimize လုပ်တဲ့ logic ထည့်နိုင်ပါတယ်
////            }
////
////            // Maximize/Restore Button
////            WindowControlButton(Icons.Default.Square, Color.Gray) {
////                // Window size toggle logic
////            }
//
//            WindowControlButton(
//                icon = Icons.Default.Close,
//                hoverColor = Color.Red,
//                iconColor = Color.DarkGray
//            ) {
//                java.lang.System.exit(0)
//            }
//        }
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
                    // currentUser ရှိမှ Home ကို ပြမယ်၊ မရှိရင် Login ကို ပြန်သွားမယ်
                    currentUser?.let { user ->
                        HomeScreen(
                            loggedInUser = user, // ဒီမှာ parameter ပို့ပေးရပါမယ်
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

//@OptIn(ExperimentalComposeUiApi::class)
//@Composable
//fun WindowControlButton(
//    icon: androidx.compose.ui.graphics.vector.ImageVector,
//    iconColor: Color = Color.Gray,
//    hoverColor: Color = Color.LightGray.copy(alpha = 0.3f),
//    onClick: () -> Unit
//) {
//    var isHovered by remember { mutableStateOf(false) }
//
//    Box(
//        modifier = Modifier
//            .size(45.dp, 30.dp)
//            .background(if (isHovered) hoverColor else Color.Transparent)
//            .clickable { onClick() }
//            .onPointerEvent(androidx.compose.ui.input.pointer.PointerEventType.Enter) { isHovered = true }
//            .onPointerEvent(androidx.compose.ui.input.pointer.PointerEventType.Exit) { isHovered = false },
//        contentAlignment = Alignment.Center
//    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = null,
//            modifier = Modifier.size(16.dp),
//            tint = if (isHovered && icon == Icons.Default.Close) Color.White else iconColor
//        )
//    }
//}