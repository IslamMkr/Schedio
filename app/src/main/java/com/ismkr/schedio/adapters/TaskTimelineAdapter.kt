package com.ismkr.schedio.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import com.ismkr.schedio.R
import com.ismkr.schedio.models.Task
import com.ismkr.schedio.utils.DateUtils
import com.ismkr.schedio.utils.Error

class TaskTimelineAdapter(val context: Context)
    : RecyclerView.Adapter<TaskTimelineAdapter.ViewHolder>() {

    private var tasksList = mutableListOf<Task>()

    fun setTasksList(tasks: List<Task>) {
        tasksList = tasks.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = if (viewType == 1) {
            LayoutInflater.from(parent.context).inflate(R.layout.item_timeline_on, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.item_timeline_off, parent, false)
        }

        Error.logErrorMessage("E", "viewType = $viewType")

        return ViewHolder(itemView, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasksList[position]

        holder.title.text = task.name
        holder.desc.text = task.description
        holder.hour.text = task.time

        val timeDiff = DateUtils.getTimeDifference(task.time, task.duration).split(":")

        holder.timeDif.text = if (timeDiff[0].toInt() == 0) {
            val diff = "${timeDiff[1]} minutes"
            diff
        } else {
            val diff = "${timeDiff[0]} hours and ${timeDiff[1]} minutes"
            diff
        }

        when (DateUtils.compareTime(task.time, task.duration)) {
            1 -> holder.timeDesc.text = "passed"
            0 -> holder.timeDesc.text = "remaining"
            else -> holder.timeDesc.text = "ago"
        }

        holder.timeLineView.initLine(0)
    }

    override fun getItemViewType(position: Int): Int {
        // See if current time is between the task start time and task end time
        // (taskStart <= currentTime < taskStart + duration)
        return when (
            DateUtils.compareTime(
                tasksList[position].time,
                tasksList[position].duration
            )
        ) {
            0, -1 -> 0
            else -> 1
        }
    }

    override fun getItemCount(): Int = tasksList.size

    class ViewHolder(itemView: View, val viewType: Int) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val desc: TextView = itemView.findViewById(R.id.desc)
        val hour: TextView = itemView.findViewById(R.id.hour)
        val timeDif: TextView = itemView.findViewById(R.id.time_dif)
        val timeDesc: TextView = itemView.findViewById(R.id.time_desc)
        val timeLineView: TimelineView = itemView.findViewById(R.id.timeLine)
    }

}