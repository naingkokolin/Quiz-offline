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
}