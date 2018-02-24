package com.jaychang.extensions.demo.common

import android.app.Application
import com.jaychang.extensions.common.AppStatusManager

class App: Application() {

  override fun onCreate() {
    super.onCreate()
    AppStatusManager.register(this, object: AppStatusManager.Callback {
      override fun onAppEnterBackground() {
      }

      override fun onAppEnterForeground() {
      }
    })
  }
}