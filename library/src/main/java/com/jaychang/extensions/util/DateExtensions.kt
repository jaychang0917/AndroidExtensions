package com.jaychang.extensions.util

import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import org.threeten.bp.temporal.WeekFields


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
