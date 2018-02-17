package com.jaychang.extensions.demo.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jaychang.extensions.demo.R
import kotlinx.android.synthetic.main.activity_view.*

class ViewDemoActivity: AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_view)
    view.showBadge()
  }
}