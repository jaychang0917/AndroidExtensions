package com.jaychang.extensions.av.filter

import android.content.Context
import android.support.annotation.FloatRange
import android.util.Log
import com.github.piasy.audioprocessor.AudioProcessor
import com.jaychang.extensions.av.StreamAudioRecorder
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class PitchShiftFilter(context: Context, @FloatRange(from = 0.0, to = 2.0) private val ratio: Float = DEFAULT_RATIO) : AudioFilter {
  companion object {
    private val TAG = PitchShiftFilter::class.java.simpleName
    private const val BUFFER = 2048
    private const val DEFAULT_RATIO = 0.5f
  }

  private val audioProcessor: AudioProcessor
  private val output: File

  init {
    this.audioProcessor = AudioProcessor(BUFFER)
    this.output = File(context.externalCacheDir, "pitchShift_audio_" + UUID.randomUUID() + ".pcm")
  }

  @Throws(IOException::class)
  override fun process(input: File): File {
    val time = System.currentTimeMillis()
    val inputStream = FileInputStream(input)
    val outputStream = FileOutputStream(output)
    val audioBuffer = ByteArray(2048)
    while (inputStream.read(audioBuffer) > 0) {
      outputStream.write(audioProcessor.process(ratio, audioBuffer, StreamAudioRecorder.SAMPLE_RATE))
    }
    outputStream.close()
    inputStream.close()

    Log.i(TAG,  "process time:" + (System.currentTimeMillis() - time))
    return output
  }

  override fun processStream(data: ByteArray): ByteArray {
    return audioProcessor.process(ratio, data, StreamAudioRecorder.SAMPLE_RATE)
  }
}