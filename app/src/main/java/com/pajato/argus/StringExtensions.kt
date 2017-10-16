package com.pajato.argus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/** Return a new String where  white-space sequences are replaced with a single space. */
fun String.stripMiddle() = this.replace(Regex("\\s+"), " ")

/** Return a new String where the leading white-space is removed. */
fun String.stripLeft() = this.replace(Regex("^\\s+"), "")

/** Return a new String where the trailing white-space is removed. */
fun String.stripRight() = this.replace(Regex("\\s+$"), "")

/** Return a String where all white-space has been trimmed. */
fun String.strip() = this.stripLeft().stripRight().stripMiddle()

fun  <T : View> ViewGroup.inflate(resId: Int): T {
    @Suppress("UNCHECKED_CAST")
    return LayoutInflater.from(context).inflate(resId, this, false) as T
}

class Video(val title: String, val network: String, val type: String = "")