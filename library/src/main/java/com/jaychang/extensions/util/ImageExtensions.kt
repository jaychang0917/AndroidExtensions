package com.jaychang.extensions.util

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.IntRange
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.View
import java.io.*

/**
 * conversion
 * */
private fun getBitmapOptions(): BitmapFactory.Options {
  val options = BitmapFactory.Options()
  options.inPreferredConfig = Bitmap.Config.ARGB_8888
  return options
}

fun File.toBitmap(): Bitmap? {
  return BitmapFactory.decodeFile(path, getBitmapOptions())
}

fun ByteArray.toBitmap(): Bitmap? {
  return BitmapFactory.decodeByteArray(this, 0, size, getBitmapOptions())
}

fun Drawable.toBitmap(): Bitmap? {
  return (this as? BitmapDrawable)?.bitmap
}

fun @receiver:DrawableRes Int.toBitmap(context: Context): Bitmap? {
  return BitmapFactory.decodeResource(context.resources, this, getBitmapOptions())
}

fun InputStream.toBitmap(): Bitmap? {
  return BitmapFactory.decodeStream(this, null, getBitmapOptions())
}

fun View.toBitmap(): Bitmap {
  isDrawingCacheEnabled = true
  measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
  layout(0, 0, measuredWidth, measuredHeight)
  buildDrawingCache(true)
  val bitmap = Bitmap.createBitmap(drawingCache)
  isDrawingCacheEnabled = false
  return bitmap
}

fun Bitmap.toBytes(): ByteArray {
  val baos = ByteArrayOutputStream()
  compress(Bitmap.CompressFormat.JPEG, 100, baos)
  return baos.toByteArray()
}

fun Bitmap.toFile(file: File): Boolean {
  val outputStream: OutputStream?
  try {
    outputStream = FileOutputStream(file)
    compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.flush()
    outputStream.close()
    return true
  } catch (ex: Exception) {
    ex.printStackTrace()
  }
  return false
}

fun Bitmap.toDrawable(context: Context): Drawable {
  return BitmapDrawable(context.resources, this)
}

fun Drawable.toBytes(): ByteArray? {
  return toBitmap()?.toBytes()
}

fun @receiver:DrawableRes Int.toBytes(context: Context): ByteArray? {
  val drawable = ContextCompat.getDrawable(context, this)
  return drawable.toBitmap()?.toBytes()
}

fun ByteArray.toDrawable(context: Context): Drawable? {
  return toBitmap()?.toDrawable(context)
}

fun Bitmap.rgbaAt(x: Int, y: Int): FloatArray {
  val color = getPixel(x, y)
  val red = Color.red(color)
  val green = Color.green(color)
  val blue = Color.blue(color)
  val alpha = Color.alpha(color)
  return floatArrayOf(red.toFloat(), green.toFloat(), blue.toFloat(), alpha.toFloat())
}

//fun Activity.screenshot(): Bitmap {
//  val view = window.decorView
//  view.isDrawingCacheEnabled = true
//  view.buildDrawingCache()
//  val bmp = view.drawingCache
//  val statusBarHeight = AppUtils.getStatusBarHeightPixels()
//  val width = AppUtils.getScreenWidthPixels(activity)
//  val height = AppUtils.getScreenHeightPixels(activity)
//  val bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight)
//  view.destroyDrawingCache()
//  return bp
//}

fun Bitmap.toUri(context: Context): Uri {
  val bytes = ByteArrayOutputStream()
  compress(Bitmap.CompressFormat.JPEG, 100, bytes)
  val path = MediaStore.Images.Media.insertImage(context.contentResolver, this, "Title", null)
  return Uri.parse(path)
}

/**
 * transform
 * */
fun Bitmap.rotate(degree: Int): Bitmap {
  val matrix = Matrix()
  matrix.postRotate(degree.toFloat())
  return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.scale(scaleWidth: Int, scaleHeight: Int): Bitmap {
  val newBitmap = Bitmap.createScaledBitmap(this, scaleWidth, scaleHeight, false)
  if (!isRecycled) {
    recycle()
  }
  return newBitmap
}

fun Bitmap.clip(x: Int, y: Int, width: Int, height: Int): Bitmap {
  val newBitmap = Bitmap.createBitmap(this, x, y, width, height)
  if (!isRecycled) {
    recycle()
  }
  return newBitmap
}

fun Bitmap.skew(kx: Float, ky: Float, px: Float, py: Float): Bitmap {
  val matrix = Matrix()
  matrix.setSkew(kx, ky, px, py)
  val newBitmap = Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)

  if (!isRecycled) {
    recycle()
  }

  return newBitmap
}

