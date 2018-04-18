package com.jaychang.extensions.util

import android.graphics.Color
import android.graphics.Typeface
import android.support.annotation.ColorInt
import android.text.SpannableString
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.widget.TextView

fun CharSequence.styled(): SpannableString = SpannableString(this)

fun CharSequence.bold() : SpannableString {
  val spannableString = SpannableString(this)
  spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
  return spannableString
}

fun CharSequence.italic() : SpannableString {
  val spannableString = SpannableString(this)
  spannableString.setSpan(StyleSpan(Typeface.ITALIC), 0, length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
  return spannableString
}

fun CharSequence.font(name: String) : SpannableString {
  val spannableString = SpannableString(this)
  spannableString.setSpan(TypefaceSpan(name), 0, length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
  return spannableString
}

fun CharSequence.strikethrough() : SpannableString {
  val spannableString = SpannableString(this)
  spannableString.setSpan(StrikethroughSpan(), 0, length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
  return spannableString
}

fun CharSequence.underline() : SpannableString {
  val spannableString = SpannableString(this)
  spannableString.setSpan(UnderlineSpan(), 0, length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
  return spannableString
}

fun CharSequence.backgroundColor(@ColorInt color: Int) : SpannableString {
  val spannableString = SpannableString(this)
  spannableString.setSpan(BackgroundColorSpan(color), 0, length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
  return spannableString
}

fun CharSequence.color(@ColorInt color: Int) : SpannableString {
  val spannableString = SpannableString(this)
  spannableString.setSpan(ForegroundColorSpan(color), 0, length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
  return spannableString
}

fun CharSequence.url(url: String, textView: TextView) : SpannableString {
  val spannableString = SpannableString(this)
  spannableString.setSpan(URLSpan(url), 0, length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
  textView.highlightColor = Color.TRANSPARENT
  textView.movementMethod = LinkMovementMethod()
  return spannableString
}

fun CharSequence.textSize(dp: Int) : SpannableString {
  val spannableString = SpannableString(this)
  spannableString.setSpan(AbsoluteSizeSpan(dp, true), 0, length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
  return spannableString
}

operator fun SpannableString.plus(next: SpannableString): SpannableString {
  return SpannableString(TextUtils.concat(this, next))
}