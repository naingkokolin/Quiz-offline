package org.mst_college.project.quiz.ui.components

import androidx.compose.animation.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun AnimatedQuestion(text: String) {

    AnimatedContent(
        targetState = text,
        transitionSpec = {
            slideInHorizontally { it } togetherWith
                    slideOutHorizontally { -it }
        }
    ) {
        Text(
            it,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }
}