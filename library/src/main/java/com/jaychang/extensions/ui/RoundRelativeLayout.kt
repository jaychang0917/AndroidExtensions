package com.jaychang.extensions.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.RelativeLayout

class RoundRelativeLayout : RelativeLayout {
  private val roundViewHelper = RoundViewHelper()

  constructor(ctx: Context) : this(ctx, null, 0)
  constructor(ctx: Context, attrs: AttributeSet?) : this(ctx, attrs, 0)
  constructor(ctx: Context, attrs: AttributeSet?, defaultStyle: Int) : super(ctx, attrs, defaultStyle) {
    roundViewHelper.init(this, ctx, attrs)
  }

  override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
    roundViewHelper.onSizeChanged(width, height)
  }

  override fun draw(canvas: Canvas) {
    roundViewHelper.draw(canvas)
    super.draw(canvas)
  }
}