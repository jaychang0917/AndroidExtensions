package com.jaychang.extensions.av

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMuxer
import android.util.Log
import io.reactivex.Observable
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer

object VideoManager {
  private val TAG = VideoManager::class.java.simpleName

  @Throws(IOException::class)
  fun merge(audio: File, video: File, output: File){
    Log.d(TAG, "Start merge")

    val time = System.currentTimeMillis()

    // extractAudio audio track
    val audioExtractor = MediaExtractor()
    audioExtractor.setDataSource(audio.absolutePath)

    var targetAudioFormat: MediaFormat? = null
    for (i in 0 until audioExtractor.trackCount) {
      val format = audioExtractor.getTrackFormat(i)
      val mime = format.getString(MediaFormat.KEY_MIME)
      if (mime.startsWith("audio/")) {
        targetAudioFormat = format
        audioExtractor.selectTrack(i)
        break
      }
    }

    // extractAudio video track
    val videoExtractor = MediaExtractor()
    videoExtractor.setDataSource(video.absolutePath)

    var targetVideoFormat: MediaFormat? = null
    for (i in 0 until videoExtractor.trackCount) {
      val format = videoExtractor.getTrackFormat(i)
      val mime = format.getString(MediaFormat.KEY_MIME)
      if (mime.startsWith("video/")) {
        targetVideoFormat = format
        videoExtractor.selectTrack(i)
        break
      }
    }

    if (targetAudioFormat == null || targetVideoFormat == null) {
      val errorMsg = "Expect one audio track and one video track"
      Log.e(TAG, errorMsg)
      throw RuntimeException(errorMsg)
    }

    // add audio & video tracks to muxer
    val muxer = MediaMuxer(output.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
    val targetAudioOutputTrack = muxer.addTrack(targetAudioFormat)
    val targetVideoOutputTrack = muxer.addTrack(targetVideoFormat)
    muxer.start()

    val inputBuffer = ByteBuffer.allocate(2048 * 1024)
    val bufferInfo = MediaCodec.BufferInfo()

    // write audio data
    var audioMuxDone = false
    var frameCount = 0
    var presentationTimeUs = -1L
    while (!audioMuxDone) {
      inputBuffer.clear()
      val bytesRead = audioExtractor.readSampleData(inputBuffer, 0)
      if (bytesRead < 0) {
        audioMuxDone = true
      } else {
        if (presentationTimeUs == -1L) {
          presentationTimeUs = audioExtractor.sampleTime
        }
        bufferInfo.presentationTimeUs = audioExtractor.sampleTime - presentationTimeUs
        bufferInfo.flags = audioExtractor.sampleFlags
        bufferInfo.size = bytesRead
        muxer.writeSampleData(targetAudioOutputTrack, inputBuffer, bufferInfo)
        Log.d(TAG, "Appended audio frame: " + targetAudioOutputTrack + ":" + frameCount + ":" + bufferInfo.presentationTimeUs)
        audioExtractor.advance()
        frameCount++
      }
    }

    // write video data
    var videoMuxDone = false
    frameCount = 0
    presentationTimeUs = -1L
    while (!videoMuxDone) {
      inputBuffer.clear()
      val bytesRead = videoExtractor.readSampleData(inputBuffer, 0)
      if (bytesRead < 0) {
        videoMuxDone = true
      } else {
        if (presentationTimeUs == -1L) {
          presentationTimeUs = videoExtractor.sampleTime
        }
        bufferInfo.presentationTimeUs = videoExtractor.sampleTime - presentationTimeUs
        bufferInfo.flags = videoExtractor.sampleFlags
        bufferInfo.size = bytesRead
        muxer.writeSampleData(targetVideoOutputTrack, inputBuffer, bufferInfo)
        Log.d(TAG, "Appended video frame: " + targetVideoOutputTrack + ":" + frameCount + ":" + bufferInfo.presentationTimeUs)
        videoExtractor.advance()
        frameCount++
      }
    }

    // release resources
    audioExtractor.release()
    videoExtractor.release()
    muxer.stop()
    muxer.release()

    Log.d(TAG, "End merge, used time:" + (System.currentTimeMillis() - time) + " path: " + output.absolutePath)
  }

  class rx {
    fun merge(audio: File, video: File, output: File): Observable<File> {
      return Observable.fromCallable {
        VideoManager.merge(audio, video, output)
        output
      }
    }
  }
}