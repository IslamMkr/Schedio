package com.ismkr.schedio.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import com.ismkr.schedio.R
import com.ismkr.schedio.models.Task

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private val taskList = mutableListOf<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList[position]

        holder.task.text = task.name
        holder.task.isChecked = task.done
    }

    override fun getItemCount(): Int = taskList.size

    fun setTasks(tasks: List<Task>) {
        taskList.clear()
        taskList.addAll(tasks)
        notifyDataSetChanged()
    }

    fun getTasks() = taskList

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val task: MaterialCheckBox = itemView.findViewById(R.id.task_checkbox)
    }

}