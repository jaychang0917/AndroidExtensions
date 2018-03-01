package com.jaychang.extensions.common

import android.content.Context

object PreferenceManager {

  fun saveString(context: Context, key: String, value: String) {
    android.preference.PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .edit()
      .putString(key, value)
      .apply()
  }

  fun getString(context: Context, key: String): String {
    return android.preference.PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .getString(key, "")
  }

  fun saveBoolean(context: Context, key: String, value: Boolean) {
    android.preference.PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .edit()
      .putBoolean(key, value)
      .apply()
  }

  fun getBoolean(context: Context, key: String): Boolean {
    return android.preference.PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .getBoolean(key, false)
  }

  fun getBoolean(context: Context, key: String, defaultVal: Boolean): Boolean {
    return android.preference.PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .getBoolean(key, defaultVal)
  }

  fun saveInt(context: Context, key: String, value: Int) {
    android.preference.PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .edit()
      .putInt(key, value)
      .apply()
  }

  fun getInt(context: Context, key: String): Int {
    return android.preference.PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .getInt(key, -1)
  }

  fun saveLong(context: Context, key: String, value: Long) {
    android.preference.PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .edit()
      .putLong(key, value)
      .apply()
  }

  fun getLong(context: Context, key: String): Long {
    return android.preference.PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .getLong(key, -1L)
  }

  fun saveFloat(context: Context, key: String, value: Float) {
    android.preference.PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .edit()
      .putFloat(key, value)
      .apply()
  }

  fun getFloat(context: Context, key: String): Float {
    return android.preference.PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .getFloat(key, -1f)
  }

  fun saveStringSet(context: Context, key: String, value: Set<String>) {
    android.preference.PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .edit()
      .putStringSet(key, value)
      .apply()
  }

  fun getStringSet(context: Context, key: String): Set<String> {
    return android.preference.PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .getStringSet(key, null)
  }

  fun remove(context: Context, key: String) {
    android.preference.PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .edit()
      .remove(key)
      .apply()
  }

  fun contains(context: Context, key: String) {
    android.preference.PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .contains(key)
  }

  fun clear(context: Context) {
    android.preference.PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
      .edit().clear().apply()
  }
}