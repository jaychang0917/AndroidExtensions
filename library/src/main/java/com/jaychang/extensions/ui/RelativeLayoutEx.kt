package com.jaychang.extensions.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.jaychang.extensions.internal.ViewHelper

open class RelativeLayoutEx : RelativeLayout {
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