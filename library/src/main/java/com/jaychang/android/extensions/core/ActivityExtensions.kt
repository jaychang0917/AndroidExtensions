package com.jaychang.android.extensions.core

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.ColorRes
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import java.io.File
import java.io.IOException
import java.util.*


val PERMISSION_READ_CALENDAR = Manifest.permission.READ_CALENDAR
val PERMISSION_WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR
val PERMISSION_CAMERA = Manifest.permission.CAMERA
val PERMISSION_READ_CONTACTS = Manifest.permission.READ_CONTACTS
val PERMISSION_WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS
val PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS
val PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
val PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
val PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO
val PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE
val PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE
val PERMISSION_READ_CALL_LOG = Manifest.permission.READ_CALL_LOG
val PERMISSION_WRITE_CALL_LOG = Manifest.permission.WRITE_CALL_LOG
val PERMISSION_ADD_VOICEMAIL = Manifest.permission.ADD_VOICEMAIL
val PERMISSION_USE_SIP = Manifest.permission.USE_SIP
val PERMISSION_PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS
val PERMISSION_BODY_SENSORS = Manifest.permission.BODY_SENSORS
val PERMISSION_SEND_SMS = Manifest.permission.SEND_SMS
val PERMISSION_RECEIVE_SMS = Manifest.permission.RECEIVE_SMS
val PERMISSION_READ_SMS = Manifest.permission.READ_SMS
val PERMISSION_RECEIVE_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH
val PERMISSION_RECEIVE_MMS = Manifest.permission.RECEIVE_MMS
val PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
val PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE

private fun internalRequestPermissionsIfNeed(activity: AppCompatActivity, requestCode: Int, permissions: Array<out String>) {
  val result = permissions.filter { ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED }.toTypedArray()
  if (result.isNotEmpty()) {
    ActivityCompat.requestPermissions(activity, result, requestCode)
  } else {
    val grantResult = IntArray(1) { PackageManager.PERMISSION_GRANTED }
    activity.onRequestPermissionsResult(requestCode, permissions, grantResult)
  }
}

fun AppCompatActivity.requestPermissionsIfNeed(requestCode: Int, vararg permissions: String) {
  internalRequestPermissionsIfNeed(this, requestCode, permissions)
}

fun Fragment.requestPermissionsIfNeed(requestCode: Int, vararg permissions: String) {
  internalRequestPermissionsIfNeed(activity as AppCompatActivity, requestCode, permissions)
}

fun isPermissionGranted(grantResults: IntArray): Boolean {
  return grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
}

fun Context.isResultOK(resultCode: Int): Boolean = resultCode == Activity.RESULT_OK

fun Context.isResultCanceled(resultCode: Int): Boolean = resultCode == Activity.RESULT_CANCELED

@Throws(IOException::class)
private fun Activity.createImageFile(): File {
  val imageFileName = "Image_" + UUID.randomUUID() + ".jpg"
  return File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), imageFileName)
}

private fun internalGetPhotoFromCamera(activity: Activity, requestCode: Int): Uri? {
  val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
  if (intent.resolveActivity(activity.packageManager) != null) {
    var photoFile: File? = null
    try {
      photoFile = activity.createImageFile()
    } catch (ex: IOException) {
      ex.printStackTrace()
    }

    if (photoFile != null) {
      val uri = FileProvider.getUriForFile(activity, "com.jaychang.android.extensions.provider", photoFile)
      intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      activity.startActivityForResult(intent, requestCode)
      return uri
    }
  } else {
    Log.d(activity.javaClass.simpleName, "No camera app.")
  }

  return null
}

fun Activity.getPhotoFromCamera(requestCode: Int): Uri? {
  return internalGetPhotoFromCamera(this, requestCode)
}

fun Fragment.getPhotoFromCamera(requestCode: Int): Uri? {
  return internalGetPhotoFromCamera(activity, requestCode)
}

@TargetApi(18)
fun Activity.getPhotoFromAlbum(requestCode: Int, allowMultiple: Boolean = true) {
  val intent = Intent(Intent.ACTION_PICK)
  intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple)
  intent.type = "image/*"
  //intent.action = Intent.ACTION_GET_CONTENT
  startActivityForResult(Intent.createChooser(intent, ""), requestCode)
}

@TargetApi(18)
fun Intent.getPhoto(context: Context): Uri {
  val result: String
  val cursor = context.contentResolver.query(data, null, null, null, null)
  if (cursor == null) {
    result = data.path
  } else {
    cursor.moveToFirst()
    val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
    result = cursor.getString(idx)
    cursor.close()
  }
  return Uri.fromFile(File(result))
}

fun Activity.setLandscapeMode() {
  requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

fun Activity.setPortraitMode() {
  requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

fun Activity.setAutoHideKeyboardWhenTouchOutside() {
  if (window == null) {
    return
  }

  window.decorView.setOnTouchListener { _, _ ->
    val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (currentFocus != null) {
      imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    } else {
      imm.hideSoftInputFromWindow(findViewById<View>(android.R.id.content).windowToken, 0)
    }
    false
  }
}

@TargetApi(19)
fun Activity.setStatusBarTranslucent() {
  if (Build.VERSION.SDK_INT < 19 || window == null) {
    return
  }

  window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
}

@TargetApi(19)
fun Activity.clearStatusBarTranslucent() {
  if (Build.VERSION.SDK_INT < 19 || window == null) {
    return
  }

  window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
}

@TargetApi(21)
fun Activity.setStatusBarColor(@ColorRes color: Int) {
  if (Build.VERSION.SDK_INT < 21 || window == null) {
    return
  }

  window.statusBarColor = ContextCompat.getColor(this, color)
}

@TargetApi(21)
fun Activity.setContentBehindStatusBar() {
  if (Build.VERSION.SDK_INT < 21 || window == null) {
    return
  }

  val decorView = window.decorView
  val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
  decorView.systemUiVisibility = option
}

@TargetApi(19)
fun Activity.setNavigationBarTranslucent() {
  if (Build.VERSION.SDK_INT < 19 || window == null) {
    return
  }

  window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
}

@TargetApi(19)
fun Activity.clearNavigationBarTranslucent(activity: Activity) {
  if (Build.VERSION.SDK_INT < 19 || activity.window == null) {
    return
  }

  activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
}

@TargetApi(21)
fun Activity.setNavigationBarColor(@ColorRes color: Int) {
  if (Build.VERSION.SDK_INT < 21 || window == null) {
    return
  }

  window.navigationBarColor = ContextCompat.getColor(this, color)
}

fun Activity.setFullscreenToggleable() {
  if (window == null) {
    return
  }
  val decorView = window.decorView
  val option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
  decorView.systemUiVisibility = option
}

fun Activity.fullscreen() {
  requestWindowFeature(Window.FEATURE_NO_TITLE)
  window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
}

