package com.jaychang.extensions.util

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.support.annotation.RequiresPermission
import android.telephony.TelephonyManager
import java.util.*

fun dpToPx(dp: Int): Int {
  val density = Resources.getSystem().displayMetrics.density
  return (dp * density + 0.5f).toInt()
}

fun pxToDp(px: Int): Int {
  val density = Resources.getSystem().displayMetrics.density
  return (px / density + 0.5f).toInt()
}

fun spToPx(sp: Int): Int {
  val fontScale = Resources.getSystem().displayMetrics.scaledDensity
  return (sp * fontScale + 0.5f).toInt()
}

fun pxToSp(px: Int): Int {
  val fontScale = Resources.getSystem().displayMetrics.scaledDensity
  return (px / fontScale + 0.5f).toInt()
}

fun isLandscape(): Boolean {
  return Resources.getSystem().configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

fun isPortrait(): Boolean {
  return Resources.getSystem().configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}

fun isSDCardEnable(): Boolean {
  return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
}

val deviceManufacturer: String = Build.MANUFACTURER

val deviceModel: String = Build.MODEL

val osVersion: String = Build.VERSION.RELEASE

val screenInch: Double
  get() {
    val metrics = Resources.getSystem().displayMetrics
    val widthPixels = metrics.widthPixels
    val heightPixels = metrics.heightPixels
    val widthDpi = metrics.xdpi
    val heightDpi = metrics.ydpi
    val widthInches = widthPixels / widthDpi
    val heightInches = heightPixels / heightDpi
    return Math.sqrt((widthInches * widthInches + heightInches * heightInches).toDouble())
  }

val screenHeight: Int
  get() {
    return Resources.getSystem().displayMetrics.heightPixels - statusBarHeight
  }

val screenHeightWithStatusBar: Int
  get() {
    return Resources.getSystem().displayMetrics.heightPixels
  }

val screenWidth: Int
  get() {
    return Resources.getSystem().displayMetrics.widthPixels
  }

val statusBarHeight: Int
  get() {
    var result = -1
    val resources = Resources.getSystem()
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
      result = resources.getDimensionPixelSize(resourceId)
    }
    return result
  }

val navigationBarHeight: Int
  get() {
    var result = -1
    val resources = Resources.getSystem()
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceId > 0) {
      result = resources.getDimensionPixelSize(resourceId)
    }
    return result
  }

@SuppressLint("HardwareIds")
@RequiresPermission(value = Manifest.permission.ACCESS_WIFI_STATE)
fun Context.macAddress(): String? {
  val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
  return wifiManager.connectionInfo.macAddress
}

@RequiresPermission(value = Manifest.permission.READ_PHONE_STATE)
fun Context.isPhone(): Boolean {
  val tm = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
  return tm?.phoneType != TelephonyManager.PHONE_TYPE_NONE
}

@RequiresPermission(value = Manifest.permission.READ_PHONE_STATE)
fun Context.deviceId(): String {
  val manager = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
  if (manager.deviceId != null) {
    return manager.deviceId
  }
  if (Build.VERSION.SDK_INT >= 26) {
    return Build.getSerial()
  } else {
    return Build.SERIAL
  }
}

@RequiresPermission(value = Manifest.permission.READ_PHONE_STATE)
fun Context.hasSimCard(): Boolean {
  val tm = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
  return tm?.simState == TelephonyManager.SIM_STATE_READY
}

@RequiresPermission(value = Manifest.permission.READ_PHONE_STATE)
fun Context.simCardOperatorName(): String {
  val tm = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
  return tm?.simOperatorName ?: ""
}

@RequiresPermission(value = Manifest.permission.READ_PHONE_STATE)
fun Context.simCardOperatorCode(): String {
  val tm = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
  return tm?.simOperator ?: ""
}

@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isNetworkConnected(): Boolean {
  val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  val activeNetwork = cm.activeNetworkInfo
  return activeNetwork != null && activeNetwork.isConnected
}

@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isUsingWifi(): Boolean {
  val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  val activeNetwork = cm.activeNetworkInfo
  return activeNetwork != null && activeNetwork.isAvailable && activeNetwork.type == ConnectivityManager.TYPE_WIFI
}

@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isUsingMobileData(): Boolean {
  val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  val activeNetwork = cm.activeNetworkInfo
  return activeNetwork != null && activeNetwork.isAvailable && activeNetwork.type == ConnectivityManager.TYPE_MOBILE
}

@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isUsing4G(): Boolean {
  val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  val activeNetwork = cm.activeNetworkInfo
  return activeNetwork != null && activeNetwork.isAvailable && activeNetwork.subtype == TelephonyManager.NETWORK_TYPE_LTE
}

fun Context.openNetworkSetting() {
  val intent = Intent(Settings.ACTION_SETTINGS)
  startActivity(intent)
}

@RequiresPermission(value = Manifest.permission.ACCESS_FINE_LOCATION)
fun Context.isLocationEnabled(): Boolean {
  val lm = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
  val gpsEnabled = lm.allProviders.contains(LocationManager.GPS_PROVIDER) && lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
  val networkEnabled = lm.allProviders.contains(LocationManager.NETWORK_PROVIDER) && lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
  return gpsEnabled or networkEnabled
}

fun Context.openLocationSetting() {
  val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
  startActivity(intent)
}

@RequiresPermission(value = Manifest.permission.BLUETOOTH)
fun isBluetoothEnabled(): Boolean {
  val adapter = BluetoothAdapter.getDefaultAdapter()
  return adapter.isEnabled
}

@RequiresPermission(value = Manifest.permission.BLUETOOTH_ADMIN)
fun setBluetoothEnabled(enabled: Boolean) {
  val adapter = BluetoothAdapter.getDefaultAdapter()
  if (enabled) {
    adapter.enable()
  } else {
    adapter.disable()
  }
}

fun getBluetoothDeviceID(recordData: ByteArray): UUID {
  return UUID.nameUUIDFromBytes(recordData)
}

fun Context.isFacebookInstalled(): Boolean {
  val facebook = packageManager.getLaunchIntentForPackage("com.facebook.katana")
  return facebook != null
}

fun Context.isTwitterInstalled(): Boolean {
  val facebook = packageManager.getLaunchIntentForPackage("com.twitter.android")
  return facebook != null
}

fun Context.isInstagramInstalled(): Boolean {
  val facebook = packageManager.getLaunchIntentForPackage("com.instagram.android")
  return facebook != null
}

fun Context.isWechatInstalled(): Boolean {
  val facebook = packageManager.getLaunchIntentForPackage("com.tencent.mm")
  return facebook != null
}