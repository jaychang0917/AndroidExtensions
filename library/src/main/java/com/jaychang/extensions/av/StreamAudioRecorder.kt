package com.jaychang.extensions.av

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.experimental.and

object StreamAudioRecorder {
  private val TAG = StreamAudioRecorder::class.java.simpleName
  const val BUFFER_SIZE = 2048
  const val SAMPLE_RATE = 44100
  private const val BYTE_BUFFER_SIZE = BUFFER_SIZE
  private const val SHORT_BUFFER_SIZE = BYTE_BUFFER_SIZE / 2
  private const val SOURCE = MediaRecorder.AudioSource.MIC
  private const val CHANNEL_CONFIG_STEREO = AudioFormat.CHANNEL_IN_STEREO
  private const val CHANNEL_CONFIG_MONO = AudioFormat.CHANNEL_IN_MONO
  private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT

  private val isRecording: AtomicBoolean = AtomicBoolean(false)
  private lateinit var executorService: ExecutorService

  var onDataReady: ((data: ByteArray, size: Int) -> Unit)? = null
  var onAmplitudeChanged: ((amplitude: Int) -> Unit)? = null
  var onError: (() -> Unit)? = null

  @Synchronized
  fun start(): Boolean {
    stop()
    executorService = Executors.newSingleThreadExecutor()
    if (isRecording.compareAndSet(false, true)) {
      executorService.execute(AudioRecordRunnable())
      return true
    }
    return false
  }

  @Synchronized
  fun stop() {
    isRecording.compareAndSet(true, false)
    executorService.shutdown()
  }

  private class AudioRecordRunnable : Runnable {
    private val audioRecord: AudioRecord
    private val byteBuffer: ByteArray
    private val shortBuffer: ShortArray

    init {
      var minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG_STEREO, AUDIO_FORMAT)
      val isStereoSupport = minBufferSize != AudioRecord.ERROR_BAD_VALUE
      if (!isStereoSupport) {
        minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG_MONO, AUDIO_FORMAT)
      }
      this.byteBuffer = ByteArray(BYTE_BUFFER_SIZE)
      this.shortBuffer = ShortArray(SHORT_BUFFER_SIZE)
      this.audioRecord = AudioRecord(SOURCE, SAMPLE_RATE,
        if (isStereoSupport) CHANNEL_CONFIG_STEREO else CHANNEL_CONFIG_MONO,
        AUDIO_FORMAT, Math.max(minBufferSize, BYTE_BUFFER_SIZE))
    }

    override fun run() {
      if (audioRecord.state != AudioRecord.STATE_INITIALIZED) {
        return
      }

      try {
        audioRecord.startRecording()
      } catch (e: IllegalStateException) {
        Log.w(TAG, "startRecording fail: " + e.message)
        onError?.invoke()
        return
      }

      while (isRecording.get()) {
        val readShorts = audioRecord.read(shortBuffer, 0, SHORT_BUFFER_SIZE)
        if (readShorts > 0) {
          onDataReady?.invoke(short2byte(shortBuffer, readShorts, byteBuffer), readShorts * 2)
          onAmplitudeChanged?.invoke(getAmplitude(readShorts, shortBuffer))
        } else {
          onError(readShorts)
          break
        }
      }

      audioRecord.stop()
      audioRecord.release()
    }

    private fun getAmplitude(readSize: Int, mBuffer: ShortArray): Int {
      var sum = 0.0
      for (i in 0 until readSize) {
        sum += (mBuffer[i] * mBuffer[i]).toDouble()
      }

      var amplitude = 0
      if (readSize > 0) {
        amplitude = Math.sqrt(sum / readSize).toInt()
      }

      return amplitude
    }

    private fun short2byte(sData: ShortArray, size: Int, bData: ByteArray): ByteArray {
      if (size > sData.size || size * 2 > bData.size) {
        Log.w(TAG, "short2byte: too long short data array")
      }
      for (i in 0 until size) {
        bData[i * 2] = (sData[i] and 0x00FF).toByte()
        bData[i * 2 + 1] = (sData[i].toInt() shr 8).toByte()
      }
      return bData
    }

    private fun onError(errorCode: Int) {
      if (errorCode == AudioRecord.ERROR_INVALID_OPERATION) {
        Log.e(TAG, "audio record fail: ERROR_INVALID_OPERATION")
        onError?.invoke()
      } else if (errorCode == AudioRecord.ERROR_BAD_VALUE) {
        Log.e(TAG, "audio record fail: ERROR_BAD_VALUE")
        onError?.invoke()
      }
    }
  }
}
