package com.jaychang.extensions.util

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.widget.TextView

val TextView.textBounds: Rect
  get() {
    val bound = Rect()
    paint.getTextBounds(text.toString(), 0, text.length, bound)
    return bound
  }

var TextView.leftDrawable: Drawable
  get() {
    return compoundDrawables[0]
  }
  set(value) {
    setCompoundDrawablesWithIntrinsicBounds(value, topDrawable, rightDrawable, bottomDrawable)
  }

var TextView.topDrawable: Drawable
  get() {
    return compoundDrawables[1]
  }
  set(value) {
    setCompoundDrawablesWithIntrinsicBounds(leftDrawable, value, rightDrawable, bottomDrawable)
  }

var TextView.rightDrawable: Drawable
  get() {
    return compoundDrawables[2]
  }
  set(value) {
    setCompoundDrawablesWithIntrinsicBounds(leftDrawable, topDrawable, value, bottomDrawable)
  }

var TextView.bottomDrawable: Drawable
  get() {
    return compoundDrawables[3]
  }
  set(value) {
    setCompoundDrawablesWithIntrinsicBounds(leftDrawable, topDrawable, rightDrawable, value)
  }

var TextView.startDrawable: Drawable
  get() {
    return compoundDrawablesRelative[0]
  }
  set(value) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(value, topDrawable, endDrawable, bottomDrawable)
  }

var TextView.endDrawable: Drawable
  get() {
    return compoundDrawablesRelative[2]
  }
  set(value) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(startDrawable, topDrawable, endDrawable, bottomDrawable)
  }