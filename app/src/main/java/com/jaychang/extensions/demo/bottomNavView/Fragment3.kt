package com.jaychang.extensions.demo.bottomNavView

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jaychang.extensions.demo.R

class Fragment3 : Fragment() {

  companion object {
    fun newInstance(): Fragment3 {
      return Fragment3()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    println("onCreate ${javaClass.simpleName}")
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_tab3, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    println("onViewCreated: ${javaClass.simpleName}")
  }
}