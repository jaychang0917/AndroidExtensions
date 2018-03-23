package com.jaychang.extensions.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Region
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import com.jaychang.extensions.R

open class BottomNavigationViewEx : com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx {
  constructor(ctx: Context) : this(ctx, null, 0)
  constructor(ctx: Context, attrs: AttributeSet?) : this(ctx, attrs, 0)
  constructor(ctx: Context, attrs: AttributeSet?, defaultStyle: Int) : super(ctx, attrs, defaultStyle) {
    val typedArray = context.theme
      .obtainStyledAttributes(attrs, R.styleable.BottomNavigationView, defaultStyle, 0)

    val isShiftModeEnabled = typedArray.getBoolean(R.styleable.BottomNavigationViewEx_itemShiftModeEnabled, false)
    val isIconOnly = typedArray.getBoolean(R.styleable.BottomNavigationViewEx_itemIconOnly, false)
    val isTextOnly = typedArray.getBoolean(R.styleable.BottomNavigationViewEx_itemTextOnly, false)
    val textSizePx = typedArray.getDimensionPixelSize(R.styleable.BottomNavigationViewEx_itemTextSize, 0)

    this.isShiftModeEnabled = isShiftModeEnabled
    this.isIconOnly = isIconOnly
    this.isTextOnly = isTextOnly
    this.textSizeSp = px2sp(context, textSizePx.toFloat())
  }

  var isShiftModeEnabled = false
    set(value) {
      enableItemShiftingMode(value)
      enableAnimation(value)
      enableShiftingMode(value)
      field = value
    }

  var isIconOnly = false
    set(value) {
      if (value) {
        setTextVisibility(!value)
        field = value
      }
    }

  var isTextOnly = false
    set(value) {
      if (value) {
        setIconVisibility(!value)
        field = value
      }
    }

  var textSizeSp = 0
    set(value) {
      if (value != 0) {
        setTextSize(value.toFloat())
        field = value
      }
    }

  var badgeConfig = BadgeConfig()
  private var badgeTargetView: android.widget.ImageView? = null
  private var isBadgeVisible = false

  override fun draw(canvas: Canvas) {
    super.draw(canvas)
    badgeTargetView?.let {
      drawBadgeInternal(canvas, it, isBadgeVisible)
    }
  }

  fun showBadge(pos: Int) {
    badgeTargetView = getIconAt(pos)
    isBadgeVisible = true
    invalidate()
  }

  fun hideBadge(pos: Int) {
    badgeTargetView = getIconAt(pos)
    isBadgeVisible = false
    invalidate()
  }

  private fun drawBadgeInternal(canvas: Canvas, view: android.widget.ImageView, isVisible: Boolean) {
    val badgePaint = Paint()
    badgePaint.color = if (isVisible) ContextCompat.getColor(context, badgeConfig.color) else Color.TRANSPARENT
    val bound = canvas.clipBounds
    bound.inset(-badgeConfig.radius - badgeConfig.inset, -badgeConfig.radius - badgeConfig.inset)
    canvas.clipRect(bound, Region.Op.REPLACE)
    val location = intArrayOf(0, 0)
    view.getLocationOnScreen(location)
    val offsetY = (height - itemHeight) / 2
    canvas.drawCircle(location[0].toFloat() + view.width + badgeConfig.inset, view.top.toFloat() - badgeConfig.inset + offsetY, badgeConfig.radius.toFloat(), badgePaint)
  }

  private fun px2sp(context: Context, px: Float): Int {
    val fontScale = context.applicationContext.resources.displayMetrics.scaledDensity
    return (px / fontScale + 0.5f).toInt()
  }
}