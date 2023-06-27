package com.example.todolist.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.data.Task
import com.example.todolist.database.DatabaseHelper
import com.example.todolist.databinding.FragmentToDoBinding
import com.example.todolist.recyclerview.TaskAdapter

class ToDoFragment : Fragment(), TaskAdapter.TaskListener {


    private lateinit var binding: FragmentToDoBinding
    private lateinit var taskAdapter: TaskAdapter
    private val tasks: MutableList<Task> = mutableListOf()
    private lateinit var dbHelper: DatabaseHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_to_do, container, false)
        return binding.root

    }

    //when the view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initialize the adapter and recycler view
        taskAdapter = TaskAdapter(tasks)
        binding.recyclerViewTasks.adapter = taskAdapter
        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(requireContext())
        dbHelper = DatabaseHelper(requireContext())

        retrieveTasksFromDatabase()


        //delete button logic
        binding.buttonDeleteTask.setOnClickListener {
            val checkedPositions = mutableListOf<Int>()
            //loop through task
            for (i in tasks.indices) {
                val task = tasks[i]
                if (task.isCompleted) {
                    checkedPositions.add(i)
                }
            }
            if (checkedPositions.isNotEmpty()) {
                for (position in checkedPositions) {
                    deleteTask(position)
                }
                taskAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please make sure a box is checked",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }

        //done button clicker to toggle a strike through each task
        binding.buttonTaskDone.setOnClickListener {
            taskComplete()
        }


        //add task logic pretty straight forward
        binding.buttonAddTask.setOnClickListener {
            val taskName = binding.editTextTask.text.toString().trim()
            val description = binding.descriptionEditText.text.toString().trim()
            val dueDate = binding.dueDateEditText.text.toString().trim()

            if (taskName.isNotEmpty()) {
                val task = Task(
                    name = taskName,
                    description = description,
                    dueDate = dueDate,
                    isCompleted = false
                )
                //add task to the database
                dbHelper.addTask(task)
                //adding task to the list
                tasks.add(task)
                taskAdapter.notifyItemInserted(tasks.size - 1)
                binding.editTextTask.text.clear()
                binding.descriptionEditText.text.clear()
                binding.dueDateEditText.text.clear()

            }

        }

    }

    //marks task as complete
    private fun taskComplete() {
        //checks if valid position making sure an item is clicked
        if (taskAdapter.selectedTaskPosition != RecyclerView.NO_POSITION) {
            //get the checked task
            val selectedTask = tasks[taskAdapter.selectedTaskPosition]
            selectedTask.isCompleted = true

            taskAdapter.notifyDataSetChanged()


        }
    }

    //delete task function
    private fun deleteTask(position: Int) {
        if (position in tasks.indices) {
            val deletedTask = tasks[position]
            dbHelper.deleteTaskById(deletedTask.id!!) // Update the database by deleting the task with its ID
            tasks.removeAt(position)
            taskAdapter.notifyItemRemoved(position)

        }
    }


    //must have to implement the task adapter to the class
    override fun onTaskAdded(task: Task) {

    }

    private fun retrieveTasksFromDatabase() {
        tasks.clear()
        tasks.addAll(dbHelper.selectAll())
        taskAdapter.notifyDataSetChanged()
    }


}










