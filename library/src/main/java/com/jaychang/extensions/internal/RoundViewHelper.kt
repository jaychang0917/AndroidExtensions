package com.jaychang.extensions.internal

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.jaychang.extensions.R

internal class RoundViewHelper {
  var radius = 0f
  var topLeftRadius = 0f
  var topRightRadius = 0f
  var bottomLeftRadius = 0f
  var bottomRightRadius = 0f

  var path = Path()

  fun init(view: View, ctx: Context, attrs: AttributeSet?) {
    val viewAttrs = intArrayOf(R.attr.round_radius, R.attr.round_topLeftRadius, R.attr.round_topRightRadius, R.attr.round_bottomLeftRadius, R.attr.round_bottomRightRadius)
    val typeArray = ctx.obtainStyledAttributes(attrs, viewAttrs)
    radius = typeArray.getDimensionPixelSize(0, 0).toFloat()
    topLeftRadius = typeArray.getDimensionPixelSize(1, 0).toFloat()
    topRightRadius = typeArray.getDimensionPixelSize(2, 0).toFloat()
    bottomLeftRadius = typeArray.getDimensionPixelSize(3, 0).toFloat()
    bottomRightRadius = typeArray.getDimensionPixelSize(4, 0).toFloat()
    typeArray.recycle()

    view.setWillNotDraw(false)
  }

  fun onSizeChanged(width: Int, height: Int) {
    val radii = if (radius != 0f) {
      floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)
    } else {
      floatArrayOf(topLeftRadius, topLeftRadius, topRightRadius, topRightRadius, bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius)
    }

    path.addRoundRect(RectF(0f, 0f, width.toFloat(), height.toFloat()), radii, Path.Direction.CW)
  }

  fun draw(canvas: Canvas) {
    canvas.clipPath(path)
  }
}