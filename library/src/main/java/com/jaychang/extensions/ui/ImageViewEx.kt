package com.jaychang.extensions.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView
import com.jaychang.extensions.internal.ViewHelper

open class ImageViewEx : ImageView {
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
    if (scaleType == ScaleType.FIT_CENTER) {
      scaleType = ScaleType.FIT_CENTER
    }
  }

  override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
    viewHelper.onSizeChanged(width, height)
  }

  override fun draw(canvas: Canvas) {
    viewHelper.clipPath(canvas)
    super.draw(canvas)
    viewHelper.drawBorder(canvas)
    viewHelper.drawBadge(canvas)
  }

  override fun setScaleType(scaleType: ScaleType) {
    when (scaleType) {
      ScaleType.CENTER,
      ScaleType.CENTER_CROP,
      ScaleType.CENTER_INSIDE,
      ScaleType.FIT_CENTER,
      ScaleType.FIT_START,
      ScaleType.FIT_END,
      ScaleType.FIT_XY -> { super.setScaleType(ScaleType.FIT_XY) }
      else -> { super.setScaleType(scaleType) }
    }
  }

  fun showBadge() {
    viewHelper.showBadge(this)
  }

  fun hideBadge() {
    viewHelper.hideBadge(this)
  }
}