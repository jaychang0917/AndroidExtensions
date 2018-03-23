package com.jaychang.extensions.av

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.CountDownTimer
import android.support.annotation.RawRes
import java.io.File

class AudioPlayer(val context: Context) {
  companion object {
    private const val PLAYBACK_INTERVAL = 16
  }

  private var _player: MediaPlayer? = null
  private val player: MediaPlayer
    get() = _player!!
  private val appContext = context.applicationContext
  private lateinit var playbackTimeModeTimer: CountDownTimer
  private lateinit var playbackPercentModeTimer: CountDownTimer
  private var playOpRequest:PlayOpRequest? = null
  private var seekOpRequest: SeekOpRequest? = null
  private var isPrepared = false

  private var onCompletedListener: (() -> Unit)? = null
  private var onErrorListener: (() -> Unit)? = null
  private var playbackTimeInterval = PLAYBACK_INTERVAL
  private var playbackPercentInterval = PLAYBACK_INTERVAL
  private var onPlaybackTimeListener: ((Int) -> Unit)? = null
  private var onPlaybackPercentageListener: ((Int) -> Unit)? = null
  private var onPreparedListener: (() -> Unit)? = null
  var shouldResumeMusic: Boolean = true

  val isPlaying: Boolean
    get() = player.isPlaying

  val durationMillis: Int
    get() = player.duration

  val currentPosition: Int
    get() = player.currentPosition

  private fun requestAudioFocus(): Boolean {
    val audioManager = appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val result = audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
    return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
  }

  private fun abandonAudioFocus() {
    val audioManager = appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.abandonAudioFocus(null)
  }

  private fun setSourceInternal(@RawRes rawRes: Int? = null, file: File? = null, uri: Uri? = null, url: String? = null) {
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
    url?.let {
      _player = MediaPlayer()
      player.setAudioStreamType(AudioManager.STREAM_MUSIC)
      player.setDataSource(url)
      player.prepareAsync()
    }

    player.setOnPreparedListener {
      isPrepared = true
      onPreparedListener?.invoke()
      playOpRequest?.let {
        playInternal()
        playOpRequest = null
      }
      seekOpRequest?.let {
        seekTo(it.millis)
        seekOpRequest = null
      }
    }

    player.setOnCompletionListener { _ ->
      cancelPlaybackTimer()
      onCompletedListener?.invoke()
      if (shouldResumeMusic) {
        abandonAudioFocus()
      }
    }

    player.setOnErrorListener { _, _, _ ->
      onErrorListener?.invoke()
      if (shouldResumeMusic) {
        abandonAudioFocus()
      }
      true
    }
  }

  fun setSource(@RawRes rawRes: Int) {
    setSourceInternal(rawRes = rawRes)
  }

  fun setSource(file: File) {
    setSourceInternal(file = file)
  }

  fun setSource(uri: Uri) {
    setSourceInternal(uri = uri)
  }

  fun setSource(url: String) {
    setSourceInternal(url = url)
  }

  private fun startPlaybackTimer() {
    onPlaybackTimeListener?.let {
      startPlaybackTimeModeTimer()
    }
    onPlaybackPercentageListener?.let {
      startPlaybackPercentModeTimer()
    }
  }

  private fun cancelPlaybackTimer() {
    onPlaybackTimeListener?.let {
      cancelPlaybackTimeModeTimer()
    }
    onPlaybackPercentageListener?.let {
      cancelPlaybackPercentModeTimer()
    }
  }

  private fun startPlaybackTimeModeTimer() {
    playbackTimeModeTimer = object: CountDownTimer((player.duration - player.currentPosition).toLong(), playbackTimeInterval.toLong()) {
      override fun onFinish() {
        onPlaybackTimeListener?.invoke(player.duration)
      }

      override fun onTick(millisUntilFinished: Long) {
        onPlaybackTimeListener?.invoke(player.currentPosition)
      }
    }
    playbackTimeModeTimer.start()
  }

  private fun cancelPlaybackTimeModeTimer() {
     playbackTimeModeTimer.cancel()
  }

  private fun startPlaybackPercentModeTimer() {
    playbackPercentModeTimer = object: CountDownTimer((player.duration - player.currentPosition).toLong(), playbackPercentInterval.toLong()) {
      override fun onFinish() {
        onPlaybackPercentageListener?.invoke(100)
      }

      override fun onTick(millisUntilFinished: Long) {
        onPlaybackPercentageListener?.invoke((player.currentPosition.toDouble() / player.duration.toDouble() * 100).toInt())
      }
    }
    playbackPercentModeTimer.start()
  }

  private fun cancelPlaybackPercentModeTimer() {
    playbackPercentModeTimer.cancel()
  }

  fun play() {
    if (isPrepared) {
      playInternal()
    } else {
      playOpRequest = PlayOpRequest()
    }
  }

  private fun playInternal() {
    if (isPlaying) {
      return
    }
    
    if (!requestAudioFocus()) {
      return
    }

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
    isPrepared = false

    onPlaybackTimeListener?.let {
      cancelPlaybackTimeModeTimer()
    }
    onPlaybackPercentageListener?.let {
      cancelPlaybackPercentModeTimer()
    }
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

  fun seekTo(millis: Int) {
    seekOpRequest = SeekOpRequest(millis)
    player.seekTo(millis)
  }

  fun setOnCompletedListener(listener: (() -> Unit)) {
    onCompletedListener = listener
  }

  fun setOnErrorListener(listener: (() -> Unit)) {
    onErrorListener = listener
  }

  fun setOnPlaybackMillisTimeListener(interval: Int = PLAYBACK_INTERVAL, listener: ((Int) -> Unit)) {
    playbackTimeInterval = interval
    onPlaybackTimeListener = listener
  }

  fun setOnPlaybackPercentageListener(interval: Int = PLAYBACK_INTERVAL, listener: ((Int) -> Unit)) {
    playbackPercentInterval = interval
    onPlaybackPercentageListener = listener
  }

  fun setOnPreparedListener(listener: (() -> Unit)) {
    onPreparedListener = listener
  }

  class SeekOpRequest(val millis: Int)

  class PlayOpRequest
}