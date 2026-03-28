package utils

import java.io.BufferedInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

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
    private var clip: Clip? = null // Clip ကို reference သိမ်းထားဖို့ variable ထည့်မယ်

    fun play(sound: String) {
        try {
            // အရင်ပွင့်နေတဲ့ sound ရှိရင် ပိတ်လိုက်မယ်
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
                    it.stop() // အသံကို ရပ်မယ်
                }
                it.close() // resource ကို ပြန်ပိတ်မယ်
            }
            clip = null
        } catch (e: Exception) {
            println("Stop Error: ${e.message}")
        }
    }
}