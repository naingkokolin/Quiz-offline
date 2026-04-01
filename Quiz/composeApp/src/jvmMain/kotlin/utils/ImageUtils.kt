package utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap

object ImageUtils {
    fun loadImageFromPath(path: String): ImageBitmap? {
        return try {
            val file = java.io.File(path)
            if (file.exists()) {
                val bytes = file.readBytes()
                org.jetbrains.skia.Image.makeFromEncoded(bytes).toComposeImageBitmap()
            } else null
        } catch (e: Exception) { null }
    }
}