package com.ismkr.schedio.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ismkr.schedio.R
import com.ismkr.schedio.activities.HomeActivity
import com.ismkr.schedio.adapters.ActivityAdapter
import com.ismkr.schedio.adapters.HomeProjectAdapter
import com.ismkr.schedio.databinding.FragmentHomeBinding
import com.ismkr.schedio.models.User
import com.ismkr.schedio.utils.DateUtils
import com.ismkr.schedio.viewmodels.FirestoreViewModel

class HomeFragment : Fragment() {

    private lateinit var homeActivity: HomeActivity
    private lateinit var firestoreViewModel: FirestoreViewModel
    private lateinit var user: User

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeActivity = (activity as HomeActivity)
        firestoreViewModel = homeActivity.firestoreViewModel()
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
        val adapter = context?.let { HomeProjectAdapter(it) }

        projectRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        projectRecyclerView.isNestedScrollingEnabled = true
        projectRecyclerView.adapter = adapter

        firestoreViewModel.getUserProjects(user)
        firestoreViewModel.userProjectsLiveData.observe(
            viewLifecycleOwner,
            {
                projectList ->
                if (projectList != null) {
                    binding.noProjectsTv.visibility = View.GONE
                    projectRecyclerView.visibility = View.VISIBLE
                    adapter?.setProjectList(projectList)
                } else {
                    binding.noProjectsTv.visibility = View.VISIBLE
                    projectRecyclerView.visibility = View.GONE
                }
            }
        )
    }

    private fun setupTaskRecyclerView() {
        val tasksRecyclerView = binding.tasksRecyclerview
        val adapter = ActivityAdapter(requireContext())

        tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        tasksRecyclerView.isNestedScrollingEnabled = false
        tasksRecyclerView.adapter = adapter

        firestoreViewModel.getUserTodayTasks(user)
        firestoreViewModel.userTodayTasksLiveData.observe(
            viewLifecycleOwner,
            {
                taskList ->
                if (taskList != null) {
                    val percentage = (1 * 100 / taskList.size)
                    binding.percentage.text = "$percentage%"
                    binding.progressIndicator.progress = percentage
                    binding.dayTasks.text = "1 Task / ${taskList.size} Task"
                    binding.noTasksTv.visibility = View.GONE
                    tasksRecyclerView.visibility = View.VISIBLE
                    adapter.setActivityList(taskList)
                } else {
                    binding.percentage.text = "100%"
                    binding.progressIndicator.progress = 100
                    binding.dayTasks.text = "0 Task / 0 Task"
                    binding.noTasksTv.visibility = View.VISIBLE
                    tasksRecyclerView.visibility = View.GONE
                }
            }
        )
    }

    private fun setupDate() {
        binding.dateTv.text = DateUtils.fromMDYFormatToUIFormat(DateUtils.todayDate, requireContext())
    }

}