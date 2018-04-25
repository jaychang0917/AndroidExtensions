package com.jaychang.extensions.av

import android.media.MediaRecorder
import java.io.File

object AudioRecorder {
  private val recorder: AudioRecorderInternal = AudioRecorderInternal.getInstance()

  fun setOnErrorListener(listener: (Int) -> Unit) {
    recorder.setOnErrorListener {
      listener.invoke(it)
    }
  }

  fun setOnMaxDurationReachListener(listener: () -> Unit) {
    recorder.setmOnMaxDurationReachListener {
      listener.invoke()
    }
  }

  fun prepare(file: File,
              source: Int = MediaRecorder.AudioSource.MIC,
              format: Int = MediaRecorder.OutputFormat.MPEG_4,
              encoder: Int = MediaRecorder.AudioEncoder.AAC,
              maxDurationMs: Int = Int.MAX_VALUE
  ) {
    recorder.prepareRecord(source, format, encoder, maxDurationMs, file)
  }

  fun start() {
    recorder.startRecord()
  }

  fun stop() {
    recorder.stopRecord()
  }
}