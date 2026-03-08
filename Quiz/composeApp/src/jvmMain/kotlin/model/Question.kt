package model

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val category: String,
    val question: String,
    val option_a: String,
    val option_b: String,
    val option_c: String,
    val option_d: String,
    val correct_answer: String
)