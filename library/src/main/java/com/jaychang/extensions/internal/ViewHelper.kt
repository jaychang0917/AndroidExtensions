package com.jaychang.extensions.internal

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.jaychang.extensions.R

internal class ViewHelper {
  private lateinit var view: View

  private var radius = 0f
  private var topLeftRadius = 0f
  private var topRightRadius = 0f
  private var bottomLeftRadius = 0f
  private var bottomRightRadius = 0f
  private var path = Path()

  private var borderWidth = 0
  private var borderColor = 0
  private var borderPaint = Paint()

  private var badgeColor = 0
  private var badgeRadius = 0
  private var badgeInset = 0
  private var badgePaint = Paint()

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
      R.attr.view_borderColor, 0,
      R.attr.view_badgeRadius, 0,
      R.attr.view_badgeColor, 0,
      R.attr.view_badgeInset
    )
    val typeArray = ctx.obtainStyledAttributes(attrs, viewAttrs)

    radius = typeArray.getDimensionPixelSize(0, 0).toFloat()
    topLeftRadius = typeArray.getDimensionPixelSize(2, 0).toFloat()
    topRightRadius = typeArray.getDimensionPixelSize(4, 0).toFloat()
    bottomLeftRadius = typeArray.getDimensionPixelSize(6, 0).toFloat()
    bottomRightRadius = typeArray.getDimensionPixelSize(8, 0).toFloat()

    borderWidth = typeArray.getDimensionPixelSize(10, 0)
    borderColor = typeArray.getColor(12, Color.TRANSPARENT)

    badgeRadius = typeArray.getDimensionPixelSize(14, 0)
    badgeColor = typeArray.getColor(16, Color.RED)
    badgeInset = typeArray.getDimensionPixelSize(18, 0)

    typeArray.recycle()

    borderPaint.color = borderColor
    borderPaint.strokeWidth = borderWidth.toFloat()
    borderPaint.style = Paint.Style.STROKE

    badgePaint.color = badgeColor
  }

  fun onSizeChanged(width: Int, height: Int) {
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
    val bound = canvas.clipBounds
    bound.inset(-badgeRadius - badgeInset, -badgeRadius - badgeInset)
    canvas.clipRect(bound, Region.Op.REPLACE)
    canvas.drawCircle(view.width.toFloat() + badgeInset, 0f - badgeInset, badgeRadius.toFloat(), badgePaint)
  }
}