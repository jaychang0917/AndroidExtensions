package com.jaychang.extensions.internal

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.jaychang.extensions.R
import com.jaychang.extensions.ui.BadgeConfig

internal class ViewHelper {
  private lateinit var view: View

  var radius = 0f
  var topLeftRadius = 0f
  var topRightRadius = 0f
  var bottomLeftRadius = 0f
  var bottomRightRadius = 0f
  private var path = Path()

  var borderWidth = 0
  var borderColor = 0
  private var borderPaint = Paint()

  var isBadgeVisible = false
  private var isBadgeDrawingRequested = false
  private var badgeConfig = BadgeConfig()

  fun init(view: View, ctx: Context, attrs: AttributeSet?) {
    this.view = view

    view.setWillNotDraw(false)

    val viewAttrs = intArrayOf(
      R.attr.view_radius, 0,
      R.attr.view_topLeftRadius, 0,
      R.attr.view_topRightRadius, 0,
      R.attr.view_bottomLeftRadius, 0,
      R.attr.view_bottomRightRadius, 0,
      R.attr.view_borderWidth, 0,
      R.attr.view_borderColor, 0
    )
    val typeArray = ctx.obtainStyledAttributes(attrs, viewAttrs)

    radius = typeArray.getDimensionPixelSize(0, 0).toFloat()
    topLeftRadius = typeArray.getDimensionPixelSize(2, 0).toFloat()
    topRightRadius = typeArray.getDimensionPixelSize(4, 0).toFloat()
    bottomLeftRadius = typeArray.getDimensionPixelSize(6, 0).toFloat()
    bottomRightRadius = typeArray.getDimensionPixelSize(8, 0).toFloat()

    borderWidth = typeArray.getDimensionPixelSize(10, 0)
    borderColor = typeArray.getColor(12, Color.TRANSPARENT)

    typeArray.recycle()

    borderPaint.color = borderColor
    borderPaint.strokeWidth = borderWidth.toFloat()
    borderPaint.style = Paint.Style.STROKE
  }

  fun onMeasure(width: Int, height: Int) {
    val radii = if (radius != 0f) {
      floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)
    } else {
      floatArrayOf(topLeftRadius, topLeftRadius, topRightRadius, topRightRadius, bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius)
    }

    path.addRoundRect(RectF(0f, 0f, width.toFloat(), height.toFloat()), radii, Path.Direction.CW)
  }

  fun clipPath(canvas: Canvas) {
    canvas.save()
    canvas.clipPath(path)
  }

  fun drawBorder(canvas: Canvas) {
    canvas.drawPath(path, borderPaint)
    canvas.restore()
  }

  fun drawBadge(canvas: Canvas) {
    if (!isBadgeDrawingRequested) {
      return
    }

    val bound = canvas.clipBounds
    bound.inset(-badgeConfig.radius - badgeConfig.inset, -badgeConfig.radius - badgeConfig.inset)
    canvas.clipRect(bound, Region.Op.REPLACE)
    val paint = Paint()
    paint.color = if (isBadgeVisible) ContextCompat.getColor(view.context, badgeConfig.color) else Color.TRANSPARENT
    canvas.drawCircle(view.width.toFloat() + badgeConfig.inset, 0f - badgeConfig.inset, badgeConfig.radius.toFloat(), paint)
  }

  fun showBadge(view: android.view.View) {
    isBadgeVisible = true
    isBadgeDrawingRequested = true
    view.invalidate()
  }

  fun hideBadge(view: android.view.View) {
    isBadgeVisible = false
    isBadgeDrawingRequested = true
    view.invalidate()
  }
}