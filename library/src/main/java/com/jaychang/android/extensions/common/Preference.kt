package com.jaychang.android.extensions.common

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class Preference(context: Context) {

  private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
  private val keyPrefix = context.packageName

  @Suppress("UNCHECKED_CAST")
  inner class PreferenceDelegate<T: Any> : ReadWriteProperty<Any?, T> {
    private var value: T? = null
    
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
      val key = keyPrefix + property.name
      return when (value) {
        is String, is String?  -> sharedPreferences.getString(key, "") as T
        is Float, is Float? -> sharedPreferences.getFloat(key, 0.0f) as T
        is Int, is Int? -> sharedPreferences.getInt(key, 0) as T
        is Long, is Long? -> sharedPreferences.getLong(key, 0L) as T
        is Boolean, is Boolean? -> sharedPreferences.getBoolean(key, false) as T
        else -> throw IllegalArgumentException()
      }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
      val key = keyPrefix + property.name
      val editor = sharedPreferences.edit()
      when (value) {
        is String -> editor.putString(key, value)
        is Float -> editor.putFloat(key, value)
        is Int -> editor.putInt(key, value)
        is Long -> editor.putLong(key, value)
        is Boolean -> editor.putBoolean(key, value)
        else -> throw IllegalArgumentException()
      }
      this.value = value
      editor.apply()
    }
  }
}