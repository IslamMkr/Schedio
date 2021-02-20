package com.ismkr.schedio.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ismkr.schedio.R
import com.ismkr.schedio.activities.HomeActivity
import com.ismkr.schedio.adapters.HomeProjectAdapter
import com.ismkr.schedio.adapters.HomeTaskAdapter
import com.ismkr.schedio.databinding.FragmentHomeBinding
import com.ismkr.schedio.models.User
import com.ismkr.schedio.utils.DateUtils
import com.ismkr.schedio.utils.Error

class HomeFragment : Fragment() {

    private lateinit var homeActivity: HomeActivity
    private lateinit var user: User

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeActivity = (activity as HomeActivity)
        user = homeActivity.getUser()

        setupUserInformation()
        setupDate()

        setupProjectRecyclerView()
        setupTaskRecyclerView()

        return binding.root
    }

    private fun setupUserInformation() {
        val greeting = "${getString(R.string.good_morning)}, ${user.name}"
        binding.greeting.text = greeting
    }

    private fun setupProjectRecyclerView() {
        val projectRecyclerView = binding.projectsRecyclerview
        val adapter = HomeProjectAdapter()

        projectRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        projectRecyclerView.isNestedScrollingEnabled = false
        projectRecyclerView.adapter = adapter

        homeActivity.getUserProjects().observe(
            viewLifecycleOwner,
            {
                projectList ->
                if (projectList != null) {
                    Error.logErrorMessage("Error", "not null")
                    binding.noProjectsTv.visibility = View.GONE
                    projectRecyclerView.visibility = View.VISIBLE
                    adapter.setProjectList(projectList)
                } else {
                    binding.noProjectsTv.visibility = View.VISIBLE
                    projectRecyclerView.visibility = View.GONE
                }
            }
        )
    }

    private fun setupTaskRecyclerView() {
        val tasksRecyclerView = binding.tasksRecyclerview
        val adapter = HomeTaskAdapter()

        tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        tasksRecyclerView.isNestedScrollingEnabled = false
        tasksRecyclerView.adapter = adapter

        homeActivity.getUserTodayTasks().observe(
            viewLifecycleOwner,
            {
                taskList ->
                if (taskList != null) {
                    binding.upcomingTaskTitle.text = taskList[0].name
                    binding.dayTasksNumber.text = taskList.size.toString()
                    binding.noTasksTv.visibility = View.GONE
                    tasksRecyclerView.visibility = View.VISIBLE
                    adapter.setTasks(taskList)
                } else {
                    binding.upcomingTaskTitle.text = "Nothing"
                    binding.dayTasksNumber.text = "0"
                    binding.noTasksTv.visibility = View.VISIBLE
                    tasksRecyclerView.visibility = View.GONE
                }
            }
        )
    }

    private fun setupDate() {
        binding.dayDate.text = DateUtils.todayDate.substring(3, 5)
        binding.monthDate.text = getString(DateUtils.getThisMonthResId())
    }

}