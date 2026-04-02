package utils

import java.io.BufferedInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

object SoundPlayer {
    private var clip: Clip? = null

    fun play(sound: String) {
        try {
            stop()

            val inputStream = javaClass.classLoader.getResourceAsStream(sound)
                ?: throw Exception("Unknown file: $sound (Check file path!)")

            val bufferedIn = BufferedInputStream(inputStream)
            val audioStream = AudioSystem.getAudioInputStream(bufferedIn)

            clip = AudioSystem.getClip()
            clip?.open(audioStream)
            clip?.start()

        } catch (e: Exception) {
            println("Sound Player Error: ${e.message}")
        }
    }

    fun stop() {
        try {
            clip?.let {
                if (it.isRunning) {
                    it.stop()
                }
                it.close()
            }
            clip = null
        } catch (e: Exception) {
            println("Stop Error: ${e.message}")
        }
    }
}