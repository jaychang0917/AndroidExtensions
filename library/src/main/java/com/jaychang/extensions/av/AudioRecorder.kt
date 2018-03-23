package com.jaychang.extensions.av

import android.media.MediaRecorder
import com.github.piasy.rxandroidaudio.AudioRecorder
import java.io.File

object AudioRecorder {
  private val recorder: AudioRecorder = AudioRecorder.getInstance()

  fun setOnErrorListener(listener: (Int) -> Unit) {
    recorder.setOnErrorListener {
      listener.invoke(it)
    }
  }

  fun prepare(file: File,
              source: Int = MediaRecorder.AudioSource.MIC,
              format: Int = MediaRecorder.OutputFormat.MPEG_4,
              encoder: Int = MediaRecorder.AudioEncoder.AAC
  ) {
    recorder.prepareRecord(source, format, encoder, file)
  }

  fun start() {
    recorder.startRecord()
  }

  fun stop() {
    recorder.startRecord()
  }
}