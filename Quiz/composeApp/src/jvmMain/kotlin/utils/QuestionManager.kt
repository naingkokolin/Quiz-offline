package utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import model.Question
import java.io.File
import kotlin.plus

//object QuestionManager {
//
//    private val jsonWorker = Json {
//        ignoreUnknownKeys = true
//        prettyPrint = true
//    }
//
//    private val databaseFile = File("questions.json")
//
//    fun saveQuestion(newQuestion: Question) {
//        try {
//            val list = if (databaseFile.exists() && databaseFile.readText().isNotBlank()) {
//                jsonWorker.decodeFromString<List<Question>>(databaseFile.readText())
//            } else {
//                emptyList()
//            }
//
//            val updated = list + newQuestion
//            databaseFile.writeText(jsonWorker.encodeToString(updated))
//            println("Saved to: ${databaseFile.absolutePath}")
//        } catch (e: Exception) {
//            databaseFile.writeText(jsonWorker.encodeToString(listOf(newQuestion)))
//        }
//    }
//
//    fun importQuestions() {
//        val importFile = File("questions_import.json")
//        if (!importFile.exists() || importFile.readText().isBlank()) return
//
//        try {
//            val newOnes = jsonWorker.decodeFromString<List<Question>>(importFile.readText())
//            val currentOnes = if (databaseFile.exists() && databaseFile.readText().isNotBlank()) {
//                jsonWorker.decodeFromString<List<Question>>(databaseFile.readText())
//            } else {
//                emptyList()
//            }
//
//            databaseFile.writeText(jsonWorker.encodeToString(currentOnes + newOnes))
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    fun getAllQuestions(): List<Question> {
//        val databaseFile = File("questions.json")
//        return try {
//            if (databaseFile.exists() && databaseFile.readText().isNotBlank()) {
//                Json.decodeFromString<List<Question>>(databaseFile.readText())
//            } else {
//                emptyList()
//            }
//        } catch (e: Exception) {
//            emptyList()
//        }
//    }
//
//    fun deleteQuestion(questionToDelete: Question) {
//        val all = getAllQuestions().toMutableList()
//        all.removeAll { it.question == questionToDelete.question }
//        saveAllQuestions(all)
//    }
//
//    fun importQuestionsFromFile(file: File) {
//        try {
//            val jsonContent = file.readText()
//            val newQuestions = Json.decodeFromString<List<Question>>(jsonContent)
//            val currentQuestions = getAllQuestions().toMutableList()
//
//            currentQuestions.addAll(newQuestions)
//            saveAllQuestions(currentQuestions)
//        } catch (e: Exception) {
//            println("Import Error: ${e.message}")
//        }
//    }
//
//    private val historyFile = File("quiz_history.json")
//
//    fun saveRoundHistory(questions: List<Question>) {
//        try {
//            val timestamp = java.time.LocalDateTime.now().toString()
//            val roundData = mapOf(
//                "date" to timestamp,
//                "questions" to questions
//            )
//
//            val existingHistory = if (historyFile.exists()) {
//                jsonWorker.decodeFromString<List<Map<String, Any>>>(historyFile.readText())
//            } else {
//                emptyList()
//            }
//
//            // အသစ်ပေါင်းထည့်ပြီး ပြန်သိမ်းမယ်
//            val updatedHistory = existingHistory + roundData
//            historyFile.writeText(jsonWorker.encodeToString(updatedHistory))
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    // QuestionManager.kt ထဲမှာ ဒါလေး ထည့်လိုက်ပါ
//    fun removeFromUsed(question: Question) {
//        val all = getAllQuestions().toMutableList()
//        // မေးခွန်းစာသားချင်း တူတာကို ရှာပြီး isUsed ကို false ပြန်လုပ်မယ်
//        val target = all.find { it.question == question.question }
//        if (target != null) {
//            target.isUsed = false
//            saveAllQuestions(all) // ပြင်ပြီးသားစာရင်းကို JSON ထဲ ပြန်သိမ်းမယ်
//        }
//    }
//
//    // Helper function to save the whole list
//    private fun saveAllQuestions(questions: List<Question>) {
//        val file = File("questions.json")
//        val jsonString = Json.encodeToString(questions)
//        file.writeText(jsonString)
//    }
//}

object QuestionManager {

    private val jsonWorker = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    private val databaseFile = File("questions.json")

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

    private val historyFile = File("quiz_history.json")

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

    fun importQuestionsFromFile(file: File) {
        try {
            if (!file.exists()) return

            val jsonContent = file.readText()

            val newQuestions = jsonWorker.decodeFromString<List<Question>>(jsonContent)

            val currentQuestions = getAllQuestions().toMutableList()
            currentQuestions.addAll(newQuestions)

            saveAllQuestions(currentQuestions)
            println("Imported ${newQuestions.size} questions from ${file.name}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}