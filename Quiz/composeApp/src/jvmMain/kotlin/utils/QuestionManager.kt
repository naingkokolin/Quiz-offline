package utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import model.Question
import java.io.File

object QuestionManager {

    private val jsonWorker = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    private val databaseFile = File("questions.json")

    fun saveQuestion(newQuestion: Question) {
        try {
            val list = if (databaseFile.exists() && databaseFile.readText().isNotBlank()) {
                jsonWorker.decodeFromString<List<Question>>(databaseFile.readText())
            } else {
                emptyList()
            }

            val updated = list + newQuestion
            databaseFile.writeText(jsonWorker.encodeToString(updated))
            println("Saved to: ${databaseFile.absolutePath}")
        } catch (e: Exception) {
            databaseFile.writeText(jsonWorker.encodeToString(listOf(newQuestion)))
        }
    }

    fun importQuestions() {
        val importFile = File("questions_import.json")
        if (!importFile.exists() || importFile.readText().isBlank()) return

        try {
            val newOnes = jsonWorker.decodeFromString<List<Question>>(importFile.readText())
            val currentOnes = if (databaseFile.exists() && databaseFile.readText().isNotBlank()) {
                jsonWorker.decodeFromString<List<Question>>(databaseFile.readText())
            } else {
                emptyList()
            }

            databaseFile.writeText(jsonWorker.encodeToString(currentOnes + newOnes))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAllQuestions(): List<Question> {
        val databaseFile = File("questions.json")
        return try {
            if (databaseFile.exists() && databaseFile.readText().isNotBlank()) {
                Json.decodeFromString<List<Question>>(databaseFile.readText())
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun deleteQuestion(questionToDelete: Question) {
        val all = getAllQuestions().toMutableList()
        all.removeAll { it.question == questionToDelete.question }
        saveAllQuestions(all)
    }

    fun importQuestionsFromFile(file: File) {
        try {
            val jsonContent = file.readText()
            val newQuestions = Json.decodeFromString<List<Question>>(jsonContent)
            val currentQuestions = getAllQuestions().toMutableList()

            // ရှိပြီးသား မေးခွန်းတွေထဲကို ပေါင်းထည့်မယ်
            currentQuestions.addAll(newQuestions)
            saveAllQuestions(currentQuestions)
        } catch (e: Exception) {
            println("Import Error: ${e.message}")
        }
    }

    // Helper function to save the whole list
    private fun saveAllQuestions(questions: List<Question>) {
        val file = File("questions.json")
        val jsonString = Json.encodeToString(questions)
        file.writeText(jsonString)
    }
}