package com.example.todolist.data

import java.util.Date

data class Task(
    val id: Long? = null,
    val name: String,
    val description: String,
    val dueDate: String,
    var isCompleted: Boolean

)
