package com.ismkr.schedio.fragments.home

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ismkr.schedio.R
import com.ismkr.schedio.activities.HomeActivity
import com.ismkr.schedio.adapters.SubtaskAdapter
import com.ismkr.schedio.databinding.FragmentAddTaskBinding
import com.ismkr.schedio.interfaces.OnItemClicked
import com.ismkr.schedio.models.Task
import com.ismkr.schedio.utils.DateUtils
import com.ismkr.schedio.utils.Error
import java.util.*

class AddTaskFragment : Fragment(), OnItemClicked {

    private lateinit var binding: FragmentAddTaskBinding
    private lateinit var activity: HomeActivity
    private lateinit var subtaskAdapter: SubtaskAdapter

    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        activity = getActivity() as HomeActivity

        binding.arrowBack.setOnClickListener {
            findNavController().navigateUp()
        }

        setupDate()
        setupDatePicker()

        setupTime()
        setupTimePicker()

        setupSubtasksRecyclerView()

        setupLinkSpinner()
        setupDurationSpinner()

        addTaskClick()

        return binding.root
    }

    private fun addTaskClick() {
        binding.addTaskButton.setOnClickListener {
            val taskName = binding.taskNameEt.text.toString().trim()

            if (taskName.isNotBlank()) {
                val date = DateUtils.fromUIFormatToMDYFormat(requireContext(), binding.date.text.toString())
                val time = binding.time.text.toString()
                val desc = binding.taskDescEt.text.toString().trim()
                val link = binding.taskLinkSpinner.selectedItem as String
                val duration = DateUtils.fromLetterToHMFormat(requireContext(), binding.taskDurationSpinner.selectedItem as String)

                val task = Task(taskName, date, time, desc, link)
                task.creationDate = DateUtils.todayDate
                task.subTasks.addAll(subtaskAdapter.getSubtasks())
                task.duration = duration

                activity.addTask(task)
            } else {
                Error.makeToast(requireContext(), "Please, enter task name!")
            }
        }
    }

    private fun setupLinkSpinner() {
        val spinner = binding.taskLinkSpinner

        val links = mutableListOf<String>()
        links.add(resources.getString(R.string.none))

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_text,
            links
        )

        adapter.setDropDownViewResource(R.layout.spinner_text)
        spinner.adapter = adapter

        activity.getUserProjects().observe(
            viewLifecycleOwner,
            { projectList ->
                if (projectList != null) {
                    links.addAll(
                        projectList.map { project -> project.name }
                    )
                    adapter.notifyDataSetChanged()
                }
            }
        )
    }

    private fun setupDurationSpinner() {
        val durationSpinner = binding.taskDurationSpinner

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.durations,
            R.layout.spinner_text
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_text)
            durationSpinner.adapter = adapter
        }
    }

    private fun setupSubtasksRecyclerView() {
        val subtaskRecyclerView = binding.subtasksRecyclerview
        subtaskAdapter = SubtaskAdapter()
        subtaskAdapter.setOnClickListener(this)

        subtaskRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        subtaskRecyclerView.isNestedScrollingEnabled = false
        subtaskRecyclerView.adapter = subtaskAdapter

        binding.addSubtaskButton.setOnClickListener {
            showAddSubtaskDialog(subtaskAdapter)
        }
    }

    private fun showAddSubtaskDialog(adapter: SubtaskAdapter) {
        val addSubtaskDialog = Dialog(requireContext())
        addSubtaskDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        addSubtaskDialog.setCancelable(true)
        addSubtaskDialog.setContentView(R.layout.dialog_add_subtask)

        val subtaskEditText = addSubtaskDialog.findViewById<EditText>(R.id.sub_task)
        val addButton = addSubtaskDialog.findViewById<Button>(R.id.add_subtask_button)
        val cancelButton = addSubtaskDialog.findViewById<Button>(R.id.cancel_button)

        addButton.setOnClickListener {
            val subtaskText = subtaskEditText.text.toString().trim()

            if (subtaskText.isNotBlank()) {
                binding.subtasksRecyclerview.visibility = View.VISIBLE
                binding.emptySubtask.visibility = View.GONE
                adapter.addSubtask(subtaskText)
                addSubtaskDialog.cancel()
            } else {
                addSubtaskDialog.cancel()
            }
        }

        cancelButton.setOnClickListener {
            addSubtaskDialog.cancel()
        }

        addSubtaskDialog.show()
    }

    private fun setupTime() {
        val time = DateUtils.currentTime.split(":")
        calendar.set(Calendar.HOUR, time[0].toInt() + 1)
        binding.time.text = DateUtils.timeAsHM(calendar.time)
        calendar.time = DateUtils.time
    }

    private fun setupTimePicker() {
        val time = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)

            val date = DateUtils.fromUIFormatToMDYFormat (
                requireContext(),
                binding.date.text.toString()
            )

            // Days between the picked date and the current date
            // How much days left to reach the picked date
            val daysDifference = DateUtils.calculateTimeLeft(date)

            // If the picked date is today we need to make sure that the chosen
            // time is not in the past
            if (daysDifference == 0L) {
                val hourAndMinute = DateUtils.currentTime.split(":")
                if (hourAndMinute[0].toInt() > hourOfDay) {
                    Error.makeToast(requireContext(), "Please, choose a valid time!")
                } else if (hourAndMinute[0].toInt() == hourOfDay && hourAndMinute[1].toInt() <= minute) {
                    Error.makeToast(requireContext(), "Please, choose a valid time!")
                } else {
                    binding.time.text = DateUtils.timeAsHM(calendar.time)
                }
            } else {
                binding.time.text = DateUtils.timeAsHM(calendar.time)
            }

            calendar.time = DateUtils.time
        }

        // Setting the click listener
        binding.taskHour.setOnClickListener {
            TimePickerDialog(
                requireContext(),
                time,
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    private fun setupDate() {
        val date = DateUtils.todayDate.split("/")
        val month = resources.getString(
            DateUtils.getMonthResId(
                date[0].toInt() - 1
            )
        )

        val currentDate = "$month ${date[1]}, ${date[2]}"
        binding.date.text = currentDate
    }

    private fun setupDatePicker() {
        // Setting up the listener
        val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            // Days between the picked date and the current date
            // How much days left to reach the picked date
            val daysDifference = DateUtils.calculateTimeLeft(DateUtils.dateAsMDYFormat(dayOfMonth, monthOfYear, year))

            if (daysDifference >= 0) {
                val month = getString(DateUtils.getMonthResId(monthOfYear))
                val pickedDate = "$month $dayOfMonth, $year"
                binding.date.text = pickedDate

                // Making sure that when the date picked is today we add an hour to the time
                // so that we don't run into problems later
                if (daysDifference == 0L) {
                    val time = DateUtils.currentTime.split(":")
                    calendar.set(Calendar.HOUR, time[0].toInt() + 1)
                    calendar.set(Calendar.MINUTE, time[1].toInt())
                    binding.time.text = DateUtils.timeAsHM(calendar.time)
                }
            } else {
                Error.makeToast(requireContext(), "Please, choose a valid date!")
            }

            calendar.time = DateUtils.time
        }

        // Setting up the click listener
        binding.taskDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    override fun removeItemClick(position: Int) {
        if (subtaskAdapter.removeItemByPosition(position) == 0) {
            binding.subtasksRecyclerview.visibility = View.GONE
            binding.emptySubtask.visibility = View.VISIBLE
        }
    }

}