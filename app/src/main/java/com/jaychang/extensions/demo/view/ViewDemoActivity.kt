package com.jaychang.extensions.demo.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.jaychang.extensions.demo.R
import kotlinx.android.synthetic.main.activity_view.*
import java.util.*

class ViewDemoActivity: AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_view)
  }

  fun change(view: View) {
    textView.text = UUID.randomUUID().toString()
  }
}