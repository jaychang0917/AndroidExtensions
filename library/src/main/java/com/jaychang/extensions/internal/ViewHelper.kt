package com.jaychang.extensions.internal

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.jaychang.extensions.R
import com.jaychang.extensions.util.dpToPx

internal class ViewHelper {
  private var radius = 0f
  private var topLeftRadius = 0f
  private var topRightRadius = 0f
  private var bottomLeftRadius = 0f
  private var bottomRightRadius = 0f
  private var path = Path()

  private var borderWidth = 0
  private var borderColor = 0
  private var paint = Paint()

  fun init(view: View, ctx: Context, attrs: AttributeSet?) {
    view.setWillNotDraw(false)

    val viewAttrs = intArrayOf(
      R.attr.view_radius, 0,
      R.attr.view_topLeftRadius, 0,
      R.attr.view_topRightRadius, 0,
      R.attr.view_bottomLeftRadius, 0,
      R.attr.view_bottomRightRadius, 0,
      R.attr.view_border_width, 0,
      R.attr.view_border_color
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

    paint.color = borderColor
    paint.strokeWidth = dpToPx(borderWidth).toFloat()
    paint.style = Paint.Style.STROKE
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
    canvas.clipPath(path)
  }

  fun drawPath(canvas: Canvas) {
    canvas.drawPath(path, paint)
  }
}