package com.jaychang.extensions.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView

class RoundImageView : ImageView {
  private val roundViewHelper = RoundViewHelper()

  constructor(ctx: Context) : this(ctx, null, 0)
  constructor(ctx: Context, attrs: AttributeSet?) : this(ctx, attrs, 0)
  constructor(ctx: Context, attrs: AttributeSet?, defaultStyle: Int) : super(ctx, attrs, defaultStyle) {
    roundViewHelper.init(this, ctx, attrs)
    if (scaleType == ScaleType.FIT_CENTER) {
      scaleType = ScaleType.FIT_CENTER
    }
  }

  override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
    roundViewHelper.onSizeChanged(width, height)
  }

  override fun draw(canvas: Canvas) {
    roundViewHelper.draw(canvas)
    super.draw(canvas)
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
}