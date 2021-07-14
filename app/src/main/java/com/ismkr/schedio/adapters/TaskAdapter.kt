package com.ismkr.schedio.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
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

    fun addTask(task: Task) {
        taskList.add(task)
        notifyDataSetChanged()
    }

    fun removeItemByPosition(position: Int) : Int {
        taskList.removeAt(position)
        notifyDataSetChanged()
        return taskList.size
    }

    fun getTasks() = taskList

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val task: AppCompatCheckBox = itemView.findViewById(R.id.sub_task_checkbox)
    }

}