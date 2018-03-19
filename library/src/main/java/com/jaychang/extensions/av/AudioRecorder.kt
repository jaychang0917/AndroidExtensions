//package com.jaychang.extensions.av
//
//import android.media.MediaRecorder
//import com.github.piasy.rxandroidaudio.AudioRecorder
//import java.io.File
//
//object AudioRecorder {
//  private val recorder: AudioRecorder = AudioRecorder.getInstance()
//
//  var onError: ((Int) -> Unit)? = null
//    set(value) {
//      recorder.setOnErrorListener {
//        value?.invoke(it)
//      }
//    }
//
//  fun prepare(file: File,
//              source: Int = MediaRecorder.AudioSource.MIC,
//              format: Int = MediaRecorder.OutputFormat.MPEG_4,
//              encoder: Int = MediaRecorder.AudioEncoder.AAC
//  ) {
//    recorder.prepareRecord(source, format, encoder, file)
//  }
//
//  fun start() {
//    recorder.startRecord()
//  }
//
//  fun stop() {
//    recorder.startRecord()
//  }
//}