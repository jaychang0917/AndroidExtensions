package com.jaychang.extensions.common

import android.content.Context
import com.jaychang.extensions.internal.ContextProvider

object PreferenceManager {

  fun saveString(key: String, value: String) {
    android.preference.PreferenceManager.getDefaultSharedPreferences(ContextProvider.context)
      .edit()
      .putString(key, value)
      .apply()
  }

  fun getString(key: String): String {
    return android.preference.PreferenceManager.getDefaultSharedPreferences(ContextProvider.context)
      .getString(key, "")
  }

  fun saveBoolean(key: String, value: Boolean) {
    android.preference.PreferenceManager.getDefaultSharedPreferences(ContextProvider.context)
      .edit()
      .putBoolean(key, value)
      .apply()
  }

  fun getBoolean(key: String): Boolean {
    return android.preference.PreferenceManager.getDefaultSharedPreferences(ContextProvider.context)
      .getBoolean(key, false)
  }

  fun getBoolean(key: String, defaultVal: Boolean): Boolean {
    return android.preference.PreferenceManager.getDefaultSharedPreferences(ContextProvider.context)
      .getBoolean(key, defaultVal)
  }

  fun saveInt(key: String, value: Int) {
    android.preference.PreferenceManager.getDefaultSharedPreferences(ContextProvider.context)
      .edit()
      .putInt(key, value)
      .apply()
  }

  fun getInt(key: String): Int {
    return android.preference.PreferenceManager.getDefaultSharedPreferences(ContextProvider.context)
      .getInt(key, -1)
  }

  fun saveLong(key: String, value: Long) {
    android.preference.PreferenceManager.getDefaultSharedPreferences(ContextProvider.context)
      .edit()
      .putLong(key, value)
      .apply()
  }

  fun getLong(key: String): Long {
    return android.preference.PreferenceManager.getDefaultSharedPreferences(ContextProvider.context)
      .getLong(key, -1L)
  }

  fun saveFloat(key: String, value: Float) {
    android.preference.PreferenceManager.getDefaultSharedPreferences(ContextProvider.context)
      .edit()
      .putFloat(key, value)
      .apply()
  }

  fun getFloat(key: String): Float {
    return android.preference.PreferenceManager.getDefaultSharedPreferences(ContextProvider.context)
      .getFloat(key, -1f)
  }

  fun saveStringSet(key: String, value: Set<String>) {
    android.preference.PreferenceManager.getDefaultSharedPreferences(ContextProvider.context)
      .edit()
      .putStringSet(key, value)
      .apply()
  }

  fun getStringSet(key: String): Set<String> {
    return android.preference.PreferenceManager.getDefaultSharedPreferences(ContextProvider.context)
      .getStringSet(key, null)
  }

  fun remove(key: String) {
    android.preference.PreferenceManager.getDefaultSharedPreferences(ContextProvider.context)
      .edit()
      .remove(key)
      .apply()
  }

  fun contains(key: String) {
    android.preference.PreferenceManager.getDefaultSharedPreferences(ContextProvider.context)
      .contains(key)
  }

  fun clear(context: Context) {
    android.preference.PreferenceManager.getDefaultSharedPreferences(ContextProvider.context)
      .edit().clear().apply()
  }
}