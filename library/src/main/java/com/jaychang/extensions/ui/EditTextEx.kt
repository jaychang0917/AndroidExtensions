package com.jaychang.extensions.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.Gravity
import android.widget.EditText
import com.jaychang.extensions.internal.ViewHelper

open class EditTextEx : EditText {
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

  constructor(ctx: Context) : this(ctx, null)
  constructor(ctx: Context, attrs: AttributeSet?) : super(ctx, attrs) {
    setup(ctx, attrs)
  }
  constructor(ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr) {
    setup(ctx, attrs)
  }

  private fun setup(ctx: Context, attrs: AttributeSet?) {
    viewHelper.init(this, ctx, attrs)
    background = null
    gravity = Gravity.TOP
  }

  override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
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