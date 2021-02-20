package com.ismkr.schedio.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ismkr.schedio.R

class HomeTaskSubtaskAdapter : RecyclerView.Adapter<HomeTaskSubtaskAdapter.ViewHolder>() {

    private val subtaskList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTaskSubtaskAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_subtask_home_task_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.subtaskText.text = subtaskList[position]
    }

    override fun getItemCount(): Int = subtaskList.size

    fun setSubtasks(tasks: List<String>) {
        subtaskList.clear()
        subtaskList.addAll(tasks)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subtaskText = itemView.findViewById<TextView>(R.id.subtask_text)
    }

}