package com.jaychang.extensions.common

class Version(val version: String) : Comparable<Version> {

  init {
    if (!version.matches("[0-9]+(\\.[0-9]+)*".toRegex())) {
      throw IllegalArgumentException("Invalid version format")
    }
  }

  override fun compareTo(other: Version): Int {
    val thisParts = this.version.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val thatParts = other.version.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val length = Math.max(thisParts.size, thatParts.size)
    for (i in 0 until length) {
      val thisPart = if (i < thisParts.size) Integer.parseInt(thisParts[i]) else 0
      val thatPart = if (i < thatParts.size) Integer.parseInt(thatParts[i]) else 0
      if (thisPart < thatPart) {
        return -1
      }
      if (thisPart > thatPart) {
        return 1
      }
    }
    return 0
  }

  override fun equals(other: Any?): Boolean {
    if (this === other)
      return true
    if (other == null)
      return false
    return if (this.javaClass != other.javaClass) false else this.compareTo((other as Version?)!!) == 0
  }

}
