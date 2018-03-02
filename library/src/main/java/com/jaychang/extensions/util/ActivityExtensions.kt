package com.jaychang.extensions.util

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
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

private fun internalTakePhotoFromCamera(activity: Activity, requestCode: Int): File? {
  val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
  if (intent.resolveActivity(activity.packageManager) != null) {
    var photoFile: File? = null
    try {
      photoFile = activity.createImageFile()
    } catch (ex: IOException) {
      ex.printStackTrace()
    }

    if (photoFile != null) {
      val uri = FileProvider.getUriForFile(activity, activity.packageName + ".androidextensionsfileprovider", photoFile)
      intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
      activity.startActivityForResult(intent, requestCode)
      return photoFile
    }
  } else {
    Log.d(activity.javaClass.simpleName, "No camera app.")
  }

  return null
}

fun Activity.takePhotoFromCamera(requestCode: Int): File? {
  return internalTakePhotoFromCamera(this, requestCode)
}

fun Fragment.takePhotoFromCamera(requestCode: Int): File? {
  return internalTakePhotoFromCamera(activity!!, requestCode)
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

fun Activity.toggleFullScreen() {
  val decorView = window.decorView
  decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION + View.SYSTEM_UI_FLAG_FULLSCREEN
}

var Activity.isStatusBarTranslucent: Boolean
  get() {
    if (Build.VERSION.SDK_INT < 19) {
      throw RuntimeException("API level must >= 19")
    } else {
      val option = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
      return window.attributes.flags and option != 0
    }
  }
  set(isTranslucent) {
    if (Build.VERSION.SDK_INT < 19) return

    val option = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
    if (isTranslucent) {
      if (!isStatusBarTranslucent) {
        window.addFlags(option)
      }
    } else {
      if (isStatusBarTranslucent) {
        window.clearFlags(option)
      }
    }
  }

var Activity.isNavigationTranslucent: Boolean
  get() {
    if (Build.VERSION.SDK_INT < 19) {
      throw RuntimeException("API level must >= 19")
    } else {
      val option = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
      return window.attributes.flags and option != 0
    }
  }
  set(isTranslucent) {
    if (Build.VERSION.SDK_INT < 19) return

    val option = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
    if (isTranslucent) {
      if (!isNavigationTranslucent) {
        window.addFlags(option)
      }
    } else {
      if (isNavigationTranslucent) {
        window.clearFlags(option)
      }
    }
  }

var Activity.navigationBarColor: Int
  get() {
    if (Build.VERSION.SDK_INT < 21) {
      throw RuntimeException("API level must >= 21")
    } else {
      return window.navigationBarColor
    }
  }
  set(color) {
    if (Build.VERSION.SDK_INT < 21) {
      return
    }

    window.navigationBarColor = color
  }

var Activity.statusBarColor: Int
  get() {
    if (Build.VERSION.SDK_INT < 21) {
      throw RuntimeException("API level must >= 21")
    } else {
      return window.statusBarColor
    }
  }
  set(color) {
    if (Build.VERSION.SDK_INT < 21) {
      return
    }

    window.statusBarColor = color
  }

var Activity.isContentUnderStatusBar: Boolean
  get() {
    val decorView = window.decorView
    val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN + View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    return decorView.systemUiVisibility and option != 0
  }
  set(isUnder) {
    if (Build.VERSION.SDK_INT < 21) return

    val decorView = window.decorView
    val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN + View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    if (isUnder) {
      if (!isContentUnderStatusBar) {
        decorView.systemUiVisibility += option
      }
    } else {
      if (isContentUnderStatusBar) {
        decorView.systemUiVisibility -= option
      }
    }
  }

var Activity.isStatusBarVisible: Boolean
  get() {
    val decorView = window.decorView
    return decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_FULLSCREEN != 0
  }
  set(visible) {
    val decorView = window.decorView
    val option = View.SYSTEM_UI_FLAG_FULLSCREEN
    if (visible) {
      if (!isStatusBarVisible) {
        decorView.systemUiVisibility -= option
      }
    } else {
      if (isStatusBarVisible) {
        decorView.systemUiVisibility += option
      }
    }
  }

var Activity.isScreenAlwaysOn: Boolean
  get() {
    return window.attributes.flags and WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON != 0
  }
  set(isOn) {
    if (isOn) {
      window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    } else {
      window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
  }

fun Activity.showMessageDialog(message: String, title: String? = null, okText:String, okButtonClickListener: (()->Unit)? = null) {
  AlertDialog.Builder(this)
    .setTitle(title)
    .setMessage(message)
    .setPositiveButton(okText) { _, _ -> okButtonClickListener?.invoke() }
    .show()
}

fun Activity.showItemsDialog(title: String? = null, items: Array<String>, listener: ((DialogInterface, Int)->Unit)? = null) {
  AlertDialog.Builder(this)
    .setTitle(title)
    .setItems(items, listener)
    .show()
}

fun Activity.showMultiChoiceDialog(title: String? = null, items: Array<String>, selectedItems: BooleanArray, listener: ((DialogInterface, Int, Boolean)->Unit)? = null) {
  AlertDialog.Builder(this)
    .setTitle(title)
    .setMultiChoiceItems(items, selectedItems, listener)
    .show()
}

fun Activity.showSingleChoiceDialog(title: String? = null, items: Array<String>, checkedItem: Int, listener: ((DialogInterface, Int)->Unit)? = null) {
  AlertDialog.Builder(this)
    .setTitle(title)
    .setSingleChoiceItems(items, checkedItem, listener)
    .show()
}
