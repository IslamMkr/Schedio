package com.ismkr.schedio.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ismkr.schedio.activities.HomeActivity
import com.ismkr.schedio.databinding.FragmentProfilBinding
import com.ismkr.schedio.viewmodels.AuthViewModel

class ProfilFragment : Fragment() {

    private lateinit var binding: FragmentProfilBinding

    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilBinding.inflate(inflater, container, false)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        binding.disconnect.setOnClickListener {
            disconnect()
        }

        return binding.root
    }

    private fun disconnect() {
        authViewModel.logOut()
        (activity as HomeActivity).logout()
    }

}