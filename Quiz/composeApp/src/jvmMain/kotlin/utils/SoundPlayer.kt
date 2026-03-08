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
            // Resource ကို Stream အနေနဲ့ ဖတ်တာက Jar file ထုပ်ပြီးရင်တောင် ပိုအလုပ်လုပ်ပါတယ်
            val inputStream = javaClass.classLoader.getResourceAsStream(sound)
                ?: throw Exception("မသိရသေးသော ဖိုင်အမည်: $sound (လမ်းကြောင်းကို ပြန်စစ်ပါ)")

            // BufferedInputStream သုံးတာက အသံထစ်တာကို ကာကွယ်ပေးတယ်
            val bufferedIn = BufferedInputStream(inputStream)
            val audioStream = AudioSystem.getAudioInputStream(bufferedIn)

            val clip = AudioSystem.getClip()
            clip.open(audioStream)
            clip.start()

            // အသံပြီးသွားရင် clip ကို ပြန်ပိတ်ချင်ရင် Listener ထည့်လို့ရပါတယ် (Optional)
        } catch (e: Exception) {
            println("Sound Player Error: ${e.message}")
        }
    }
}