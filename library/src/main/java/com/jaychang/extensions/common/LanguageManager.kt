package com.jaychang.extensions.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.preference.PreferenceManager
import android.text.TextUtils
import java.util.*

object LanguageManager {

  private var defaultLocale: Locale? = null

  fun changeLanguage(context: Context, locale: Locale) {
    saveString(context, getKeyCurrentLang(context), locale.language)
    saveString(context, getKeyCurrentCountry(context), locale.country)

    wrap(context)

    val refresh = getLauncherIntent(context)
    refresh.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
    context.startActivity(refresh)
  }

  fun wrap(context: Context): Context {
    var ctx = context
    val locale = getLocale(context)
    Locale.setDefault(locale)

    val res = ctx.resources
    val config = Configuration(res.configuration)
    if (Build.VERSION.SDK_INT >= 17) {
      config.setLocale(locale)
      ctx = context.createConfigurationContext(config)
    } else {
      config.locale = locale
      res.updateConfiguration(config, res.displayMetrics)
    }
    return ctx
  }

  fun getLocale(context: Context): Locale {
    val lang = getString(context, getKeyCurrentLang(context))
    val country = getString(context, getKeyCurrentCountry(context))
    return if (!TextUtils.isEmpty(lang)) {
      Locale(lang, country)
    } else {
      defaultLocale ?: Locale(Locale.getDefault().language, Locale.getDefault().country)
    }
  }

  fun restore(context: Context, defaultLocale: Locale) {
    this.defaultLocale = defaultLocale
    Locale.setDefault(getLocale(context))
  }

  private fun getKeyCurrentLang(context: Context): String {
    return context.packageName + "_CURRENT_LANG"
  }

  private fun getKeyCurrentCountry(context: Context): String {
    return context.packageName + "_CURRENT_COUNTRY"
  }

  @SuppressLint("ApplySharedPref")
  private fun saveString(context: Context, key: String, value: String) {
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    prefs.edit().putString(key, value).commit()
  }

  private fun getString(context: Context, key: String): String {
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    return prefs.getString(key, "")
  }

  private fun getLauncherIntent(context: Context): Intent {
    return context.packageManager.getLaunchIntentForPackage(context.packageName)
  }

}
