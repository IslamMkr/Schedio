package com.ismkr.schedio.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import com.ismkr.schedio.R
import com.ismkr.schedio.models.Task

class TaskWithRemoveOptionAdapter : RecyclerView.Adapter<TaskWithRemoveOptionAdapter.ViewHolder>() {

    private val tasks = mutableListOf<Task>()

    fun addTask(task: Task) {
        tasks.add(task)
        notifyDataSetChanged()
    }

    fun addTasks(addedTasks: List<Task>) {
        this.tasks.addAll(addedTasks)
        notifyDataSetChanged()
    }

    fun tasks() = tasks

    private fun removeTask(position: Int) {
        tasks.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task_with_remove_option, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]

        holder.task.text = task.name
        holder.task.isChecked = task.done

        holder.taskRemoveIcon.setOnClickListener {
            removeTask(position)
        }
    }

    override fun getItemCount() = tasks.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val task: MaterialCheckBox = itemView.findViewById(R.id.task_checkbox)
        val taskRemoveIcon: ImageView = itemView.findViewById(R.id.task_delete)
    }

}