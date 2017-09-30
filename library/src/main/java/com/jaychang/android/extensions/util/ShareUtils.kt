package com.jaychang.android.extensions.util

import android.content.Context
import android.content.Intent
import android.net.Uri

object ShareUtils {

  val FACEBOOK = "com.facebook.katana"
  val WHATSAPP = "com.whatsapp"
  val LINE = "jp.naver.line.android"
  val WECHAT = "com.tencent.mm"
  val GOOGLE_PLUS = "com.google.android.apps.plus"
  val INSTAGRAM = "com.instagram.android"

  fun share(context: Context, text: String? = null, images: ArrayList<Uri>? = null, videos: ArrayList<Uri>? = null, packageName: String? = null) {
    val intent = Intent()

    val types = mutableListOf<String>()

    text?.let {
      types.add("text/plain")
      intent.putExtra(Intent.EXTRA_TEXT, text)
      intent.action = Intent.ACTION_SEND
    }

    images?.let {
      types.add("image/*")
      intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, images)
      intent.action = Intent.ACTION_SEND_MULTIPLE
    }

    videos?.let {
      types.add("video/*")
      intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, videos)
      intent.action = Intent.ACTION_SEND_MULTIPLE
    }

    intent.type = types.joinToString(",")

    packageName?.let {
      intent.`package` = packageName
    }

    context.startActivity(Intent.createChooser(intent, ""))
  }

}