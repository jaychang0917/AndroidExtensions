package com.jaychang.android.extensions.util


fun <K,V> HashMap<K, V>.removeAll(vararg keys: K): HashMap<K, V> {
  keys.forEach { remove(it) }
  return this
}