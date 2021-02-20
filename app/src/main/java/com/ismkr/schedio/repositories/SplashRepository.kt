package com.ismkr.schedio.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ismkr.schedio.models.User
import com.ismkr.schedio.utils.Error

class SplashRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = rootRef.collection(AuthRepository.USERS)

    private val user = User()

    fun checkIfUserIsAuthentificatedInFirebase(): MutableLiveData<User> {
        val isUserAuthentificatedInFirebaseMutableLiveData = MutableLiveData<User>()
        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser == null) {
            user.isAuthenticated = false
            isUserAuthentificatedInFirebaseMutableLiveData.value = user
        } else {
            user.uid = firebaseUser.uid
            user.isAuthenticated = true
            isUserAuthentificatedInFirebaseMutableLiveData.value = user
        }

        return isUserAuthentificatedInFirebaseMutableLiveData
    }

    fun addUserToLiveData(uid: String): MutableLiveData<User> {
        val userMutableLiveData = MutableLiveData<User>()

        usersRef.document(uid).get().addOnCompleteListener { userTask ->
            if (userTask.isSuccessful) {
                val document = userTask.result

                if (document!!.exists()) {
                    val user = document.toObject(User::class.java)
                    userMutableLiveData.value = user
                }
            } else {
                Error.logErrorMessage(TAG, userTask.exception?.message!!)
            }
        }

        return userMutableLiveData
    }

    companion object {
        const val TAG = "SplashRepository"
    }

}