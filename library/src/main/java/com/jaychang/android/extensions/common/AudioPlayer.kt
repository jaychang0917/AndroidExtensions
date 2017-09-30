package com.jaychang.android.extensions.common

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import android.support.annotation.RawRes
import java.io.File

class AudioPlayer(val context: Context) {

  interface Callback {
    fun onCompletion()
    fun onError()
  }

  private lateinit var player: MediaPlayer
  private val appContext = context.applicationContext

  private fun requestAudioFocus(): Boolean {
    val audioManager = appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val result = audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
    return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
  }

  private fun abandonAudioFocus() {
    val audioManager = appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.abandonAudioFocus(null)
  }

  private fun play(@RawRes rawRes: Int?, file: File?, uri: Uri?, resumeMusic: Boolean = true, callback: Callback? = null) {
    if (!requestAudioFocus()) {
      return
    }

    stop()

    AsyncTask.execute {
      rawRes?.let {
        player = MediaPlayer.create(appContext, rawRes)
      }
      file?.let {
        player = MediaPlayer.create(appContext, Uri.fromFile(file))
      }
      uri?.let {
        player = MediaPlayer.create(appContext, uri)
      }

      player.setOnCompletionListener { _ ->
        callback?.onCompletion()
        if (resumeMusic) {
          abandonAudioFocus()
        }
      }

      player.setOnErrorListener { _, _, _ ->
        callback?.onError()
        if (resumeMusic) {
          abandonAudioFocus()
        }
        true
      }

      player.start()
    }
  }

  fun play(@RawRes rawRes: Int, resumeMusic: Boolean = true, callback: Callback? = null) {
    play(rawRes = rawRes, resumeMusic = resumeMusic, callback = callback)
  }

  fun play(file: File, resumeMusic: Boolean = true, callback: Callback? = null) {
    play(file = file, resumeMusic = resumeMusic, callback = callback)
  }

  fun play(uri: Uri, resumeMusic: Boolean = true, callback: Callback? = null) {
    play(uri = uri, resumeMusic = resumeMusic, callback = callback)
  }

  fun stop() {
    if (!player.isPlaying) {
      return
    }

    player.stop()
    player.reset()
    player.release()
    player.setOnCompletionListener(null)
    player.setOnErrorListener(null)
  }

  fun isPlaying(): Boolean {
    return player.isPlaying
  }

}