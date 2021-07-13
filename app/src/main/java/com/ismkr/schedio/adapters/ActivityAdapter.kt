package com.ismkr.schedio.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ismkr.schedio.R
import com.ismkr.schedio.models.Activity

class ActivityAdapter(private val context: Context, private val status: String) : RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {

    private lateinit var activityList: MutableList<Activity>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = when (viewType) {
            0 -> LayoutInflater.from(parent.context).inflate(R.layout.item_activity_without_task, parent, false)
            else -> LayoutInflater.from(parent.context).inflate(R.layout.item_activity_with_task, parent, false)
        }

        return ViewHolder(itemView, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = activityList[position]

        if (activity.tasks.isNullOrEmpty()) {
            holder.checkBox!!.text = activity.name
        } else {
            holder.status!!.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    when (status) {
                        "In Progress" -> R.drawable.circle_activity_status_inprogress
                        "Done" -> R.drawable.circle_activity_status_done
                        else -> R.drawable.circle_activity_status_todo
                    }
                )
            )

            if (holder.title != null) holder.title.text = activity.name
            if (holder.description != null) {
                if (activity.description.isNullOrBlank()) {
                    holder.description.visibility = View.GONE
                } else {
                    holder.description.text = activity.description
                }
            }

            if (holder.recyclerView != null) {
                holder.recyclerView.layoutManager = LinearLayoutManager(context)
                holder.recyclerView.isNestedScrollingEnabled = false
                val adapter = TaskAdapter()
                adapter.setTasks(activity.tasks)
                holder.recyclerView.adapter = adapter
            }
        }

        if (holder.date != null) holder.date.text = activity.date
        if (holder.time != null) holder.time.text = activity.time
    }

    override fun getItemCount(): Int {
        return activityList.size
    }

    override fun getItemViewType(position: Int): Int {
        val activity = activityList[position]

        return when (activity.tasks.isNullOrEmpty()) {
            true -> 0 // Without tasks
            else -> 1 // With tasks
        }
     }

    fun setActivityList(activities: List<Activity>) {
        activityList = activities.toMutableList()
    }

    inner class ViewHolder(itemView: View, val viewType: Int) : RecyclerView.ViewHolder(itemView) {
        val status: ImageView? = itemView.findViewById(R.id.status_drawable)
        val title: TextView? = itemView.findViewById(R.id.title)
        val description: TextView? = itemView.findViewById(R.id.description)
        val time: TextView? = itemView.findViewById(R.id.time)
        val date: TextView? = itemView.findViewById(R.id.date)
        val recyclerView: RecyclerView? = itemView.findViewById(R.id.tasks_recycler_view)

        val checkBox: AppCompatCheckBox? = if (viewType == 0) {
            itemView.findViewById(R.id.title)
        } else {
            null
        }
    }
}