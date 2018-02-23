package com.jaychang.extensions.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView
import com.jaychang.extensions.internal.ViewHelper

open class ImageViewEx : ImageView {
  private val viewHelper = ViewHelper()

  var radius = 0f
    set(value) {
      viewHelper.radius = value
    }
  var topLeftRadius = 0f
    set(value) {
      viewHelper.topLeftRadius = value
    }
  var topRightRadius = 0f
    set(value) {
      viewHelper.topRightRadius = value
    }
  var bottomLeftRadius = 0f
    set(value) {
      viewHelper.bottomLeftRadius = value
    }
  var bottomRightRadius = 0f
    set(value) {
      viewHelper.bottomRightRadius = value
    }
  var borderWidth = 0
    set(value) {
      viewHelper.borderWidth = value
    }
  var borderColor = 0
    set(value) {
      viewHelper.borderColor = value
    }
  var isBadgeVisible = false
    set(value) {
      viewHelper.isBadgeVisible = value
    }

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