package com.jaychang.android.extensions.core

fun Float.round(numberOfDecimal: Int): Float {
  var pow = 10
  for (i in 1 until numberOfDecimal) {
    pow *= 10
  }
  val tmp = this * pow
  return (if (tmp - tmp.toInt() >= 0.5f) tmp + 1 else tmp).toInt().toFloat() / pow
}