package com.jaychang.extensions.core

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.CountDownTimer
import android.support.annotation.RawRes
import java.io.File

class AudioPlayer(val context: Context) {

  private var _player: MediaPlayer? = null
  private val player: MediaPlayer
    get() = _player!!
  private val appContext = context.applicationContext
  private var isStartMode = true
  private lateinit var playbackTimer: CountDownTimer

  var onCompletion: (() -> Unit)? = null
  var onError: (() -> Unit)? = null
  var onPlaybackTimeMillis: ((Int) -> Unit)? = null
  var onPlaybackPercentage: ((Float) -> Unit)? = null
  var resumeMusic: Boolean = true

  val isPlaying: Boolean
    get() = player.isPlaying

  val duration: Int
    get() = player.duration

  private fun requestAudioFocus(): Boolean {
    val audioManager = appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val result = audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
    return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
  }

  private fun abandonAudioFocus() {
    val audioManager = appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.abandonAudioFocus(null)
  }

  private fun start(@RawRes rawRes: Int? = null, file: File? = null, uri: Uri? = null) {
    if (!requestAudioFocus()) {
      return
    }

    stop()

    rawRes?.let {
      _player = MediaPlayer.create(appContext, rawRes)
    }
    file?.let {
      _player = MediaPlayer.create(appContext, Uri.fromFile(file))
    }
    uri?.let {
      _player = MediaPlayer.create(appContext, uri)
    }

    player.setOnCompletionListener { _ ->
      onCompletion?.invoke()
      if (resumeMusic) {
        abandonAudioFocus()
      }
    }

    player.setOnErrorListener { _, _, _ ->
      onError?.invoke()
      if (resumeMusic) {
        abandonAudioFocus()
      }
      true
    }

    isStartMode = true

    start()

    isStartMode = false
  }

  private fun start() {
    try {
      player.start()
      startPlaybackTimer()
    } catch (e: IllegalStateException) {
      player.release()
    }
  }

  private fun startPlaybackTimer() {
    playbackTimer = object: CountDownTimer((player.duration - player.currentPosition).toLong(), 1000L) {
      override fun onFinish() {
        onPlaybackTimeMillis?.invoke(player.duration)
        onPlaybackPercentage?.invoke(100f)
      }

      override fun onTick(millisUntilFinished: Long) {
        onPlaybackTimeMillis?.invoke(player.currentPosition)
        onPlaybackPercentage?.invoke(player.currentPosition.toFloat() / player.duration.toFloat() * 100)
      }
    }
    playbackTimer.start()
  }

  private fun cancelPlaybackTimer() {
     playbackTimer.cancel()
  }

  fun play(@RawRes rawRes: Int) {
    if (isStartMode) {
      start(rawRes = rawRes)
    } else {
      start()
    }
  }

  fun play(file: File) {
    if (isStartMode) {
      start(file = file)
    } else {
      start()
    }
  }

  fun play(uri: Uri) {
    if (isStartMode) {
      start(uri = uri)
    } else {
      start()
    }
  }

  fun stop() {
    if (_player == null || !player.isPlaying) {
      return
    }

    try {
      player.stop()
    } catch (e: IllegalStateException) {
    }
    player.reset()
    player.release()
    player.setOnCompletionListener(null)
    player.setOnErrorListener(null)
    _player = null

    isStartMode = true

    cancelPlaybackTimer()
  }

  fun pause() {
    if (_player == null || !player.isPlaying) {
      return
    }

    try {
      player.pause()
    } catch (e: IllegalStateException) {
    }

    cancelPlaybackTimer()
  }
}