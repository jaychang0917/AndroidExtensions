package com.jaychang.extensions.core

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.jaychang.extensions.ui.BadgeConfig
import com.jaychang.extensions.ui.BottomNavigationViewEx

class BottomNavigationViewController(private val view: BottomNavigationViewEx,
                                     private val fragmentManager: FragmentManager,
                                     private val fragmentProviders: List<(Int) -> Fragment>,
                                     @IdRes private val containerId: Int,
                                     private val retainInstance: Boolean = false,
                                     private val badgeConfig: BadgeConfig = BadgeConfig()) {

  init {
    view.setOnNavigationItemSelectedListener {
      val pos = view.getMenuItemPosition(it)
      if (pos != view.currentItem) {
        internalChangTab(pos)
      }
      true
    }
    view.badgeConfig = badgeConfig
  }

  private fun internalChangTab(pos: Int) {
    val tag = "${javaClass.name}:fragment:$pos"
    var fragment = fragmentManager.findFragmentByTag(tag)
    val transaction = fragmentManager.beginTransaction()

    if (fragment == null) {
      fragment = fragmentProviders[pos].invoke(pos)
      transaction.add(containerId, fragment, tag)
    } else {
      if (retainInstance) {
        transaction.attach(fragment)
      } else {
        transaction.remove(fragment)
        fragment = fragmentProviders[pos].invoke(pos)
        transaction.add(containerId, fragment, tag)
      }
    }

    fragmentManager.primaryNavigationFragment?.let {
      transaction.detach(it)
    }

    transaction.setPrimaryNavigationFragment(fragment)
    transaction.commitNow()
  }

  fun changeTab(position: Int) {
    internalChangTab(position)
  }

  fun showBadge(position: Int) {
    view.showBadge(position)
  }

  fun hideBadge(position: Int) {
    view.hideBadge(position)
  }
}