package com.ismkr.schedio.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.ismkr.schedio.R
import com.ismkr.schedio.models.Priority
import com.ismkr.schedio.models.Project
import com.ismkr.schedio.utils.DateUtils

class HomeProjectAdapter(
    val context: Context
) : RecyclerView.Adapter<HomeProjectAdapter.ViewHolder>() {

    private val projectList = mutableListOf<Project>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_project, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProject = projectList[position]

        holder.projectName.text = currentProject.name

        holder.projectPriority.text = when (currentProject.priority) {
            Priority.HIGH -> {
                holder.projectPriority.setTextColor(context.resources.getColor(R.color.priority_high))
                "High"
            }
            Priority.MEDIUM -> {
                holder.projectPriority.setTextColor(context.resources.getColor(R.color.priority_medium))
                "Med"
            }
            else -> {
                holder.projectPriority.setTextColor(context.resources.getColor(R.color.priority_low))
                "Low"
            }
        }

        val daysLeft = DateUtils.calculateTimeLeft(currentProject.deadline)

        holder.projectTimeLeft.text = "$daysLeft days"
        holder.projectCompletedActivities.text = "2/8"
        holder.projectProgressBar.progress = currentProject.progress

        val progress = "${currentProject.progress}%"
        holder.projectProgress.text = progress
    }

    override fun getItemCount(): Int = projectList.size

    fun setProjectList(list: List<Project>) {
        projectList.clear()
        projectList.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val projectName = itemView.findViewById<TextView>(R.id.project_name)
        val projectPriority = itemView.findViewById<TextView>(R.id.project_priority)
        val projectTimeLeft = itemView.findViewById<TextView>(R.id.project_time_left)
        val projectCompletedActivities = itemView.findViewById<TextView>(R.id.project_completed_activities)
        val projectProgressBar = itemView.findViewById<LinearProgressIndicator>(R.id.project_progress_bar)
        val projectProgress = itemView.findViewById<TextView>(R.id.project_progress)
        //val projectMembersRecyclerView = itemView.findViewById<RecyclerView>(R.id.project_members_recyclerview)
    }

}