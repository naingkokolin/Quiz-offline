package utils

import java.io.BufferedInputStream
import javax.sound.sampled.AudioSystem

//object SoundPlayer {
//
//    fun play(sound: String) {
//
//        val url = SoundPlayer::class.java.getResource("/$sound")
//
//        val audioStream = AudioSystem.getAudioInputStream(url)
//
//        val clip = AudioSystem.getClip()
//
//        clip.open(audioStream)
//
//        clip.start()
//    }
//}

object SoundPlayer {
    fun play(sound: String) {
        try {
            val inputStream = javaClass.classLoader.getResourceAsStream(sound)
                ?: throw Exception("Unknown file: $sound (Check file path!)")

            val bufferedIn = BufferedInputStream(inputStream)
            val audioStream = AudioSystem.getAudioInputStream(bufferedIn)

            val clip = AudioSystem.getClip()
            clip.open(audioStream)
            clip.start()

        } catch (e: Exception) {
            println("Sound Player Error: ${e.message}")
        }
    }
}