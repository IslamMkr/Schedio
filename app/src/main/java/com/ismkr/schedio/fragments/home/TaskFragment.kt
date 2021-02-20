package com.ismkr.schedio.fragments.home

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.ismkr.schedio.activities.HomeActivity
import com.ismkr.schedio.databinding.FragmentTaskBinding
import com.ismkr.schedio.models.User
import com.ismkr.schedio.utils.DateUtils


class TaskFragment : Fragment() {

    private lateinit var homeActivity: HomeActivity
    private lateinit var user: User

    private lateinit var binding: FragmentTaskBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskBinding.inflate(inflater, container, false)
        homeActivity = (activity as HomeActivity)
        user = homeActivity.getUser()

        setupDate()
        setupCalendarListener()
        setupPieChart()
        setupRecyclerView()
        
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setupCalendarListener() {
        binding.calendar.setOnClickListener {
            val datePicker = DatePickerDialog(
                    requireContext(),
                    // Listener
                    { _, year, month, dayOfMonth ->
                        updateDate(year, month, dayOfMonth)
                    },
                    DateUtils.getYear(),
                    DateUtils.getMonth(),
                    DateUtils.getDayOfMonth()
            ).show()
        }
    }

    private fun updateDate(year: Int, month: Int, dayOfMonth: Int) {
        val date = "${getString(DateUtils.getMonthResId(month))} $dayOfMonth, $year"
        binding.date.text = date
    }

    private fun setupPieChart() {
        val progress = 1000 / 17
        binding.chart.setProgress(progress.toFloat(), true)
    }

    private fun setupDate() {
        val todayDate = DateUtils.todayDate.split("/")
        val currentMonth = getString(DateUtils.getThisMonthResId())
        val date = "$currentMonth ${todayDate[1]}, ${todayDate[2]}"

        binding.date.text = date
    }

    private fun setupRecyclerView() {

    }

}