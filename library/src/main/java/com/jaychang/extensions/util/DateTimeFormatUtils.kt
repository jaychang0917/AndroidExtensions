package com.jaychang.extensions.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateTimeFormatUtils {
  
  fun formatDateTime(millis: Long, pattern: String): String {
    return millis.milliToLocalDateTime().format(pattern)
  }

  /**
   * E.g. toDateTimeString("hh:mm a", "'Yesterday' hh:mm a", "'Tomorrow'", "yyyy-MM-dd");
   * return one of the following case:
   * 1. within today: 09:30 PM
   * 2. yesterday: Yesterday 10:20 PM
   * 3. other: 2016-01-20
   * Note: if the format contains other user defined strings, use '' to wrap the string.
   */
  fun formatDateTime(millis: Long, todayPattern: String, yesterdayPattern: String, defaultPattern: String): String {
    val thatDayMillis = millis
    val todayMillis = Calendar.getInstance().timeInMillis
    val thatDay = Date(thatDayMillis)
    val today = Date(todayMillis)
    val diffDays = today.date - thatDay.date
    val formatter = SimpleDateFormat()
    when (diffDays) {
      0 -> formatter.applyPattern(todayPattern)
      1 -> formatter.applyPattern(yesterdayPattern)
      else -> formatter.applyPattern(defaultPattern)
    }
    return formatter.format(thatDayMillis)
  }

  fun formatElapsedDateTime(millis: Long,
                            yearAgoText: String,
                            monthAgoText: String,
                            weekAgoText: String,
                            dayAgoText: String,
                            hourAgoText: String,
                            minuteAgoText: String,
                            nowText: String): String {
    val fromMillis = millis
    val nowMillis = System.currentTimeMillis()
    val millisFromNow = nowMillis - fromMillis

    val minutesFromNow = TimeUnit.MILLISECONDS.toMinutes(millisFromNow)
    if (minutesFromNow < 1) {
      return nowText
    }
    val hoursFromNow = TimeUnit.MILLISECONDS.toHours(millisFromNow)
    if (hoursFromNow < 1) {
      return minutesFromNow.toString() + minuteAgoText
    }
    val daysFromNow = TimeUnit.MILLISECONDS.toDays(millisFromNow)
    if (daysFromNow < 1) {
      return hoursFromNow.toString() + hourAgoText
    }
    val weeksFromNow = TimeUnit.MILLISECONDS.toDays(millisFromNow) / 7
    if (weeksFromNow < 1) {
      return daysFromNow.toString() + dayAgoText
    }
    val monthsFromNow = TimeUnit.MILLISECONDS.toDays(millisFromNow) / 30
    if (monthsFromNow < 1) {
      return weeksFromNow.toString() + weekAgoText
    }
    val yearsFromNow = TimeUnit.MILLISECONDS.toDays(millisFromNow) / 365
    if (yearsFromNow < 1) {
      return monthsFromNow.toString() + monthAgoText
    }
    return yearsFromNow.toString() + yearAgoText
  }
}