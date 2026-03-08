package utils

import javax.sound.sampled.AudioSystem

object SoundPlayer {

    fun play(sound: String) {

        val url = SoundPlayer::class.java.getResource("/$sound")

        val audioStream = AudioSystem.getAudioInputStream(url)

        val clip = AudioSystem.getClip()

        clip.open(audioStream)

        clip.start()
    }
}