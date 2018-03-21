package com.jaychang.extensions.util

object DurationFormatUtils {

  fun formatHMS(millis: Long, fullFormat: Boolean = false): String {
    val duration = millis / 1000
    val hours = duration / 3600
    val minutes = (duration % 3600) / 60
    val seconds = duration % 60
    return if (hours > 0 || fullFormat) String.format("%d:%02d:%02d", hours, minutes, seconds) else String.format("%02d:%02d", minutes, seconds)
  }
}