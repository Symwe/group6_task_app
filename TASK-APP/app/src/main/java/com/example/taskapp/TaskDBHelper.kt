package com.example.taskapp
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TaskDatabase.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + " ("
                + TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TaskContract.TaskEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL, "
                + TaskContract.TaskEntry.COLUMN_NAME_START_DATE + " TEXT, "
                + TaskContract.TaskEntry.COLUMN_NAME_END_DATE + " TEXT, "
                + TaskContract.TaskEntry.COLUMN_NAME_IS_COMPLETED + " INTEGER NOT NULL DEFAULT 0)")
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME)
        onCreate(db)
    }
}