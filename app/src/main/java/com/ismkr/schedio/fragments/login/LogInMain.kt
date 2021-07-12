package com.ismkr.schedio.fragments.login

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.ismkr.schedio.R
import com.ismkr.schedio.activities.HomeActivity
import com.ismkr.schedio.databinding.FragmentLogInBinding
import com.ismkr.schedio.models.User
import com.ismkr.schedio.viewmodels.AuthViewModel
import com.ismkr.schedio.utils.Error

class LogInMain : Fragment() {

    private lateinit var binding: FragmentLogInBinding

    private lateinit var authViewModel: AuthViewModel

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(inflater, container, false)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        initPasswordCheckBox()
        initForgotPassword()
        initGoogleSignInClient()
        initGoogleSignInButton()
        initLogInButton()
        initSignUpTV()

        return binding.root
    }

    private fun initForgotPassword() {
        binding.forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_logInMain_to_resetPassword)
        }
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

    private fun initSignUpTV() {
        binding.signUpTv.setOnClickListener {
            findNavController().navigate(R.id.action_logInMain_to_signUp)
        }
    }

    private fun initLogInButton() {
        binding.logInButton.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordTiet.text.toString()

            if (!email.isEmpty() && !password.isEmpty()) {
                binding.progressLayout.visibility = View.VISIBLE
                signInWithEmailAndPassword(email, password)
            }
        }
    }

    private fun initGoogleSignInClient() {
        val googleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)
    }

    private fun initGoogleSignInButton() {
        binding.googleButton.setOnClickListener {
            val googleSignInIntent = googleSignInClient.signInIntent
            startActivityForResult(googleSignInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val googleSignInAccount = task.getResult(ApiException::class.java)

                if (googleSignInAccount != null) {
                    getGoogleAuthCredential(googleSignInAccount)
                }
            } catch (e: ApiException) {
                Error.logErrorMessage(TAG, e.message!!)
            }
        }
    }

    private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
        val googleTokenId = googleSignInAccount.idToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
        signInWithGoogleAuthCredential(googleAuthCredential)
    }

    private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
        authViewModel.signInWithGoogle(googleAuthCredential)
        authViewModel.authetificatedUserLiveData.observe(
            this,
            { authentificatedUser ->
                if (authentificatedUser.isNewUser) {
                    createNewUser(authentificatedUser)
                } else {
                    goToHomeActivity(authentificatedUser)
                }
            }
        )
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        authViewModel.signInWithEmailAndPassword(email, password)
        authViewModel.authetificatedUserLiveData.observe(
            viewLifecycleOwner,
            { authentifiedUser ->
                binding.progressLayout.visibility = View.GONE
                if (Error.manageErrors(requireContext(), authentifiedUser.uid)) {
                    goToHomeActivity(authentifiedUser)
                }
            }
        )
    }

    private fun createNewUser(authentificatedUser: User?) {
        authViewModel.createUser(authentificatedUser!!)
        authViewModel.createdUserLiveData.observe(
            viewLifecycleOwner,
            { user ->
                if (user.isCreated) {
                    Toast.makeText(requireContext(), user.name, Toast.LENGTH_SHORT).show()
                }

                goToHomeActivity(user)
            }
        )
    }

    private fun goToHomeActivity(user: User) {
        val intent = Intent(activity, HomeActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)

        activity?.finish()
    }

    companion object {
        const val TAG = "LoginMain"
        const val RC_SIGN_IN = 1


    }

}