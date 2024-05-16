package com.example.taskapp
import android.provider.BaseColumns

object TaskContract {
    object TaskEntry : BaseColumns {
        const val TABLE_NAME = "tasks"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_START_DATE = "start_date"
        const val COLUMN_NAME_END_DATE = "end_date"
        const val COLUMN_NAME_IS_COMPLETED = "is_completed"
        const val _ID = BaseColumns._ID
    }
}
