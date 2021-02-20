package com.ismkr.schedio.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.firebase.auth.AuthCredential
import com.ismkr.schedio.models.User
import com.ismkr.schedio.repositories.AuthRepository

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private var authRepository: AuthRepository = AuthRepository()
    lateinit var createdUserLiveData: LiveData<User>
    lateinit var authetificatedUserLiveData: LiveData<User>
    lateinit var emailSentLiveData: LiveData<String>

    fun signInWithGoogle(authCredential: AuthCredential) {
        authetificatedUserLiveData = authRepository.firebaseSignInWithGoogle(authCredential)
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        authetificatedUserLiveData = authRepository.signInWithEmailAndPassword(email, password)
    }

    fun signUpWithEmailAndPassword(email: String, password: String) {
        authetificatedUserLiveData = authRepository.signUpWithEmailAndPassword(email, password)
    }

    fun createUser(authentificatedUser: User) {
        createdUserLiveData = authRepository.createUserInFirebaseIfNotExists(authentificatedUser)
    }

    fun sendPasswordResetEmail(email: String) {
        emailSentLiveData = authRepository.sendPasswordResetEmail(email)
    }

    fun logOut() {
        authRepository.logOut()
    }

}