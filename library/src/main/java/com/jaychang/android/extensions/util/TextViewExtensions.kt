package com.jaychang.android.extensions.util

import android.graphics.Rect
import android.widget.TextView

val TextView.textBounds: Rect
  get() {
    val bound = Rect()
    paint.getTextBounds(text.toString(), 0, text.length, bound)
    return bound
  }