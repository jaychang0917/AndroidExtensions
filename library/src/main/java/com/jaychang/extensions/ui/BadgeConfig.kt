package com.jaychang.extensions.ui

import android.support.annotation.ColorRes

data class BadgeConfig(val radius: Int = 8, val inset: Int = 0, @ColorRes val color: Int = android.R.color.holo_red_light)