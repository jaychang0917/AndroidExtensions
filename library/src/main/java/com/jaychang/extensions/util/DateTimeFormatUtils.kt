package com.jaychang.extensions.util

object DateTimeFormatUtils {

  fun formatDateTime(millis: Long, pattern: String): String {
    return millis.milliToLocalDateTime().format(pattern)
  }
}