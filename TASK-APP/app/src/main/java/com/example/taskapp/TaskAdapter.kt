package com.example.taskapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView


class TaskAdapter(context: Context, private val tasks: MutableList<Task>) : ArrayAdapter<Task>(context, 0, tasks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.task_item, parent, false)

        val task = getItem(position)
        val titleTextView = view.findViewById<TextView>(R.id.taskTitle)
        val startDateTextView = view.findViewById<TextView>(R.id.taskStartDate)
        val endDateTextView = view.findViewById<TextView>(R.id.taskEndDate)
        val checkBox = view.findViewById<CheckBox>(R.id.taskCheckBox)

        titleTextView.text = task?.title
        startDateTextView.text = task?.startDate
        endDateTextView.text = task?.endDate
        checkBox.isChecked = if (task?.isCompleted == true) true else false

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            task?.isCompleted = isChecked
            notifyDataSetChanged() // Update the UI
        }

        return view
    }

    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

}
