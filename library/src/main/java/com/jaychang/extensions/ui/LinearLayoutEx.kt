package com.jaychang.extensions.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.LinearLayout
import com.jaychang.extensions.internal.ViewHelper

open class LinearLayoutEx : LinearLayout {
  private val viewHelper = ViewHelper()

  var radius
    set(value) {
      viewHelper.radius = value
    }
    get() = viewHelper.radius

  var topLeftRadius
    set(value) {
      viewHelper.topLeftRadius = value
    }
    get() = viewHelper.topLeftRadius

  var topRightRadius
    set(value) {
      viewHelper.topRightRadius = value
    }
    get() = viewHelper.topRightRadius

  var bottomLeftRadius
    set(value) {
      viewHelper.bottomLeftRadius = value
    }
    get() = viewHelper.bottomLeftRadius

  var bottomRightRadius
    set(value) {
      viewHelper.bottomRightRadius = value
    }
    get() = viewHelper.bottomRightRadius

  var borderWidth
    set(value) {
      viewHelper.borderWidth = value
    }
    get() = viewHelper.borderWidth

  var borderColor
    set(value) {
      viewHelper.borderColor = value
    }
    get() = viewHelper.borderColor

  var isBadgeVisible
    set(value) {
      viewHelper.isBadgeVisible = value
    }
    get() = viewHelper.isBadgeVisible
  
  constructor(ctx: Context) : this(ctx, null, 0)
  constructor(ctx: Context, attrs: AttributeSet?) : this(ctx, attrs, 0)
  constructor(ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr) {
    viewHelper.init(this, ctx, attrs)
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    viewHelper.onMeasure(width, height)
  }

  override fun draw(canvas: Canvas) {
    viewHelper.clipPath(canvas)
    super.draw(canvas)
    viewHelper.drawBorder(canvas)
    viewHelper.drawBadge(canvas)
  }

  fun showBadge() {
    viewHelper.showBadge(this)
  }

  fun hideBadge() {
    viewHelper.hideBadge(this)
  }
}