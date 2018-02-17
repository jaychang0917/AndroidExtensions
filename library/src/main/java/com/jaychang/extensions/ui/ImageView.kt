package com.jaychang.extensions.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView
import com.jaychang.extensions.internal.ViewHelper

class ImageView : ImageView {
  private val viewHelper = ViewHelper()

  constructor(ctx: Context) : this(ctx, null, 0)
  constructor(ctx: Context, attrs: AttributeSet?) : this(ctx, attrs, 0)
  constructor(ctx: Context, attrs: AttributeSet?, defaultStyle: Int) : super(ctx, attrs, defaultStyle) {
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