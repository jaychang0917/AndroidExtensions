package com.jaychang.extensions.av.codec

import android.annotation.SuppressLint
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.MediaMuxer
import android.support.annotation.RequiresApi
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.util.*

/**
 * Encoding profile recommendations: https://developer.android.com/guide/topics/media/media-formats.html#recommendations
 */
class AACEncoder(input: File) : Encoder {
  companion object {
    private val TAG = AACEncoder::class.java.simpleName
    private const val MIME_TYPE = "audio/mp4a-latm"
    private const val CODEC_TIMEOUT = 10000
    private const val BITRATE = 128000
    private const val SAMPLE_RATE = 44100
    private const val CHANNEL_COUNT = 2
    private const val BYTE_PER_SAMPLE = 2 // PCM_16bit
  }

  private lateinit var mediaFormat: MediaFormat
  private lateinit var mediaCodec: MediaCodec
  private lateinit var mediaMuxer: MediaMuxer
  private lateinit var codecInputBuffers: Array<ByteBuffer>
  private lateinit var codecOutputBuffers: Array<ByteBuffer>
  private lateinit var bufferInfo: MediaCodec.BufferInfo
  private lateinit var output: File
  private var audioTrackId: Int = 0
  private var totalBytesRead: Int = 0
  private var presentationTimeUs: Long = 0

  init {
    try {
      mediaFormat = MediaFormat.createAudioFormat(MIME_TYPE, SAMPLE_RATE, CHANNEL_COUNT)
      mediaFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC)
      mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, BITRATE)

      mediaCodec = MediaCodec.createEncoderByType(MIME_TYPE)
      mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
      mediaCodec.start()

      codecInputBuffers = mediaCodec.inputBuffers
      codecOutputBuffers = mediaCodec.outputBuffers

      bufferInfo = MediaCodec.BufferInfo()

      output = File(input.absoluteFile.parent, "_" + UUID.randomUUID() + ".m4a")
      mediaMuxer = MediaMuxer(output.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)

      totalBytesRead = 0
      presentationTimeUs = 0
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  @SuppressLint("WrongConstant")
  @Throws(IOException::class)
  override fun encode(input: File): File {
    Log.d(TAG, "Start encode")

    val inputStream = FileInputStream(input.absolutePath)
    val tempBuffer = ByteArray(2 * SAMPLE_RATE)
    var hasMoreData = true
    var stop = false

    while (!stop) {
      var inputBufferIndex = 0
      var currentBatchRead = 0
      while (inputBufferIndex != MediaCodec.INFO_TRY_AGAIN_LATER && hasMoreData && currentBatchRead <= 50 * SAMPLE_RATE) {
        inputBufferIndex = mediaCodec.dequeueInputBuffer(CODEC_TIMEOUT.toLong())

        if (inputBufferIndex >= 0) {
          val buffer = codecInputBuffers[inputBufferIndex]
          buffer.clear()

          val bytesRead = inputStream.read(tempBuffer, 0, buffer.limit())
          if (bytesRead == -1) {
            mediaCodec.queueInputBuffer(inputBufferIndex, 0, 0, presentationTimeUs, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
            hasMoreData = false
            stop = true
          } else {
            totalBytesRead += bytesRead
            currentBatchRead += bytesRead
            buffer.put(tempBuffer, 0, bytesRead)
            mediaCodec.queueInputBuffer(inputBufferIndex, 0, bytesRead, presentationTimeUs, 0)
            presentationTimeUs = 1000000L * (totalBytesRead / BYTE_PER_SAMPLE / SAMPLE_RATE)
          }
        }
      }

      var outputBufferIndex = 0
      while (outputBufferIndex != MediaCodec.INFO_TRY_AGAIN_LATER) {
        outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, CODEC_TIMEOUT.toLong())
        if (outputBufferIndex >= 0) {
          val encodedData = codecOutputBuffers[outputBufferIndex]
          encodedData.position(bufferInfo.offset)
          encodedData.limit(bufferInfo.offset + bufferInfo.size)

          if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG != 0 && bufferInfo.size != 0) {
            mediaCodec.releaseOutputBuffer(outputBufferIndex, false)
          } else {
            mediaMuxer.writeSampleData(audioTrackId, codecOutputBuffers[outputBufferIndex], bufferInfo)
            mediaCodec.releaseOutputBuffer(outputBufferIndex, false)
          }
        } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
          mediaFormat = mediaCodec.outputFormat
          audioTrackId = mediaMuxer.addTrack(mediaFormat)
          mediaMuxer.start()
        }
      }
    }

    inputStream.close()
    mediaCodec.stop()
    mediaCodec.release()
    mediaMuxer.stop()
    mediaMuxer.release()

    Log.d(TAG, "Finish encode")

    return output
  }

  @RequiresApi(21)
  override fun encodeStream(tempBuffer: ByteArray, bytesRead: Int) {
    try {
      val inputBufferIndex = mediaCodec.dequeueInputBuffer(CODEC_TIMEOUT.toLong())
      if (inputBufferIndex >= 0) {
        val buffer = codecInputBuffers[inputBufferIndex]
        totalBytesRead += bytesRead
        buffer.put(tempBuffer, 0, bytesRead)
        mediaCodec.queueInputBuffer(inputBufferIndex, 0, bytesRead, presentationTimeUs, 0)
        presentationTimeUs = 1000000L * (totalBytesRead / BYTE_PER_SAMPLE / SAMPLE_RATE)
      }

      val outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, CODEC_TIMEOUT.toLong())
      if (outputBufferIndex >= 0) {
        val encodedData = codecOutputBuffers[outputBufferIndex]
        encodedData.position(bufferInfo.offset)
        encodedData.limit(bufferInfo.offset + bufferInfo.size)

        if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG != 0 && bufferInfo.size != 0) {
          mediaCodec.releaseOutputBuffer(outputBufferIndex, false)
        } else {
          mediaMuxer.writeSampleData(audioTrackId, encodedData, bufferInfo)
          mediaCodec.releaseOutputBuffer(outputBufferIndex, false)
        }
      } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
        mediaFormat = mediaCodec.outputFormat
        audioTrackId = mediaMuxer.addTrack(mediaFormat)
        mediaMuxer.start()
      }
    } catch (ex: IllegalStateException) {
      Log.e(TAG, "AAC encode error")
    }

  }

  @RequiresApi(21)
  override fun stopEncodeStream(): File {
    mediaCodec.stop()
    mediaCodec.reset()
    mediaCodec.release()
    mediaMuxer.stop()
    mediaMuxer.release()
    Log.d(TAG, "Finish encode stream")
    return output
  }
}