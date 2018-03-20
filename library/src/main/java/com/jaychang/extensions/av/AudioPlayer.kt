package com.jaychang.extensions.av

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.CountDownTimer
import android.support.annotation.RawRes
import java.io.File

class AudioPlayer(val context: Context,
                  @RawRes rawRes: Int? = null, file: File? = null, uri: Uri? = null, val url: String? = null) {

  init {
    setSource(rawRes = rawRes, file = file, uri = uri, url = url)
  }

  private var _player: MediaPlayer? = null
  private val player: MediaPlayer
    get() = _player!!
  private val appContext = context.applicationContext
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

  private var isPrepared = false

  private fun requestAudioFocus(): Boolean {
    val audioManager = appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val result = audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
    return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
  }

  private fun abandonAudioFocus() {
    val audioManager = appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.abandonAudioFocus(null)
  }

  fun setSource(@RawRes rawRes: Int? = null, file: File? = null, uri: Uri? = null, url: String? = null) {
    isPrepared = false

    rawRes?.let {
      _player = MediaPlayer.create(appContext, rawRes)
    }
    file?.let {
      _player = MediaPlayer.create(appContext, Uri.fromFile(file))
    }
    uri?.let {
      _player = MediaPlayer.create(appContext, uri)
    }
    url?.let {
      _player = MediaPlayer()
      player.setAudioStreamType(AudioManager.STREAM_MUSIC)
      player.setDataSource(url)
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

  fun play() {
    url?.let {
      player.prepareAsync()
      player.setOnPreparedListener {
        playInternal()
      }

      return
    }

    playInternal()
  }

  private fun playInternal() {
    if (!requestAudioFocus()) {
      return
    }

    stop()

    try {
      player.start()
      startPlaybackTimer()
    } catch (e: IllegalStateException) {
      player.release()
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