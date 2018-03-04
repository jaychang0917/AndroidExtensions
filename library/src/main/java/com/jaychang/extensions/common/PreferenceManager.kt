package com.jaychang.extensions.common

import android.content.SharedPreferences
import com.jaychang.extensions.internal.ContextProvider

object PreferenceManager {

  fun saveString(key: String, value: String, isAsync: Boolean = true) {
    perform(isAsync) {
      getEditor().putString(key, value)
    }
  }

  fun getString(key: String): String {
    return getPreference().getString(key, "")
  }

  fun saveBoolean(key: String, value: Boolean, isAsync: Boolean = true) {
    perform(isAsync) {
      getEditor().putBoolean(key, value)
    }
  }

  fun getBoolean(key: String): Boolean {
    return getPreference().getBoolean(key, false)
  }

  fun getBoolean(key: String, defaultVal: Boolean): Boolean {
    return getPreference().getBoolean(key, defaultVal)
  }

  fun saveInt(key: String, value: Int, isAsync: Boolean = true) {
    perform(isAsync) {
      getEditor().putInt(key, value)
    }
  }

  fun getInt(key: String): Int {
    return getPreference().getInt(key, -1)
  }

  fun saveLong(key: String, value: Long, isAsync: Boolean = true) {
    perform(isAsync) {
      getEditor().putLong(key, value)
    }
  }

  fun getLong(key: String): Long {
    return getPreference().getLong(key, -1L)
  }

  fun saveFloat(key: String, value: Float, isAsync: Boolean = true) {
    perform(isAsync) {
      getEditor().putFloat(key, value)
    }
  }

  fun getFloat(key: String): Float {
    return getPreference().getFloat(key, -1f)
  }

  fun saveStringSet(key: String, value: Set<String>, isAsync: Boolean = true) {
    perform(isAsync) {
      getEditor().putStringSet(key, value)
    }
  }

  fun getStringSet(key: String): Set<String> {
    return getPreference().getStringSet(key, null)
  }

  fun remove(key: String, isAsync: Boolean = true) {
    perform(isAsync) {
      getEditor().remove(key)
    }
  }

  fun contains(key: String): Boolean {
    return getPreference().contains(key)
  }

  fun clear(isAsync: Boolean = true) {
    perform(isAsync) {
      getEditor().clear()
    }
  }

  private fun getPreference(): SharedPreferences {
    return android.preference.PreferenceManager.getDefaultSharedPreferences(ContextProvider.context)
  }

  private fun getEditor(): SharedPreferences.Editor {
    return getPreference().edit()
  }

  private fun perform(isAsync: Boolean, action: () -> SharedPreferences.Editor) {
    val editor = action()
    if (isAsync) {
      editor.apply()
    } else {
      editor.commit()
    }
  }
}