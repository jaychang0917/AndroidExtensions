package com.jaychang.extensions.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Patterns
import java.io.File
import java.net.URLDecoder
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.regex.Pattern



fun String.orIfEmpty(default: String): String {
  return if (isEmpty()) default else this
}

fun String.base64Encode(): String {
  return Base64.encodeToString(toByteArray(), Base64.NO_WRAP)
}

fun String.base64Decode(): String {
  return String(Base64.decode(this, Base64.NO_WRAP))
}

private fun hash(data: String, algorithm: String): String {
  try {
    val digest = MessageDigest.getInstance(algorithm)
    digest.update(data.toByteArray())

    val messageDigest = digest.digest()

    val hexString = StringBuilder()
    for (aMessageDigest in messageDigest) {
      var h = Integer.toHexString(aMessageDigest.toInt() and 0xFF)
      while (h.length < 2) {
        h = "0" + h
      }
      hexString.append(h)
    }
    return hexString.toString()
  } catch (e: NoSuchAlgorithmException) {
    e.printStackTrace()
  }

  return ""
}

fun String.md5(): String {
  return hash(this, "MD5")
}

fun String.sha1(): String {
  return hash(this, "SHA-1")
}

fun String.sha256(): String {
  return hash(this, "SHA-256")
}

fun String.sha384(): String {
  return hash(this, "SHA-384")
}

fun String.sha512(): String {
  return hash(this, "SHA-512")
}

fun String.spaceToCamelCase(): String {
  val parts = split("_")
  val toProperCase: (String) -> String = { s ->
    s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase()
  }
  return parts.reduce { acc, s -> acc + toProperCase(s) }
}

fun String.hasEmoji(): Boolean {
  val regex = "([\\u20a0-\\u32ff\\ud83c\\udc00-\\ud83d\\udeff\\udbb9\\udce5-\\udbb9\\udcee])"
  val matcher = Pattern.compile(regex).matcher(toString())
  return matcher.find()
}

fun String.hasLetters(): Boolean {
  return contains(Regex("[a-zA-Z]+"))
}

fun String.hasNumbers(): Boolean {
  return contains(Regex("[0-9]+"))
}

fun String.isAlphabetic(): Boolean {
  return matches(Regex("[a-zA-Z]+"))
}

fun String.isDigits(): Boolean {
  return matches(Regex("[0-9]+"))
}

fun String.isDecimal(): Boolean {
  return matches(Regex("[1-9][0-9]*\\.[0-9]+"))
}

fun String.isEmail(): Boolean {
  return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isUrl(): Boolean {
  return Patterns.WEB_URL.matcher(this).matches()
}

fun String.urlEncode(): String {
  return URLEncoder.encode(this, "utf-8")
}

fun String.urlDecoded(): String {
  return URLDecoder.decode(this, "utf-8")
}

fun String.words(): List<String> {
  return trim().split(Regex("\\s+"))
}

fun String.wordsCount(): Int {
  return words().size
}

fun String.count(string: String): Int {
  return if (contains(string)) split(string).size - 1 else 0
}

fun String.truncate(toLength: Int, trailing: String? = "..."): String {
  if (length <= 0) return ""
  return if (length > toLength) {
    substring(0 until toLength) + (trailing ?: "")
  } else {
    this
  }
}

fun String.copyToClipboard(context: Context) {
  val clipboard = context.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
  val clip = ClipData.newPlainText("text", this)
  clipboard.primaryClip = clip
}

fun String.toFile(): File {
  return File(this)
}

fun String.toUri(): Uri {
  return Uri.parse(this)
}

object StringUtils {
  fun random(ofLength: Int): String {
    val base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    val chars = base.toCharArray()
    val random = SecureRandom()
    val sb = StringBuilder(ofLength)
    for (i in 0 until ofLength) {
      sb.append(chars[random.nextInt(base.length)])
    }
    return sb.toString()
  }

  fun getYoutubeThumbnailByUrl(url: String): String? {
    getYoutubeId(url)?.let {
      return getYoutubeThumbnailById(it)
    }
    return null
  }

  fun getYoutubeThumbnailById(id: String): String {
     return "http://img.youtube.com/vi/$id/0.jpg"
  }

  fun getYoutubeId(url: String): String? {
    val pattern = "^(?:(?:https?:\\/\\/)?(?:www\\.)?)?(youtube(?:-nocookie)?\\.com|youtu\\.be)\\/.*?(?:embed|e|v|watch\\?.*?v=)?\\/?([^#&?]*).*$"
    val compiledPattern = Pattern.compile(pattern)
    val matcher = compiledPattern.matcher(url)
    if (matcher.matches()) {
      return matcher.group(2)
    }
    return null
  }
}

