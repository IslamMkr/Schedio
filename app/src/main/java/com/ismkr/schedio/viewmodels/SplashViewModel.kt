package com.ismkr.schedio.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ismkr.schedio.models.User
import com.ismkr.schedio.repositories.SplashRepository

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val splashRepository = SplashRepository()

    lateinit var isUserAuthentificatedLiveData: LiveData<User>
    lateinit var userLiveData: LiveData<User>

    fun checkIfUserIsAuthentified() {
        isUserAuthentificatedLiveData = splashRepository.checkIfUserIsAuthentificatedInFirebase()
    }

    fun setUID(uid: String) {
        userLiveData = splashRepository.addUserToLiveData(uid)
    }

}