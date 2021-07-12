package com.ismkr.schedio.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ismkr.schedio.R
import com.ismkr.schedio.interfaces.OnItemClicked

class SubtaskAdapter : RecyclerView.Adapter<SubtaskAdapter.ViewHolder>() {

    private val subtaskList = mutableListOf<String>()
    private lateinit var listener: OnItemClicked

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_subtask, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.subtaskText.text = subtaskList[position]

        holder.removeIcon.setOnClickListener {
            listener.removeItemClick(position)
        }
    }

    override fun getItemCount(): Int = subtaskList.size

    fun setSubtasks(tasks: List<String>) {
        subtaskList.clear()
        subtaskList.addAll(tasks)
        notifyDataSetChanged()
    }

    fun addSubtask(subtask: String) {
        subtaskList.add(subtask)
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: OnItemClicked) {
        this.listener = listener
    }

    fun removeItemByPosition(position: Int) : Int {
        subtaskList.removeAt(position)
        notifyDataSetChanged()
        return subtaskList.size
    }

    fun getSubtasks() = subtaskList

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subtaskText = itemView.findViewById<CheckBox>(R.id.sub_task_checkbox)
        val removeIcon = itemView.findViewById<ImageView>(R.id.ic_remove)
    }

}