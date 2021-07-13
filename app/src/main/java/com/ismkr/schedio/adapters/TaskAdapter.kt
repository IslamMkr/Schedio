package com.ismkr.schedio.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.ismkr.schedio.R

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private val taskList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.taskText.text = taskList[position]
    }

    override fun getItemCount(): Int = taskList.size

    fun setTasks(tasks: List<String>) {
        taskList.clear()
        taskList.addAll(tasks)
        notifyDataSetChanged()
    }

    fun addTask(subtask: String) {
        taskList.add(subtask)
        notifyDataSetChanged()
    }

    fun removeItemByPosition(position: Int) : Int {
        taskList.removeAt(position)
        notifyDataSetChanged()
        return taskList.size
    }

    fun getTasks() = taskList

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskText: CheckBox = itemView.findViewById(R.id.sub_task_checkbox)
    }

}