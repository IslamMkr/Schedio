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
import com.ismkr.schedio.adapters.ActivitySectionAdapter
import com.ismkr.schedio.databinding.FragmentTaskBinding
import com.ismkr.schedio.interfaces.OnPopUpMenuItemClicked
import com.ismkr.schedio.models.Action
import com.ismkr.schedio.models.User
import com.ismkr.schedio.utils.DateUtils
import com.ismkr.schedio.utils.Error
import com.ismkr.schedio.viewmodels.FirestoreViewModel
import java.util.*


class TaskFragment :
    Fragment(),
    OnPopUpMenuItemClicked {

    private lateinit var homeActivity: HomeActivity
    private lateinit var firestoreViewModel: FirestoreViewModel
    private lateinit var user: User

    private lateinit var binding: FragmentTaskBinding

    private lateinit var adapter: ActivitySectionAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskBinding.inflate(inflater, container, false)
        homeActivity = (activity as HomeActivity)
        firestoreViewModel = homeActivity.firestoreViewModel()
        user = homeActivity.getUser()

        updateDate()
        setupCalendarListener()
        setupRecyclerView()
        setupAddTask()
        
        return binding.root
    }

    private fun setupAddTask() {
        binding.add.setOnClickListener {
            findNavController().navigate(R.id.action_taskFragment_to_addTaskFragment)
        }
    }

    private fun setupCalendarListener() {
        binding.calendar.setOnClickListener {
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
                        val date = DateUtils.dateAsString(Date(calendar.timeInMillis))

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
        binding.date.text = DateUtils.fromMDYFormatToUIFormat(date, requireContext())
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.sectionsRecyclerView
        adapter = ActivitySectionAdapter(requireContext())
        adapter.setOnPopUpMenuItemListener(this)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = adapter

        observeNewData()
    }

    private fun observeNewData(date: String = DateUtils.todayDate) {
        firestoreViewModel.getSpecificDayTasks(user, date, viewLifecycleOwner)
        firestoreViewModel.userSpecificDayTasksLiveData.observe(
                viewLifecycleOwner, {
                    sections ->
                        adapter.setActivitySectionsList(sections!!)
                        adapter.notifyDataSetChanged()
                }
        )
    }

    override fun onMenuItemClicked(position: Int, action: Action) {
        Error.makeToast(requireContext(), "position : $position\naction : ${action.name}")
    }

}