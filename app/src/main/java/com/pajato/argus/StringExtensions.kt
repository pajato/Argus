package com.pajato.argus

/** Return a new String where  white-space sequences are replaced with a single space. */
fun String.stripMiddle() = this.replace(Regex("\\s+"), " ")

/** Return a new String where the leading white-space is removed. */
fun String.stripLeft() = this.replace(Regex("^\\s+"), "")

/** Return a new String where the trailing white-space is removed. */
fun String.stripRight() = this.replace(Regex("\\s+$"), "")

/** Return a String where all white-space has been trimmed. */
fun String.strip() = this.stripLeft().stripRight().stripMiddle()

class Video(val title: String, val network: String, val dateWatched: String, val type: String = "") {
    constructor(title: String, network: String) : this(title, network, "", "")
}