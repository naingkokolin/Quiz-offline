package settings

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import java.io.File

@Serializable
data class Settings(
    val title: String = "Quiz Tournament",
    val logoPath: String = "logo.png"
)

object SettingsManager {
    private val file = File("settings.json")

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    fun load(): Settings {
        return try {
            if (!file.exists()) {
                val default = Settings()
                save(default)
                default
            } else {
                json.decodeFromString<Settings>(file.readText())
            }
        } catch (e: Exception) {
            val default = Settings()
            save(default)
            default
        }
    }

    fun save(settings: Settings) {
        try {
            val jsonString = json.encodeToString(Settings.serializer(), settings)
            file.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}