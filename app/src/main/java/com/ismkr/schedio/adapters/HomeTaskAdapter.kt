package com.ismkr.schedio.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ismkr.schedio.R
import com.ismkr.schedio.models.Activity

class HomeTaskAdapter : RecyclerView.Adapter<HomeTaskAdapter.ViewHolder>() {

    private val taskList = mutableListOf<Activity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_home_activity, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = taskList[position]

        holder.name.text = currentItem.name
        holder.time.text = currentItem.time
        holder.description.text = currentItem.description

        if (currentItem.tasks.isNotEmpty()) {
            val adapter = TaskAdapter()

            holder.subTaskRecyclerView.visibility = View.VISIBLE
            holder.subTaskRecyclerView.isNestedScrollingEnabled = false
            holder.subTaskRecyclerView.adapter = adapter

            adapter.setTasks(currentItem.tasks.toList())
        }
    }

    override fun getItemCount(): Int = taskList.size

    fun setTasks(activities: List<Activity>) {
        taskList.clear()
        taskList.addAll(activities)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.task_name)
        val description = itemView.findViewById<TextView>(R.id.task_description)
        val time = itemView.findViewById<TextView>(R.id.task_time)
        val subTaskRecyclerView = itemView.findViewById<RecyclerView>(R.id.task_subtask_recyclerview)
    }
}