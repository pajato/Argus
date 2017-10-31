package com.pajato.argus

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

/** The object that defines the table contents */
object DatabaseEntry : BaseColumns {
    val TABLE_NAME = "video"
    val COLUMN_NAME_TITLE = "title"
    val COLUMN_NAME_NETWORK = "network"
    @Suppress("ObjectPropertyName")
    val _ID = BaseColumns._ID
}

/** A required SQLite implementation that handles . */
class DatabaseReaderHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    companion object {
        private val SQL_CREATE_ENTRIES = "CREATE TABLE " + DatabaseEntry.TABLE_NAME + " (" +
                DatabaseEntry._ID + " INTEGER PRIMARY KEY," + DatabaseEntry.COLUMN_NAME_TITLE +
                " TEXT," + DatabaseEntry.COLUMN_NAME_NETWORK + " TEXT)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DatabaseEntry.TABLE_NAME

        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION: Int = 1
        private var DATABASE_NAME: String = "Argus.db"

        fun setDatabaseName(name: String) {
            DATABASE_NAME = name
        }
    }
}

/** Write the given video into the database. */
fun writeVideo(v: Video, context: Context) {
    // Add the new entry into the database
    val db = DatabaseReaderHelper(context).writableDatabase
    val values = ContentValues()
    values.put(DatabaseEntry.COLUMN_NAME_TITLE, v.title)
    values.put(DatabaseEntry.COLUMN_NAME_NETWORK, v.network)
    db.insert(DatabaseEntry.TABLE_NAME, null, values)
}

/** Returns a list of all the videos in the database. */
fun getVideosFromDb(context: Context): MutableList<Video> {
    // Search the database for all entries.
    val db = DatabaseReaderHelper(context).readableDatabase
    val projection = arrayOf(DatabaseEntry._ID, DatabaseEntry.COLUMN_NAME_TITLE, DatabaseEntry.COLUMN_NAME_NETWORK)
    val sortOrder = DatabaseEntry.COLUMN_NAME_NETWORK + " DESC"
    val cursor: Cursor = db.query(DatabaseEntry.TABLE_NAME, projection, null, null, null, null, sortOrder)
    val items = mutableListOf<Video>()
    // Put each of the database entries into Video objects and our video list.
    while (cursor.moveToNext()) {
        //val itemId: Long = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseEntry._ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_NAME_TITLE))
        val network = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_NAME_NETWORK))
        val video = Video(title, network)
        items.add(video)
    }
    cursor.close()
    return items
}

fun updateVideo(previousTitle: String, v: Video, context: Context) {
    val db = DatabaseReaderHelper(context).writableDatabase
    val values = ContentValues()
    values.put(DatabaseEntry.COLUMN_NAME_TITLE, v.title)
    values.put(DatabaseEntry.COLUMN_NAME_NETWORK, v.network)

    val selection = DatabaseEntry.COLUMN_NAME_TITLE + " LIKE ?"
    val args: Array<String> = arrayOf(previousTitle)

    db.update(DatabaseEntry.TABLE_NAME, values, selection, args)
}

/** Deletes a specific video from the database, searching by video title. */
fun deleteVideo(v: Video, context: Context) {
    // Delete a specific video entry from the database.
    val db = DatabaseReaderHelper(context).writableDatabase
    val selection: String = DatabaseEntry.COLUMN_NAME_TITLE + " LIKE ?"
    val args: Array<String> = arrayOf(v.title)
    db.delete(DatabaseEntry.TABLE_NAME, selection, args)
}

/** Deletes all items in the database. */
fun deleteAll(context: Context) {
    val db = DatabaseReaderHelper(context).writableDatabase
    db.delete(DatabaseEntry.TABLE_NAME, null, null)
}