fun Bitmap.circle(): Bitmap {
  val width = width
  val height = height
  val radius = Math.min(width, height) shr 1

  val newBitmap = Bitmap.createBitmap(width, height, config)
  val paint = Paint()
  val canvas = Canvas(newBitmap)
  val rect = Rect(0, 0, width, height)
  paint.isAntiAlias = true
  canvas.drawARGB(0, 0, 0, 0)
  canvas.drawCircle((width shr 1).toFloat(), (height shr 1).toFloat(), radius.toFloat(), paint)
  paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
  canvas.drawBitmap(this, rect, rect, paint)

  if (!isRecycled) {
    recycle()
  }

  return newBitmap
}

fun Bitmap.round(radius: Int): Bitmap {
  val width = width
  val height = height

  val newBitmap = Bitmap.createBitmap(width, height, config)
  val paint = Paint()
  val canvas = Canvas(newBitmap)
  val rect = Rect(0, 0, width, height)
  paint.isAntiAlias = true
  canvas.drawRoundRect(RectF(rect), radius.toFloat(), radius.toFloat(), paint)
  paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
  canvas.drawBitmap(this, rect, rect, paint)

  if (!isRecycled) {
    recycle()
  }

  return newBitmap
}

fun Bitmap.alpha(@IntRange(from = 0, to = 100) alpha: Int): Bitmap {
  val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
  val canvas = Canvas(newBitmap)
  val alphaPaint = Paint()
  alphaPaint.alpha = alpha
  canvas.drawBitmap(this, 0f, 0f, alphaPaint)

  if (!isRecycled) {
    recycle()
  }

  return newBitmap
}

fun Bitmap.gray(): Bitmap {
  val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
  val canvas = Canvas(newBitmap)
  val paint = Paint()
  val colorMatrix = ColorMatrix()
  colorMatrix.setSaturation(0f)
  val colorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)
  paint.colorFilter = colorMatrixColorFilter
  canvas.drawBitmap(this, 0f, 0f, paint)

  if (!isRecycled) {
    recycle()
  }

  return newBitmap
}

fun Bitmap.border(borderWidth: Int, color: Int): Bitmap {
  val doubleBorder = borderWidth shl 1
  val newWidth = width + doubleBorder
  val newHeight = height + doubleBorder

  val newBitmap = Bitmap.createBitmap(newWidth, newHeight, config)
  val canvas = Canvas(newBitmap)
  val rect = Rect(0, 0, newWidth, newHeight)
  val paint = Paint()
  paint.color = color
  paint.style = Paint.Style.STROKE
  paint.strokeWidth = doubleBorder.toFloat()
  canvas.drawRect(rect, paint)
  canvas.drawBitmap(this, borderWidth.toFloat(), borderWidth.toFloat(), null)

  if (!isRecycled) {
    recycle()
  }

  return newBitmap
}

fun Bitmap.text(context: Context, text: String, textSizeDp: Int = 8, textColor: Int = android.R.color.white): Bitmap {
  val scale = context.resources.displayMetrics.density
  val config = config ?: Bitmap.Config.ARGB_8888
  val bitmap = copy(config, true)
  val canvas = Canvas(bitmap)
  val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  paint.textSize = textSizeDp * scale
  paint.color = ContextCompat.getColor(context, textColor)
  val textBounds = Rect()
  paint.getTextBounds(text, 0, text.length, textBounds)
  val x = (width - textBounds.width()) / 2f
  val y = height / 2 - (paint.descent() + paint.ascent()) / 2
  canvas.drawText(text, x, y, paint)
  return bitmap
}

fun Drawable.tint(context: Context, @ColorRes color: Int): Drawable {
  val wrappedDrawable = DrawableCompat.wrap(this)
  DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(context, color))
  return wrappedDrawable
}

/**
 * meta
 * */
class ImageSize(width: Int, height: Int)

fun Uri.imageSize(): ImageSize {
  val options = BitmapFactory.Options()
  options.inJustDecodeBounds = true
  BitmapFactory.decodeFile(File(path).absolutePath, options)
  return ImageSize(options.outWidth, options.outHeight)
}

fun Uri.imageDegree(): Int {
  try {
    val exif = ExifInterface(path)
    val rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
    return when (rotation) {
      ExifInterface.ORIENTATION_ROTATE_90 -> 90
      ExifInterface.ORIENTATION_ROTATE_180 -> 180
      ExifInterface.ORIENTATION_ROTATE_270 -> 270
      else -> 0
    }
  } catch (ex: IOException) {
    throw RuntimeException(ex)
  }
}
