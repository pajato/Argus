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
    val COLUMN_NAME_DATE_WATCHED = "dateWatched"
    val COLUMN_NAME_LOCATION_WATCHED = "locationWatched"
    @Suppress("ObjectPropertyName")
    val _ID = BaseColumns._ID
}

/** A required SQLite implementation that handles . */
class DatabaseReaderHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    /** Add additional database information for each database version. */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL(DATABASE_ALTER_FOR_V2)
        }
        if (oldVersion < 3) {
            db.execSQL(DATABASE_ALTER_FOR_V3)
        }
    }

    companion object {
        private val SQL_CREATE_ENTRIES = "CREATE TABLE " + DatabaseEntry.TABLE_NAME + " (" +
                DatabaseEntry._ID + " INTEGER PRIMARY KEY, " + DatabaseEntry.COLUMN_NAME_TITLE +
                " TEXT," + DatabaseEntry.COLUMN_NAME_NETWORK + " TEXT, " +
                DatabaseEntry.COLUMN_NAME_DATE_WATCHED + " TEXT, " +
                DatabaseEntry.COLUMN_NAME_LOCATION_WATCHED + " TEXT)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DatabaseEntry.TABLE_NAME

        private val DATABASE_ALTER_FOR_V2 = "ALTER TABLE ${DatabaseEntry.TABLE_NAME} " +
                "ADD COLUMN ${DatabaseEntry.COLUMN_NAME_DATE_WATCHED} TEXT NOT NULL DEFAULT '';"
        private val DATABASE_ALTER_FOR_V3 = "ALTER TABLE ${DatabaseEntry.TABLE_NAME} " +
                "ADD COLUMN ${DatabaseEntry.COLUMN_NAME_LOCATION_WATCHED} TEXT NOT NULL DEFAULT '';"

        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION: Int = 3
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
    values.put(DatabaseEntry.COLUMN_NAME_DATE_WATCHED, v.dateWatched)
    values.put(DatabaseEntry.COLUMN_NAME_LOCATION_WATCHED, v.locationWatched)
    db.insert(DatabaseEntry.TABLE_NAME, null, values)
}

/** Returns a list of all the videos in the database. */
fun getVideosFromDb(context: Context): MutableList<Video> {
    // Search the database for all entries.
    val db = DatabaseReaderHelper(context).writableDatabase
    val projection = arrayOf(DatabaseEntry._ID, DatabaseEntry.COLUMN_NAME_TITLE,
            DatabaseEntry.COLUMN_NAME_NETWORK, DatabaseEntry.COLUMN_NAME_DATE_WATCHED,
            DatabaseEntry.COLUMN_NAME_LOCATION_WATCHED)
    val sortOrder = DatabaseEntry.COLUMN_NAME_NETWORK + " DESC"
    val cursor: Cursor = db.query(DatabaseEntry.TABLE_NAME, projection, null, null, null, null, sortOrder)
    val items = mutableListOf<Video>()
    // Put each of the database entries into Video objects and our video list.
    while (cursor.moveToNext()) {
        //val itemId: Long = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseEntry._ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_NAME_TITLE))
        val network = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_NAME_NETWORK))
        val dateWatched = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_NAME_DATE_WATCHED))
        val locationWatched = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_NAME_LOCATION_WATCHED))
        val video = Video(title, network, dateWatched, "", locationWatched)
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
    values.put(DatabaseEntry.COLUMN_NAME_DATE_WATCHED, v.dateWatched)
    values.put(DatabaseEntry.COLUMN_NAME_LOCATION_WATCHED, v.locationWatched)

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