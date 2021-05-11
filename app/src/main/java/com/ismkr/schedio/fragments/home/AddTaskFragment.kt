package com.ismkr.schedio.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ismkr.schedio.databinding.FragmentAddTaskBinding

class AddTaskFragment : Fragment() {

    private lateinit var binding: FragmentAddTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)

        binding.arrowBack.setOnClickListener {
            findNavController().navigateUp()
        }

        setupDate()
        setupTime()
        setupSubtasksRecyclerView()
        setupLinkSpinner()

        return binding.root
    }

    private fun setupLinkSpinner() {
        TODO("Not yet implemented")
    }

    private fun setupSubtasksRecyclerView() {
        TODO("Not yet implemented")
    }

    private fun setupTime() {
        TODO("Not yet implemented")
    }

    private fun setupDate() {
        TODO("Not yet implemented")
    }

}