package com.example.todolist.recyclerview

import android.content.res.Resources
import android.graphics.Paint
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.data.Task

class TaskAdapter(private val tasks: MutableList<Task>) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    var selectedTaskPosition: Int = RecyclerView.NO_POSITION


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.task_item_layout, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]

        //implementing the checkboxs
        holder.taskCheckbox.isChecked = task.isCompleted
        holder.taskCheckbox.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                selectedTaskPosition = position
            }
        }

        //setting text and format for task name
        holder.taskNameTextView.text = "${task.name}"
        holder.taskNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        //trying to make sure any long task will display properly as well as setting the task
        holder.taskNameTextView.maxWidth = calculateMaxWidth(holder.taskCheckbox)

        //setting text and format for descripttion
        holder.description.text = "Description: ${task.description}"
        holder.description.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)

        //setting text and format for duedate
        holder.dueDate.text = "Due Date: ${task.dueDate}"
        holder.dueDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)

        updateStrikeThrough(holder, task)

    }

    //trying to make sure that a long task will be displayed properly
    private fun calculateMaxWidth(taskCheckbox: CheckBox): Int {
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val checkboxWidth = taskCheckbox.width
        val padding = taskCheckbox.paddingStart + taskCheckbox.paddingEnd
        return screenWidth - checkboxWidth - padding

    }

    //makes code cleaner
    private fun updateStrikeThrough(holder: ViewHolder, task: Task) {
        if (task.isCompleted) {
            // Apply strike-through effect
            holder.taskNameTextView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.description.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.dueDate.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            // Remove strike-through effect
            holder.taskNameTextView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.description.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.dueDate.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    //must do
    override fun getItemCount(): Int {
        return tasks.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // ViewHolder implementation
        val taskNameTextView: TextView = itemView.findViewById(R.id.taskNameTextView)
        val taskCheckbox: CheckBox = itemView.findViewById(R.id.taskCheckbox)
        val description: TextView = itemView.findViewById(R.id.descriptionTextView)
        val dueDate: TextView = itemView.findViewById(R.id.dueDateTextView)

    }

    //for the database
    interface TaskListener {
        fun onTaskAdded(task: Task)
    }

}


