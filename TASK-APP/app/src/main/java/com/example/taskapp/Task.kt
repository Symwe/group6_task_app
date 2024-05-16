package com.example.taskapp

data class Task(val title: String, val startDate: String, val endDate: String, var isCompleted: Boolean = false)
