package com.ismkr.schedio.fragments.login

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ismkr.schedio.R
import com.ismkr.schedio.activities.HomeActivity
import com.ismkr.schedio.databinding.FragmentSignUpBinding
import com.ismkr.schedio.models.User
import com.ismkr.schedio.utils.Error
import com.ismkr.schedio.viewmodels.AuthViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignUp : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        initPasswordCheckBox()
        initBackToMainLogIn()
        initSignUpButton()

        return binding.root
    }

    private fun initPasswordCheckBox() {
        binding.passwordCheckbox.setOnCheckedChangeListener { _, isChecked ->
            binding.passwordTiet.transformationMethod = if (!isChecked) {
                PasswordTransformationMethod.getInstance()
            } else {
                HideReturnsTransformationMethod.getInstance()
            }
        }
    }

    private fun initBackToMainLogIn() {
        binding.arrowBack.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_logInMain)
        }

        binding.logInTv.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_logInMain)
        }
    }

    private fun initSignUpButton() {
        binding.signUpButton.setOnClickListener {
            val name = binding.usernameEt.text.toString()
            val email = binding.emailEt.text.toString()
            val password = binding.passwordTiet.text.toString()

            if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                binding.progressLayout.visibility = View.VISIBLE
                signUpWithEmailAndPassword(email, password, name)
            }
        }
    }

    private fun signUpWithEmailAndPassword(email: String, password: String, name: String) {
        authViewModel.signUpWithEmailAndPassword(email, password)
        authViewModel.authetificatedUserLiveData.observe(
                viewLifecycleOwner,
                { authentifiedUser ->
                    if (Error.manageErrors(requireContext(), authentifiedUser.uid)) {
                        authentifiedUser.name = name
                        if (authentifiedUser.isNewUser) {
                            createNewUser(authentifiedUser!!)
                        } else {
                            binding.progressText.text = "Account created."
                            GlobalScope.launch {
                                delay(500)
                                goToHomeActivity(authentifiedUser!!)
                            }
                        }
                    } else {
                        binding.progressLayout.visibility = View.GONE
                    }
                }
        )
    }

    private fun createNewUser(authentifiedUser: User) {
        authViewModel.createUser(authentifiedUser)
        authViewModel.createdUserLiveData.observe(
                viewLifecycleOwner,
                { user ->
                    binding.progressText.text = "Account created."
                    GlobalScope.launch {
                        delay(500)
                        goToHomeActivity(user)
                    }
                }
        )
    }

    private fun goToHomeActivity(authentifiedUser: User) {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        intent.putExtra("user", authentifiedUser)
        startActivity(intent)

        activity?.finish()
    }

}