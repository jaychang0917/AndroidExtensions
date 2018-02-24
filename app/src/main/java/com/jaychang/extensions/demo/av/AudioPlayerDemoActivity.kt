package com.jaychang.extensions.demo.av

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.jaychang.extensions.av.AudioPlayer
import com.jaychang.extensions.demo.R

class AudioPlayerDemoActivity : AppCompatActivity() {

  lateinit var audioPlayer: AudioPlayer
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_audio_player)
    audioPlayer = AudioPlayer(this)
    audioPlayer.onPlaybackPercentage = {
      println("time: $it%")
    }
  }

  fun play(view: View) {
    println("play")
//    audioPlayer.play(R.raw.a)
  }

  fun pause(view: View) {
    println("pause")
    audioPlayer.pause()
  }

  fun stop(view: View) {
    println("stop")
    audioPlayer.stop()
  }
}