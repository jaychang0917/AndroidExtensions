package com.jaychang.extensions.util

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.File

fun ImageView.setImage(url: String) {
  Glide.with(context).load(url).into(this)
}

fun ImageView.setImage(uri: Uri) {
  Glide.with(context).load(uri).into(this)
}

fun ImageView.setImage(file: File) {
  Glide.with(context).load(file).into(this)
}