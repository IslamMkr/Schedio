package com.ismkr.schedio.fragments.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ismkr.schedio.R
import com.ismkr.schedio.activities.HomeActivity
import com.ismkr.schedio.adapters.TaskTimelineAdapter
import com.ismkr.schedio.databinding.FragmentTaskBinding
import com.ismkr.schedio.models.User
import com.ismkr.schedio.utils.DateUtils
import java.util.*


class TaskFragment : Fragment() {

    private lateinit var homeActivity: HomeActivity
    private lateinit var user: User

    private lateinit var binding: FragmentTaskBinding

    private lateinit var adapter: TaskTimelineAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskBinding.inflate(inflater, container, false)
        homeActivity = (activity as HomeActivity)
        user = homeActivity.getUser()

        updateDate()
        setupCalendarListener()
        setupRecyclerView()
        setupAddTask()
        
        return binding.root
    }

    private fun setupAddTask() {
        binding.addTaskButton.setOnClickListener {
            findNavController().navigate(R.id.action_taskFragment_to_addTaskFragment)
        }
    }

    private fun setupCalendarListener() {
        binding.datePicker.setOnClickListener {
            DatePickerDialog(
                    requireContext(),
                    // Listener
                    { _, year, month, dayOfMonth ->
                        // Setting up the calendar to the picked date
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, month)
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                        // Retrieve the picked date in milliseconds from the calendar
                        val date = DateUtils.formatDate(Date(calendar.timeInMillis))

                        // Update the UI
                        updateDate(date)

                        // Retrieve data of the picked date
                        observeNewData(date)
                    },
                    DateUtils.getYear(),
                    DateUtils.getMonth(),
                    DateUtils.getDayOfMonth()
            ).show()
        }
    }

    private fun updateDate(date: String = DateUtils.todayDate) {
        val todayDate = date.split("/")
        val currentMonth = getString(DateUtils.getThisMonthResId())
        val date = "${todayDate[1]} $currentMonth, ${todayDate[2]}"

        binding.date.text = date
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.tasksRecyclerView
        adapter = TaskTimelineAdapter(requireContext())

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = adapter

        observeNewData()
    }

    private fun observeNewData(date: String = DateUtils.todayDate) {
        homeActivity.getSpecificDayTasks(date).observe(
                viewLifecycleOwner,
                {
                    taskList ->

                    if (taskList == null) {
                        binding.tasksRecyclerView.visibility = View.GONE
                        binding.noTasksTv.visibility = View.VISIBLE
                    } else {
                        binding.tasksRecyclerView.visibility = View.VISIBLE
                        binding.noTasksTv.visibility = View.GONE
                        adapter.setTasksList(taskList)
                        adapter.notifyDataSetChanged()
                    }
                }
        )
    }

}