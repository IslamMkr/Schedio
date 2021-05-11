package com.ismkr.schedio.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.ismkr.schedio.R
import com.ismkr.schedio.models.Project

class HomeProjectAdapter : RecyclerView.Adapter<HomeProjectAdapter.ViewHolder>() {

    private val projectList = mutableListOf<Project>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_project, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProject = projectList[position]

        holder.projectName.text = currentProject.name
        holder.projectDesc.text = currentProject.description
    }

    override fun getItemCount(): Int = projectList.size

    fun setProjectList(list: List<Project>) {
        projectList.clear()
        projectList.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card = itemView.findViewById<MaterialCardView>(R.id.card)
        val projectName = itemView.findViewById<TextView>(R.id.project_name)
        val projectDesc = itemView.findViewById<TextView>(R.id.project_description)
        val projectDayLeft = itemView.findViewById<TextView>(R.id.project_days_left)
    }

}