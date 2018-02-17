package com.jaychang.extensions.demo.bottomNavView

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.jaychang.extensions.core.BottomNavigationViewController
import com.jaychang.extensions.demo.R
import com.jaychang.extensions.ui.BadgeConfig
import kotlinx.android.synthetic.main.activity_bnv.*

class BottomNavigationViewDemoActivity: AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_bnv)

    val fragmentProviders = listOf<(Int) -> Fragment>(
      { Fragment1.newInstance() },
      { Fragment2.newInstance() },
      { Fragment3.newInstance() },
      { Fragment4.newInstance() }
    )

    val bottomNavViewController = BottomNavigationViewController(
      view = bottomNavView,
      fragmentManager = supportFragmentManager,
      fragmentProviders = fragmentProviders,
      containerId = R.id.fragmentsContainer,
      retainInstance = false,
      badgeConfig = BadgeConfig(color = R.color.colorAccent)
    )

    bottomNavViewController.changeTab(0)
    bottomNavViewController.showBadge(0)
  }
}