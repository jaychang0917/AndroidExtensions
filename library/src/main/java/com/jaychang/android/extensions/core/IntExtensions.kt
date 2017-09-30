package com.jaychang.android.extensions.core

fun Int.digits(): List<Int> {
  val result = mutableListOf<Int>()
  return toString().mapTo(result, { it.toString().toInt() })
}

fun Long.digits(): List<Long> {
  val result = mutableListOf<Long>()
  return toString().mapTo(result, { it.toString().toLong() })
}

fun Int.toRadians(): Double = Math.toRadians(this.toDouble())

fun Int.toDegrees(): Double = Math.toDegrees(this.toDouble())

fun Int.isPositive(): Boolean = this > 0

fun Int.isNegative(): Boolean = this < 0

fun Int.isEven(): Boolean = this % 2 == 0

fun Int.isOdd(): Boolean = this % 2 != 0

fun Int.format(trimTrailingZero: Boolean = true): String {
  if (this < 1000) {
    return toString()
  }
  val exp = (Math.log(toDouble()) / Math.log(1000.0)).toInt()
  var formatted = String.format("%.1f%c", this / Math.pow(1000.0, exp.toDouble()), "kMGTPE"[exp - 1])
  if (trimTrailingZero && formatted.contains(Regex("\\.0[kMGTPE]*"))) {
    formatted = formatted.replace(".0", "")
  }
  return formatted
}

fun Int.toRomanNumeral(): String {
  val romanValues = listOf("M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I")
  val arabicValues = listOf(1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1)

  var romanValue = ""
  var startingValue = this

  for ((index, romanChar) in romanValues.withIndex()) {
    val arabicValue = arabicValues[index]
    val div = startingValue / arabicValue
    if (div > 0) {
      for (index in 0 until div) {
        romanValue += romanChar
      }
      startingValue -= arabicValue * div
    }
  }
  return romanValue
}