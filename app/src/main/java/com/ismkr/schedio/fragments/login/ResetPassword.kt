package com.ismkr.schedio.fragments.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ismkr.schedio.R
import com.ismkr.schedio.databinding.FragmentResetPasswordBinding
import com.ismkr.schedio.utils.Dialog
import com.ismkr.schedio.utils.Error
import com.ismkr.schedio.viewmodels.AuthViewModel

class ResetPassword : Fragment() {

    private lateinit var binding: FragmentResetPasswordBinding

    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetPasswordBinding.inflate(layoutInflater, container, false)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        initResetButton()
        initBackArrow()

        return binding.root
    }

    private fun initBackArrow() {
        binding.arrowBack.setOnClickListener {
            findNavController().navigate(R.id.action_resetPassword_to_logInMain)
        }
    }

    private fun initResetButton() {
        binding.resetLinkButton.setOnClickListener {
            val email = binding.emailEt.text.toString()

            if (!email.isEmpty()) {
                sendPasswordResetEmail(email)
            }
        }
    }

    private fun sendPasswordResetEmail(email: String) {
        authViewModel.sendPasswordResetEmail(email)
        authViewModel.emailSentLiveData.observe(
                viewLifecycleOwner,
                { error ->
                    if (Error.manageErrors(requireContext(), error)) {
                        Dialog.infoDialogMessage(requireContext(), "Reset email sent", "Password reset link was sent to your email address. Please check your email account.")
                    }
                }
        )
    }

}