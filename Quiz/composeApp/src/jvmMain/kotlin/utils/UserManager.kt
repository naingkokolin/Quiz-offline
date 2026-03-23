package utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import model.User
import java.io.File
import java.security.MessageDigest

object UserManager {

    private val jsonWorker = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    private val userFile = File("users.json")

    // Password ကို Hash ပြောင်းပေးတဲ့ function (SHA-256)
    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    // ၁။ User အသစ်ဆောက်ခြင်း (Register)
    fun register(username: String, password: String): Boolean {
        val users = getAllUsers().toMutableList()
        if (users.any { it.username == username }) return false

        // passwordHash အစား password လို့ ပြောင်းသုံးပါ
        val newUser = User(
            user_id = users.size + 1, // ID auto တိုးချင်ရင်
            username = username,
            password = hashPassword(password)
        )

        users.add(newUser)
        saveAllUsers(users)
        return true
    }

    // ၂။ Login စစ်ဆေးခြင်း
    fun login(username: String, password: String): User? {
        val users = getAllUsers()
        val inputHash = hashPassword(password)
        // it.passwordHash အစား it.password လို့ ပြောင်းပါ
        return users.find { it.username == username && it.password == inputHash }
    }

    // ၃။ Password ပြောင်းခြင်း
    fun changePassword(username: String, oldPw: String, newPw: String): Boolean {
        val users = getAllUsers().toMutableList()
        val oldHash = hashPassword(oldPw)
        // it.passwordHash အစား it.password လို့ ပြောင်းပါ
        val index = users.indexOfFirst { it.username == username && it.password == oldHash }

        if (index != -1) {
            // .copy(password = ...) လို့ ပြောင်းပါ
            users[index] = users[index].copy(password = hashPassword(newPw))
            saveAllUsers(users)
            return true
        }
        return false
    }

    fun getAllUsers(): List<User> {
        return try {
            if (userFile.exists() && userFile.readText().isNotBlank()) {
                jsonWorker.decodeFromString<List<User>>(userFile.readText())
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveAllUsers(users: List<User>) {
        userFile.writeText(jsonWorker.encodeToString(users))
    }
}