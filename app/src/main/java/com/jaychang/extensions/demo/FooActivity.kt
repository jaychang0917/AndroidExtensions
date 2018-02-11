package com.jaychang.extensions.demo

import android.app.Activity
import android.os.Bundle

class FooActivity: Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_foo)
  }
}