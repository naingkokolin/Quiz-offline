package org.mst_college.project.quiz

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Quiz Tournament",
    ) {
        App()
    }
}