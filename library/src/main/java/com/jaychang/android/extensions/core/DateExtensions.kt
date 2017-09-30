package com.jaychang.android.extensions.core

import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import org.threeten.bp.temporal.WeekFields
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * LocalDate
 * */
fun LocalDate.isToday(): Boolean = isEqual(LocalDate.now())

fun LocalDate.isYesterday(): Boolean = isEqual(LocalDate.now().minusDays(1))

fun LocalDate.isTomorrow(): Boolean = isEqual(LocalDate.now().plusDays(1))

fun LocalDate.isInWeekday(): Boolean = dayOfWeek.value in 1..5

fun LocalDate.isInWeekend(): Boolean = dayOfWeek.value in 6..7

fun LocalDate.isInThisWeek(): Boolean {
  val weekOfYear = WeekFields.ISO.weekOfWeekBasedYear()
  return get(weekOfYear) == LocalDate.now().get(weekOfYear)
}

fun LocalDate.isInThisMonth(): Boolean {
  return YearMonth.from(this) == YearMonth.now()
}

fun LocalDate.isInThisYear(): Boolean {
  return Year.from(this) == Year.now()
}

fun LocalDate.lengthOfMonth(): Int {
  return YearMonth.from(this).lengthOfMonth()
}

fun LocalDate.format(pattern: String): String {
  return format(DateTimeFormatter.ofPattern(pattern))
}

fun LocalDate.format(style: FormatStyle): String {
  return format(DateTimeFormatter.ofLocalizedDate(style))
}

fun Long.milliToLocalDate(): LocalDate {
  return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
}

/**
 * LocalTime
 * */
fun LocalTime.format(pattern: String): String {
  return format(DateTimeFormatter.ofPattern(pattern))
}

fun LocalTime.format(style: FormatStyle): String {
  return format(DateTimeFormatter.ofLocalizedTime(style))
}

/**
 * LocalDateTime
 * */
fun LocalDateTime.isToday(): Boolean = toLocalDate().isToday()

fun LocalDateTime.isYesterday(): Boolean = toLocalDate().isYesterday()
fun LocalDateTime.isTomorrow(): Boolean = toLocalDate().isTomorrow()
fun LocalDateTime.isInWeekday(): Boolean = toLocalDate().isInWeekday()
fun LocalDateTime.isInWeekend(): Boolean = toLocalDate().isInWeekend()
fun LocalDateTime.isInThisWeek(): Boolean = toLocalDate().isInThisWeek()
fun LocalDateTime.isInThisMonth(): Boolean = toLocalDate().isInThisMonth()
fun LocalDateTime.isInThisYear(): Boolean = toLocalDate().isInThisYear()
fun LocalDateTime.lengthOfMonth(): Int = toLocalDate().lengthOfMonth()
fun LocalDateTime.lengthOfYear(): Int = toLocalDate().lengthOfYear()

fun LocalDateTime.format(pattern: String): String {
  return format(DateTimeFormatter.ofPattern(pattern))
}

fun LocalDateTime.format(style: FormatStyle): String {
  return format(DateTimeFormatter.ofLocalizedDateTime(style))
}

fun LocalDateTime.secondsSince(date: LocalDateTime): Long {
  return Duration.between(date, this).seconds
}

fun LocalDateTime.minutesSince(date: LocalDateTime): Long {
  return Duration.between(date, this).toMinutes()
}

fun LocalDateTime.hoursSince(date: LocalDateTime): Long {
  return Duration.between(date, this).toHours()
}

fun LocalDateTime.daysSince(date: LocalDateTime): Long {
  return Duration.between(date, this).toDays()
}

fun Long.milliToLocalDateTime(): LocalDateTime {
  return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
}

/**
 * util
 * */
fun Long.toDateTimeString(pattern: String): String {
  return milliToLocalDateTime().format(pattern)
}

/**
 * E.g. toDateTimeString("hh:mm a", "'Yesterday' hh:mm a", "'Tomorrow'", "yyyy-MM-dd");
 * return one of the following case:
 * 1. within today: 09:30 PM
 * 2. yesterday: Yesterday 10:20 PM
 * 3. other: 2016-01-20
 * Note: if the format contains other user defined strings, use '' to wrap the string.
 */
fun Long.toDateTimeString(todayPattern: String, yesterdayPattern: String, defaultPattern: String): String {
  val thatDayMillis = this
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

fun Long.toElapsedDateTimeString(yearAgoText: String,
                          monthAgoText: String,
                          weekAgoText: String,
                          dayAgoText: String,
                          hourAgoText: String,
                          minuteAgoText: String,
                          nowText: String): String {
  val fromMillis = this
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
