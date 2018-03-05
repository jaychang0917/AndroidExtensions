package com.jaychang.extensions.util

import android.app.Activity
import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.os.Process
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.reflect.KClass



/**
 * system
 * */
fun Context.sendSMS(phoneNumber: String, smsContent: String) {
  val uri = Uri.parse("smsto:" + phoneNumber)
  val it = Intent(Intent.ACTION_SENDTO, uri)
  it.putExtra("sms_body", smsContent)
  it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
  startActivity(it)
}

fun Context.openPhoneCall(phoneNumber: String) {
  val uri = Uri.parse("tel:" + phoneNumber)
  val intent = Intent(Intent.ACTION_VIEW, uri)
  intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
  startActivity(intent)
}

fun Context.openGoogleMap(lat: Double, lon: Double) {
  val gmmIntentUri = Uri.parse("geo:$lat,$lon")
  val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
  mapIntent.setPackage("com.google.android.apps.maps")
  if (mapIntent.resolveActivity(packageManager) != null) {
    startActivity(mapIntent)
  }
}

fun Context.sendEmail(emails: Array<String>, subject: String? = null, attachments: Array<File>? = null) {
  val intent = Intent(Intent.ACTION_SEND)
  intent.type = "vnd.android.cursor.dir/email"
  intent.putExtra(Intent.EXTRA_EMAIL, emails)
  attachments?.let {
    intent.action = Intent.ACTION_SEND_MULTIPLE
    val uris = it.mapTo(ArrayList<Uri>()) { Uri.fromFile(it) }
    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
  }
  subject?.let {
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
  }
  startActivity(Intent.createChooser(intent, ""))
}

fun Context.showKeyboard(view: View) {
  if (view.requestFocus()) {
    val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
  }
}

fun Context.hideKeyboard(view: View) {
  val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(view.windowToken, 0)
}

private fun Context.scanFile(file: File) {
  MediaScannerConnection.scanFile(applicationContext, arrayOf(file.toString()), null, null)
}

private fun Activity.saveImage(file: File, isPublic: Boolean = true, listener: ((File) -> Unit)? = null) {
  AsyncTask.execute {
    try {
      val root = if (isPublic) Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) else Environment.getExternalStorageDirectory()
      val extension = file.extension.orIfEmpty("jpg")
      val imageFile = File(root.toString(), UUID.randomUUID().toString() + ".$extension")

      if (!imageFile.exists()) {
        imageFile.createNewFile()
      }
      FileUtils.copyFile(file, imageFile)
      scanFile(imageFile)

      listener?.let {
        runOnUiThread { listener.invoke(imageFile) }
      }
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }
}

fun Activity.saveImageToAlbum(file: File, listener: ((File) -> Unit)? = null) {
  saveImage(file, true, listener)
}

fun Activity.saveImageToAppDir(file: File, listener: ((File) -> Unit)? = null) {
  saveImage(file, false, listener)
}

fun Activity.takeVideo(requestCode: Int): Boolean {
  val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
  if (intent.resolveActivity(packageManager) != null) {
    startActivityForResult(intent, requestCode)
    return true
  }
  return false
}

fun Fragment.takeVideo(requestCode: Int): Boolean {
  val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
  if (intent.resolveActivity(activity!!.packageManager) != null) {
    startActivityForResult(intent, requestCode)
    return true
  }
  return false
}

fun Context.openUrl(url: String) {
  val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
  startActivity(intent)
}

fun Context.openFbMessenger(fbUserId: String) {
  startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("fb://messaging/" + fbUserId)))
}

fun Context.openImage(imagePath: String) {
  val intent = Intent(Intent.ACTION_VIEW)
  intent.addCategory(Intent.CATEGORY_DEFAULT)
  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
  intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
  val uri = Uri.fromFile(File(imagePath))
  intent.setDataAndType(uri, "image/*")
  startActivity(intent)
}

fun Context.openVideo(videoPath: String) {
  val intent = Intent(Intent.ACTION_VIEW)
  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
  intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
  intent.putExtra("oneshot", 0)
  intent.putExtra("configchange", 0)
  val uri = Uri.fromFile(File(videoPath))
  intent.setDataAndType(uri, "video/*")
  startActivity(intent)
}

fun Context.openAppInGooglePlay(packageName: String) {
  try {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)))
  } catch (e: ActivityNotFoundException) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)))
  }
}

fun Context.isServiceRunning(clazz: KClass<*>): Boolean {
  val manager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
  return manager.getRunningServices(Integer.MAX_VALUE).any { clazz.java.name == it.service.className }
}

val Context.clipboardText: CharSequence?
  get() {
    val clipboard = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    return if (clipboard.hasPrimaryClip()) {
      clipboard.primaryClip.getItemAt(0).coerceToText(this)
    } else {
      null
    }
  }

/**
 * meta
 * */
val Context.appName: String
  get() {
    val stringId = applicationInfo.labelRes
    return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else getString(stringId)
  }

val Context.versionName: String
  get() {
    return try {
      packageManager.getPackageInfo(packageName, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
      ""
    }
  }

val Context.versionCode: Int
  get() {
    return try {
      packageManager.getPackageInfo(packageName, 0).versionCode
    } catch (e: PackageManager.NameNotFoundException) {
      -1
    }
  }

val Context.processName: String
  get() {
    val pid = Process.myPid()
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return activityManager.runningAppProcesses.first { it.pid == pid }.processName ?: ""
  }

fun Context.getLaunchIntent(): Intent {
  return packageManager.getLaunchIntentForPackage(packageName)
}

fun Context.getMetaDataValue(name: String): String? {
  val appInfo: ApplicationInfo = try {
    packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
  } catch (e: PackageManager.NameNotFoundException) {
    return null
  }

  return (appInfo.metaData.get(name) as String).trim()
}
