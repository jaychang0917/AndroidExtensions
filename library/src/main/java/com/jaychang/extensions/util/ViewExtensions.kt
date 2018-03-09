package com.jaychang.extensions.util

import android.animation.ValueAnimator
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

fun View.measure() {
  val params = layoutParams ?: ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
  val childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, params.width)
  val height = params.height
  val childHeightSpec = if (height > 0) {
    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
  } else {
    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
  }
  measure(childWidthSpec, childHeightSpec)
}

var View.viewWidth: Int
  set(value) {
    layoutParams.width = value
  }
  get() {
    measure()
    return measuredWidth
  }

var View.viewHeight: Int
  set(value) {
    layoutParams.height = value
  }
  get() {
    measure()
    return measuredHeight
  }

var View.viewMarginStart: Int
  set(value) {
    (layoutParams as ViewGroup.MarginLayoutParams).leftMargin = value
  }
  get() {
    return (layoutParams as ViewGroup.MarginLayoutParams).leftMargin
  }

var View.viewMarginEnd: Int
  set(value) {
    (layoutParams as ViewGroup.MarginLayoutParams).rightMargin = value
  }
  get() {
    return (layoutParams as ViewGroup.MarginLayoutParams).rightMargin
  }

var View.viewMarginTop: Int
  set(value) {
    (layoutParams as ViewGroup.MarginLayoutParams).topMargin = value
  }
  get() {
    return (layoutParams as ViewGroup.MarginLayoutParams).topMargin
  }

var View.viewMarginBottom: Int
  set(value) {
    (layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = value
  }
  get() {
    return (layoutParams as ViewGroup.MarginLayoutParams).bottomMargin
  }

var View.viewPaddingStart: Int
  set(value) {
    setPadding(value, paddingTop, paddingEnd, paddingBottom)
  }
  get() {
    return paddingStart
  }

var View.viewPaddingEnd: Int
  set(value) {
    setPadding(paddingStart, paddingTop, value, paddingBottom)
  }
  get() {
    return paddingEnd
  }

var View.viewPaddingTop: Int
  set(value) {
    setPadding(paddingStart, value, paddingEnd, paddingBottom)
  }
  get() {
    return paddingTop
  }

var View.viewPaddingBottom: Int
  set(value) {
    setPadding(paddingStart, paddingTop, paddingEnd, value)
  }
  get() {
    return paddingBottom
  }

fun View.show() {
  visibility = View.VISIBLE
}

fun View.hide() {
  visibility = View.INVISIBLE
}

fun View.gone() {
  visibility = View.GONE
}

fun View.expandVertically(duration: Int = 150, fromHeight: Int = 0): ValueAnimator {
  measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
  val targetHeight = measuredHeight

  val animator = ValueAnimator.ofInt(fromHeight, targetHeight)
  animator.addUpdateListener { valueAnimator ->
    val value = valueAnimator.animatedValue as Int
    val layoutParams = layoutParams
    layoutParams.height = value
    this.layoutParams = layoutParams
  }
  animator.duration = duration.toLong()
  animator.start()

  return animator
}

fun View.expandHorizontally(duration: Int = 150, fromWidth: Int = 0): ValueAnimator {
  measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
  val targetWidth = measuredWidth

  val animator = ValueAnimator.ofInt(fromWidth, targetWidth)
  animator.addUpdateListener { valueAnimator ->
    val value = valueAnimator.animatedValue as Int
    val layoutParams = layoutParams
    layoutParams.width = value
    this.layoutParams = layoutParams
  }
  animator.duration = duration.toLong()
  animator.start()

  return animator
}

fun View.collapseVertically(duration: Int = 150, toHeight: Int = 0): ValueAnimator {
  measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
  val targetHeight = measuredHeight

  val animator = ValueAnimator.ofInt(targetHeight, toHeight)
  animator.addUpdateListener { valueAnimator ->
    val value = valueAnimator.animatedValue as Int
    val layoutParams = layoutParams
    layoutParams.height = value
    this.layoutParams = layoutParams
  }
  animator.duration = duration.toLong()
  animator.start()

  return animator
}

fun View.collapseHorizontally(duration: Int = 150, toWidth: Int = 0): ValueAnimator {
  measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
  val targetWidth = measuredWidth

  val animator = ValueAnimator.ofInt(targetWidth, toWidth)
  animator.addUpdateListener { valueAnimator ->
    val value = valueAnimator.animatedValue as Int
    val layoutParams = layoutParams
    layoutParams.width = value
    this.layoutParams = layoutParams
  }
  animator.duration = duration.toLong()
  animator.start()

  return animator
}

fun View.isTouchInside(motionEvent: MotionEvent): Boolean {
  val viewLoc = IntArray(2)
  getLocationOnScreen(viewLoc)
  val motionX = motionEvent.rawX
  val motionY = motionEvent.rawY
  return motionX >= viewLoc[0]
    && motionX <= viewLoc[0] + width
    && motionY >= viewLoc[1]
    && motionY <= viewLoc[1] + height
}

fun View.doOnLayout(f: (View) -> Unit) {
  viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
    override fun onGlobalLayout() {
      viewTreeObserver.removeOnGlobalLayoutListener(this)
      f(this@doOnLayout)
    }
  })
}