package com.example.todolist.database


import android.content.ContentValues.TAG
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.todo.ToDoListDatabase
import com.example.todolist.data.Task


class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val driver: SqlDriver =
        AndroidSqliteDriver(ToDoListDatabase.Schema, context, "ToDoListDatabase.db")
    private val database: ToDoListDatabase = ToDoListDatabase(driver)

    companion object {
        private const val DATABASE_NAME = "ToDoListDatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Task"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_DUE_DATE = "dueDate"
        private const val COLUMN_IS_COMPLETED = "isCompleted"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME TEXT NOT NULL," +
                "$COLUMN_DESCRIPTION TEXT," +
                "$COLUMN_DUE_DATE TEXT," +
                "$COLUMN_IS_COMPLETED INTEGER NOT NULL DEFAULT 0" +
                ")"
        db?.execSQL(createTableQuery)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }


    fun addTask(task: Task) {
        val isCompletedValue = if (task.isCompleted) 1L else 0L
        database.taskQueries.insertTask(
            task.name,
            task.description,
            task.dueDate,
            isCompletedValue
        )
    }

    //
    fun selectAll(): List<Task> {
        val tasks = mutableListOf<Task>()
        val retrievedTasks = database.taskQueries.selectAll().executeAsList()

        for (retrievedTask in retrievedTasks) {
            val task = Task(
                id = retrievedTask.id,
                name = retrievedTask.name,
                description = retrievedTask.description.orEmpty(),
                dueDate = retrievedTask.dueDate.orEmpty(),
                isCompleted = retrievedTask.isCompleted == 1L
            )
            tasks.add(task)
        }
        return tasks
    }


    fun deleteTaskById(taskId: Long) {
        database.taskQueries.deleteById(taskId)
    }

}





