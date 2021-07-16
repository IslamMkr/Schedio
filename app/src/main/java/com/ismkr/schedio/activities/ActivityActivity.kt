package com.ismkr.schedio.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ismkr.schedio.R
import com.ismkr.schedio.adapters.TaskWithRemoveOptionAdapter
import com.ismkr.schedio.databinding.ActivityActivityBinding
import com.ismkr.schedio.models.Activity
import com.ismkr.schedio.models.Task
import com.ismkr.schedio.models.User
import com.ismkr.schedio.utils.Constants

class ActivityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActivityBinding
    private lateinit var user: User
    private lateinit var userActivity: Activity

    private lateinit var adapter: TaskWithRemoveOptionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = Gson().fromJson(
            intent.getStringExtra(Constants.USER_CODE),
            User::class.java
        )

        userActivity = Gson().fromJson(
            intent.getStringExtra(Constants.ACTIVITY_CODE),
            Activity::class.java
        )

        updateUI()
        setupRecyclerView()
        handleAddTaskClick()
    }

    private fun handleAddTaskClick() {
        binding.addTaskView.setOnClickListener {
            val addTaskDialog = Dialog(this)
            addTaskDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            addTaskDialog.setCancelable(true)
            addTaskDialog.setContentView(R.layout.dialog_add_task)

            val taskEditText: EditText = addTaskDialog.findViewById(R.id.task)
            val addButton: TextView = addTaskDialog.findViewById(R.id.add_task_button)
            val cancelButton: TextView = addTaskDialog.findViewById(R.id.cancel_button)

            addButton.setOnClickListener {
                val taskText = taskEditText.text.toString().trim()

                if (taskText.isNotBlank()) {
                    adapter.addTask(Task(taskText, false))
                    addTaskDialog.cancel()
                } else {
                    addTaskDialog.cancel()
                }
            }

            cancelButton.setOnClickListener {
                addTaskDialog.cancel()
            }

            addTaskDialog.show()
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.tasksRecyclerView
        adapter = TaskWithRemoveOptionAdapter()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = adapter

        if (userActivity.tasks.isNotEmpty()) {
            adapter.addTasks(userActivity.tasks)
        }
    }

    private fun updateUI() {
        binding.activityName.text = userActivity.name
        binding.description.text = userActivity.description
        binding.time.text = userActivity.time
    }

}