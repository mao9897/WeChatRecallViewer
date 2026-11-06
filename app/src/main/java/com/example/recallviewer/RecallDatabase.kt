package com.example.recallviewer

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class RecallDatabase(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "recall_messages.db"
        private const val DB_VERSION = 1
        const val TABLE = "messages"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                sender TEXT NOT NULL,
                content TEXT NOT NULL,
                package_name TEXT NOT NULL,
                timestamp INTEGER NOT NULL,
                is_recalled INTEGER DEFAULT 0
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    fun insert(msg: RecallMessage): Long {
        val db = writableDatabase
        val cv = android.content.ContentValues().apply {
            put("sender", msg.sender)
            put("content", msg.content)
            put("package_name", msg.packageName)
            put("timestamp", msg.timestamp)
            put("is_recalled", if (msg.isRecalled) 1 else 0)
        }
        return db.insert(TABLE, null, cv)
    }

    fun getAll(): List<RecallMessage> {
        val db = readableDatabase
        val cursor = db.query(TABLE, null, null, null, null, null, "timestamp DESC")
        val list = mutableListOf<RecallMessage>()
        while (cursor.moveToNext()) {
            list.add(RecallMessage(
                id = cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                sender = cursor.getString(cursor.getColumnIndexOrThrow("sender")),
                content = cursor.getString(cursor.getColumnIndexOrThrow("content")),
                packageName = cursor.getString(cursor.getColumnIndexOrThrow("package_name")),
                timestamp = cursor.getLong(cursor.getColumnIndexOrThrow("timestamp")),
                isRecalled = cursor.getInt(cursor.getColumnIndexOrThrow("is_recalled")) == 1
            ))
        }
        cursor.close()
        return list
    }

    fun markRecalled(id: Long) {
        val db = writableDatabase
        val cv = android.content.ContentValues().apply { put("is_recalled", 1) }
        db.update(TABLE, cv, "id=?", arrayOf(id.toString()))
    }
}
