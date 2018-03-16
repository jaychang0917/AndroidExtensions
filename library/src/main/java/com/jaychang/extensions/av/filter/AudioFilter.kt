package com.jaychang.extensions.av.filter

import java.io.File
import java.io.IOException

interface AudioFilter {
  @Throws(IOException::class)
  fun process(input: File): File

  fun processStream(data: ByteArray): ByteArray
}
