package utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import model.Question
import java.io.File

object QuestionManager {

    private val appDataPath = System.getenv("APPDATA") ?: System.getProperty("user.home")
    private val appFolder = File(appDataPath, "QuizTournament").apply { mkdirs() }

    private val databaseFile = File(appFolder, "questions.json")
    private val historyFile = File(appFolder, "quiz_history.json")

    private val jsonWorker = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    fun markAsUsed(answeredQuestions: List<Question>) {
        val all = getAllQuestions().toMutableList()
        answeredQuestions.forEach { answered ->
            all.find { it.question == answered.question }?.let {
                it.isUsed = true
            }
        }
        saveAllQuestions(all)
    }

    fun saveQuestion(newQuestion: Question) {
        try {
            val list = getAllQuestions().toMutableList()
            list.add(newQuestion)
            saveAllQuestions(list)
        } catch (e: Exception) {
            saveAllQuestions(listOf(newQuestion))
        }
    }

    fun getAllQuestions(): List<Question> {
        return try {
            if (databaseFile.exists() && databaseFile.readText().isNotBlank()) {
                jsonWorker.decodeFromString<List<Question>>(databaseFile.readText())
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun deleteQuestion(questionToDelete: Question) {
        val all = getAllQuestions().toMutableList()
        all.removeAll { it.question == questionToDelete.question }
        saveAllQuestions(all)
    }

    fun removeFromUsed(question: Question) {
        val all = getAllQuestions().toMutableList()
        all.find { it.question == question.question }?.let {
            it.isUsed = false
        }
        saveAllQuestions(all)
    }

    private fun saveAllQuestions(questions: List<Question>) {
        try {
            val jsonString = jsonWorker.encodeToString(questions)
            databaseFile.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
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

    fun saveRoundHistory(questions: List<Question>) {
        try {
            val timestamp = java.time.LocalDateTime.now().toString()
            val roundData = mapOf(
                "date" to timestamp,
                "questions" to questions
            )

            val existingHistory = if (historyFile.exists()) {
                jsonWorker.decodeFromString<List<Map<String, Any>>>(historyFile.readText())
            } else {
                emptyList()
            }


            val updatedHistory = existingHistory + roundData
            historyFile.writeText(jsonWorker.encodeToString(updatedHistory))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

//    fun importQuestionsFromFile(file: File) {
//        try {
//            if (!file.exists()) return
//
//            val jsonContent = file.readText()
//
//            val newQuestions = jsonWorker.decodeFromString<List<Question>>(jsonContent)
//
//            val currentQuestions = getAllQuestions().toMutableList()
//            currentQuestions.addAll(newQuestions)
//
//            saveAllQuestions(currentQuestions)
//            println("Imported ${newQuestions.size} questions from ${file.name}")
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    fun importQuestionsFromFile(file: File) {
        try {
            if (!file.exists()) return

            val jsonContent = file.readText()
            val newQuestions = jsonWorker.decodeFromString<List<Question>>(jsonContent)

            val currentQuestions = getAllQuestions().toMutableList()

            val existingQuestionTexts = currentQuestions.map { it.question }.toSet()

            val uniqueNewQuestions = newQuestions.filter { it.question !in existingQuestionTexts }

            if (uniqueNewQuestions.isNotEmpty()) {
                currentQuestions.addAll(uniqueNewQuestions)
                saveAllQuestions(currentQuestions)
                println("Imported ${uniqueNewQuestions.size} unique questions. (${newQuestions.size - uniqueNewQuestions.size} duplicates skipped)")
            } else {
                println("No new unique questions found in the file.")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}