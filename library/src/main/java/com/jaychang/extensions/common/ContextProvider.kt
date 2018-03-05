package com.jaychang.extensions.common

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.jakewharton.threetenabp.AndroidThreeTen

class ContextProvider : ContentProvider() {

  companion object {
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context
  }

  override fun onCreate(): Boolean {
    Companion.context = context.applicationContext
    AndroidThreeTen.init(context.applicationContext)
    return true
  }

  override fun insert(p0: Uri?, p1: ContentValues?): Uri? = null

  override fun query(p0: Uri?, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?): Cursor? = null

  override fun update(p0: Uri?, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int = 0

  override fun delete(p0: Uri?, p1: String?, p2: Array<out String>?): Int = 0

  override fun getType(p0: Uri?): String? = null

}