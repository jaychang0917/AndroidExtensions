package com.jaychang.android.extensions.core

import java.util.*

fun <T> Iterable<T>.shuffled(): List<T> {
  val list = this.toMutableList()
  Collections.shuffle(list)
  return list
}

fun <T> Array<T>.shuffled(): List<T> {
  val list = this.toMutableList()
  Collections.shuffle(list)
  return list
}

fun <T> Collection<T>.takeRandomly(count: Int = 1): List<T> {
  if (count > size) {
    return shuffled()
  }
  
  val result = mutableListOf<T>()
  val base = shuffled()
  (0 until count).mapTo(result) { base[it] }
  return result
}