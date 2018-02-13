package com.pajato.argus.database

import android.content.ContentValues
import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.pajato.argus.Video

object FirebaseManager {
    // TODO: Update this when the user logs in.
    var TMP_USER_STRING = "users"

    fun init(context: Context) {
        FirebaseApp.initializeApp(context)
    }

    fun <T : Video> writeVideo(v: T) {
        val database = FirebaseDatabase.getInstance().reference
        val updates = HashMap<String, Map<String, Any>>()
        updates.put("/users/$TMP_USER_STRING/", v.toMap())
        database.updateChildren(updates as Map<String, Any>)
    }

    fun <T : Video> storeAllVideos(videos: MutableList<T>, context: Context) {
        val database = FirebaseDatabase.getInstance().reference
        val videoMap = videos.associateBy({ "" + it.id }, { it })
        val updates = HashMap<String, Map<String, Any>>()
        updates.put("/users/$TMP_USER_STRING/", videoMap)
        database.updateChildren(updates as Map<String, Any>)
    }
}

fun Video.toMap(): Map<String, Any> {
    return mapOf(DatabaseEntry.COLUMN_NAME_TITLE to this.title,
            DatabaseEntry.COLUMN_NAME_NETWORK to this.network,
            DatabaseEntry.COLUMN_NAME_TYPE to this.type,
            DatabaseEntry._ID to this.id,
            DatabaseEntry.COLUMN_NAME_DATE_WATCHED to this.dateWatched,
            DatabaseEntry.COLUMN_NAME_LOCATION_WATCHED to this.locationWatched
    )
}

fun ContentValues.getValueMap(): Map<String, Any> {
    val map = mutableMapOf<String, Any>()
    val keys = this.keySet()
    keys.forEach { element ->
        map.put(element, this.get(element))
    }
    return map.toMap()
}