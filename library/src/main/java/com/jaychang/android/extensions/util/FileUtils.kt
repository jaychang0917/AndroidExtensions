package com.jaychang.android.extensions.util

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

object FileUtils {

  fun copyFile(src: File, dst: File) {
    try {
      val inStream = FileInputStream(src)
      val outStream = FileOutputStream(dst)
      val inChannel = inStream.channel
      val outChannel = outStream.channel
      inChannel.transferTo(0, inChannel.size(), outChannel)
      inStream.close()
      outStream.close()
    } catch (ex: IOException) {
      ex.printStackTrace()
    }
  }

}