package com.jaychang.extensions.demo.util

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jaychang.extensions.demo.R
import com.jaychang.extensions.util.DurationFormatUtils
import kotlinx.android.synthetic.main.activity_util.*

class UtilDemoActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_util)

    textView.text = DurationFormatUtils.formatHMS(2000L)
  }

}