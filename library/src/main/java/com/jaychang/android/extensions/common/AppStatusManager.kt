package com.jaychang.android.extensions.common

import android.app.Activity
import android.app.Application
import android.app.KeyguardManager
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.PowerManager

object AppStatusManager {

  private var state = ""
  private var isInBackground: Boolean = false
  private var lifecycleCallbacks: Application.ActivityLifecycleCallbacks? = null
  private var componentCallbacks: ComponentCallbacks2? = null

  interface Callback {
    fun onAppEnterBackground()

    fun onAppEnterForeground()
  }

  fun register(app: Application, callback: Callback) {
    lifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
      override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {}

      override fun onActivityStarted(activity: Activity) {}

      override fun onActivityResumed(activity: Activity) {
        if (canInteractive(activity) && isInBackground) {
          callback.onAppEnterForeground()
        }

        isInBackground = false
        state = "Resume"
      }

      override fun onActivityPaused(activity: Activity) {
        if (!canInteractive(activity)) {
          isInBackground = true
          callback.onAppEnterBackground()
        }

        state = "Pause"
      }

      override fun onActivityStopped(activity: Activity) {
        state = "Stop"
      }

      override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

      override fun onActivityDestroyed(activity: Activity) {
        isInBackground = false
      }
    }
    app.registerActivityLifecycleCallbacks(lifecycleCallbacks)

    componentCallbacks = object : ComponentCallbacks2 {
      override fun onTrimMemory(level: Int) {
        if ("Pause" == state || "Stop" == state && !isInBackground) {
          isInBackground = true
          callback.onAppEnterBackground()
        }
      }

      override fun onConfigurationChanged(newConfig: Configuration) {}

      override fun onLowMemory() {}
    }
    app.registerComponentCallbacks(componentCallbacks)
  }

  fun unregister(app: Application) {
    app.unregisterActivityLifecycleCallbacks(lifecycleCallbacks)
    app.unregisterComponentCallbacks(componentCallbacks)
    lifecycleCallbacks = null
    componentCallbacks = null
  }

  private fun canInteractive(context: Context): Boolean {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    val isScreenAwake = if (Build.VERSION.SDK_INT < 20) powerManager.isScreenOn else powerManager.isInteractive

    val keyguardManager = context.applicationContext.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
    val isPhoneLocked = keyguardManager.inKeyguardRestrictedInputMode()

    return isScreenAwake && !isPhoneLocked
  }

}