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

  private var onCompleted: (() -> Unit)? = null
  private var onError: (() -> Unit)? = null
  private var playbackTimeInterval = PLAYBACK_INTERVAL
  private var playbackPercentInterval = PLAYBACK_INTERVAL
  private var onPlaybackTimeMillis: ((Int) -> Unit)? = null
  private var onPlaybackPercentage: ((Int) -> Unit)? = null
  private var onPrepared: (() -> Unit)? = null
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
      onPrepared?.invoke()
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
      onCompleted?.invoke()
      if (shouldResumeMusic) {
        abandonAudioFocus()
      }
    }

    player.setOnErrorListener { _, _, _ ->
      onError?.invoke()
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
    onPlaybackTimeMillis?.let {
      startPlaybackTimeModeTimer()
    }
    onPlaybackPercentage?.let {
      startPlaybackPercentModeTimer()
    }
  }

  private fun cancelPlaybackTimer() {
    onPlaybackTimeMillis?.let {
      cancelPlaybackTimeModeTimer()
    }
    onPlaybackPercentage?.let {
      cancelPlaybackPercentModeTimer()
    }
  }

  private fun startPlaybackTimeModeTimer() {
    playbackTimeModeTimer = object: CountDownTimer((player.duration - player.currentPosition).toLong(), playbackTimeInterval.toLong()) {
      override fun onFinish() {
        onPlaybackTimeMillis?.invoke(player.duration)
      }

      override fun onTick(millisUntilFinished: Long) {
        onPlaybackTimeMillis?.invoke(player.currentPosition)
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
        onPlaybackPercentage?.invoke(100)
      }

      override fun onTick(millisUntilFinished: Long) {
        onPlaybackPercentage?.invoke((player.currentPosition.toFloat() / player.duration.toFloat() * 100).toInt())
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

    onPlaybackTimeMillis?.let {
      cancelPlaybackTimeModeTimer()
    }
    onPlaybackPercentage?.let {
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
    onCompleted = listener
  }

  fun setOnErrorListener(listener: (() -> Unit)) {
    onError = listener
  }

  fun setOnPlaybackListener(interval: Int = PLAYBACK_INTERVAL, mode: OnPlayBackMode = OnPlayBackMode.TIME, listener: ((Int) -> Unit)) {
    if (mode == OnPlayBackMode.TIME) {
      playbackTimeInterval = interval
      onPlaybackTimeMillis = listener
    } else {
      playbackPercentInterval = interval
      onPlaybackPercentage = listener
    }
  }

  fun setOnPreparedListener(listener: (() -> Unit)) {
    onPrepared = listener
  }

  class SeekOpRequest(val millis: Int)

  class PlayOpRequest

  enum class OnPlayBackMode { TIME, PERCENTAGE }
}