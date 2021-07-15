package com.ismkr.schedio.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ismkr.schedio.R
import com.ismkr.schedio.interfaces.OnItemClicked
import com.ismkr.schedio.interfaces.OnPopUpMenuItemClicked
import com.ismkr.schedio.models.Section

class ActivitySectionAdapter(private val context: Context) :
    RecyclerView.Adapter<ActivitySectionAdapter.ViewHolder>()
{

    private var activitySectionsList = mutableListOf<Section>()
    private lateinit var onPopUpMenuListener: OnPopUpMenuItemClicked
    private lateinit var onItemClickListener: OnItemClicked

    fun setActivitySectionsList(activities: List<Section>) {
        activitySectionsList = activities.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_activity_section, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val section = activitySectionsList[position]

        holder.icon.setImageResource(section.iconId)
        holder.title.text = section.title

        holder.recyclerView.layoutManager = LinearLayoutManager(context)
        holder.recyclerView.isNestedScrollingEnabled = false
        val adapter = ActivityAdapter(context, section.title)
        adapter.setOnPopUpMenuClickListener(onPopUpMenuListener)
        adapter.setOnItemClickListener(onItemClickListener)
        adapter.setActivityList(section.activities)
        holder.recyclerView.adapter = adapter
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getItemCount(): Int = activitySectionsList.size

    fun setOnPopUpMenuItemClickListener(listener: OnPopUpMenuItemClicked) {
        this.onPopUpMenuListener = listener
    }

    fun setOnItemClickListener(listener: OnItemClicked) {
        this.onItemClickListener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.section_title)
        val icon: ImageView = itemView.findViewById(R.id.section_icon)
        val recyclerView : RecyclerView = itemView.findViewById(R.id.recycler_view)
    }

}