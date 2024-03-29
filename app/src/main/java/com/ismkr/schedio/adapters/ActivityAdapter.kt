package com.ismkr.schedio.adapters

import android.content.Context
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ismkr.schedio.R
import com.ismkr.schedio.interfaces.OnItemClicked
import com.ismkr.schedio.interfaces.OnPopUpMenuItemClicked
import com.ismkr.schedio.models.Action
import com.ismkr.schedio.models.Activity
import com.ismkr.schedio.utils.Error


class ActivityAdapter(private val context: Context, private val status: String = "") : RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {

    private lateinit var activityList: MutableList<Activity>
    private lateinit var popUpMenuItemClickListener: OnPopUpMenuItemClicked
    private lateinit var itemClickListener: OnItemClicked

    fun setOnPopUpMenuClickListener(listener: OnPopUpMenuItemClicked) {
        popUpMenuItemClickListener = listener
    }

    fun setOnItemClickListener(listener: OnItemClicked) {
        itemClickListener = listener
    }

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
            if (!status.isNullOrBlank()) {
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
            } else {
                holder.status!!.visibility = View.GONE
            }

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

        if (holder.menu != null) {
            // Card menu clicked
            holder.menu.setOnClickListener { view ->
                val popup = PopupMenu(context, view)
                val inflater: MenuInflater = popup.menuInflater
                inflater.inflate(R.menu.activity_popup_menu, popup.menu)

                // Menu item clicked
                popup.setOnMenuItemClickListener {
                    if (it.title.equals("Edit")) {
                        popUpMenuItemClickListener.menuItemClicked(position, activityList[position], Action.EDIT)
                    } else {
                        popUpMenuItemClickListener.menuItemClicked(position, activityList[position], Action.DELETE)
                    }
                    true
                }


                popup.show()
            }
        }

        // Card item clicked
        holder.itemView.setOnClickListener {
            itemClickListener.itemClicked(position, activityList[position])
        }

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
        val menu: ImageView? = itemView.findViewById(R.id.menu_dots)

        val checkBox: AppCompatCheckBox? = if (viewType == 0) {
            itemView.findViewById(R.id.title)
        } else {
            null
        }
    }
}