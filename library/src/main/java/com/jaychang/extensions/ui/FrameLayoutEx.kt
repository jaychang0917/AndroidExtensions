package com.jaychang.extensions.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.FrameLayout
import com.jaychang.extensions.internal.ViewHelper

open class FrameLayoutEx : FrameLayout {
  private val viewHelper = ViewHelper()

  constructor(ctx: Context) : this(ctx, null, 0)
  constructor(ctx: Context, attrs: AttributeSet?) : this(ctx, attrs, 0)
  constructor(ctx: Context, attrs: AttributeSet?, defaultStyle: Int) : super(ctx, attrs, defaultStyle) {
    viewHelper.init(this, ctx, attrs)
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

  fun showBadge() {
    viewHelper.showBadge(this)
  }

  fun hideBadge() {
    viewHelper.hideBadge(this)
  }
}