package com.example.taskapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.*;
import java.util.*



class MainActivity : AppCompatActivity() {

    private lateinit var editTextTask: EditText
    private lateinit var buttonAddTask: Button
    private lateinit var listViewTasks: ListView
//    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var editTextStartDate: EditText
    private lateinit var editTextEndDate: EditText
    private lateinit var adapter: TaskAdapter
    private lateinit var dbHelper: TaskDbHelper



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize dbHelper
        dbHelper = TaskDbHelper(this)



        editTextTask = findViewById(R.id.editTextTask)
        buttonAddTask = findViewById(R.id.buttonAddTask)
        listViewTasks = findViewById(R.id.listViewTasks)
        editTextStartDate = findViewById(R.id.editTextStartDate)
        editTextEndDate = findViewById(R.id.editTextEndDate)

        // Initialize the adapter
        // Initialize the adapter
        val tasks = mutableListOf<Task>()
        adapter = TaskAdapter(this, tasks)

        val inflater = LayoutInflater.from(this)
        val headerView = inflater.inflate(R.layout.task_header, null)
        listViewTasks.addHeaderView(headerView, null, false)

        listViewTasks.adapter = adapter

        loadTasks()

        buttonAddTask.setOnClickListener {
            val task = editTextTask.text.toString()
            val startDate = editTextStartDate.text.toString()
            val endDate = editTextEndDate.text.toString()

//            if (task.isNotEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty()) {
//                val taskWithDates = "$task (Start: $startDate, End: $endDate)"
//                adapter.add(taskWithDates)
//                editTextTask.text.clear()
//                editTextStartDate.text.clear()
//                editTextEndDate.text.clear()
//            } else {
//                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
//            }
            if (task.isNotEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty()) {
                val newTask = Task(task, startDate, endDate)
//                adapter.add(newTask)
                insertTask(newTask)
                editTextTask.text.clear()
                editTextStartDate.text.clear()
                editTextEndDate.text.clear()
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Optional: Date Picker Dialog for Start and End Dates
        editTextStartDate.setOnClickListener {
            showDatePickerDialog(it, editTextStartDate)
        }

        editTextEndDate.setOnClickListener {
            showDatePickerDialog(it, editTextEndDate)
        }
    }

    private fun showDatePickerDialog(view: View, editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
            val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
            editText.setText(selectedDate)
        }, year, month, day).show()
    }

    private fun insertTask(task: Task) {
        val values = ContentValues().apply {
            put(TaskContract.TaskEntry.COLUMN_NAME_TITLE, task.title)
            put(TaskContract.TaskEntry.COLUMN_NAME_START_DATE, task.startDate)
            put(TaskContract.TaskEntry.COLUMN_NAME_END_DATE, task.endDate)
            put(TaskContract.TaskEntry.COLUMN_NAME_IS_COMPLETED, task.isCompleted)
        }

        val db = dbHelper.writableDatabase
        db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values)
    }

    private fun loadTasks() {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            TaskContract.TaskEntry._ID,
            TaskContract.TaskEntry.COLUMN_NAME_TITLE,
            TaskContract.TaskEntry.COLUMN_NAME_START_DATE,
            TaskContract.TaskEntry.COLUMN_NAME_END_DATE,
            TaskContract.TaskEntry.COLUMN_NAME_IS_COMPLETED
        )

        val cursor = db.query(
            TaskContract.TaskEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val tasks = mutableListOf<Task>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry._ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_TITLE))
            val startDate = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_START_DATE))
            val endDate = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_END_DATE))
            val isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_IS_COMPLETED)) == 1

            tasks.add(Task(title, startDate, endDate, isCompleted))
        }
        cursor.close()

        // Update your adapter with the loaded tasks
        adapter.updateTasks(tasks)
    }

}
