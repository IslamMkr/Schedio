package com.ismkr.schedio.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ismkr.schedio.models.User
import com.ismkr.schedio.utils.Error

class AuthRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = rootRef.collection(USERS)

    fun firebaseSignInWithGoogle(authCredential: AuthCredential) : MutableLiveData<User> {
        val authentificatedUserMutableLiveData = MutableLiveData<User>()

        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener { authTask ->
            if (authTask.isSuccessful) {
                val isNewUser = authTask.result?.additionalUserInfo?.isNewUser
                val firebaseUser = firebaseAuth.currentUser

                if (firebaseUser != null) {
                    val uid = firebaseUser.uid
                    val name = firebaseUser.displayName
                    val email = firebaseUser.email

                    if (name != null && email != null) {
                        val user = User(uid, name, email)

                        if (isNewUser != null) {
                            user.isNewUser = isNewUser
                        }

                        authentificatedUserMutableLiveData.value = user
                    }
                }
            } else {
                Error.logErrorMessage(TAG, authTask.exception?.message!!)
            }
        }

        return authentificatedUserMutableLiveData
    }

    fun createUserInFirebaseIfNotExists(authentificatedUser: User): LiveData<User> {
        val newUserMutableLiveData = MutableLiveData<User>()
        val uidRef = usersRef.document(authentificatedUser.uid)

        uidRef.get().addOnCompleteListener { uidTask ->
            if (uidTask.isSuccessful) {
                val document = uidTask.result

                if (!document?.exists()!!) {
                    uidRef.set(authentificatedUser).addOnCompleteListener { userCreatedTask ->
                        if (userCreatedTask.isSuccessful) {
                            authentificatedUser.isCreated = true
                            newUserMutableLiveData.value = authentificatedUser
                        } else {
                            Error.logErrorMessage(TAG, userCreatedTask.exception?.message!!)
                        }
                    }
                } else {
                    newUserMutableLiveData.value = authentificatedUser
                }
            } else {
                Error.logErrorMessage(TAG, uidTask.exception?.message!!)
            }
        }

        return newUserMutableLiveData
    }

    fun signInWithEmailAndPassword(email: String, password: String) : MutableLiveData<User> {
        val authentificatedUserMutableLiveData = MutableLiveData<User>()

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { authTask ->
            if (authTask.isSuccessful) {
                val firebaseUser = firebaseAuth.currentUser

                if (firebaseUser != null) {
                    val user = User()
                    user.uid = firebaseUser.uid
                    user.email = firebaseUser.email.toString()
                    user.name = firebaseUser.displayName.toString()

                    authentificatedUserMutableLiveData.value = user
                }
            } else {
                val user = User(authTask.exception?.message!!)
                authentificatedUserMutableLiveData.value = user
                Error.logErrorMessage(TAG, authTask.exception?.message!!)
            }
        }

        return authentificatedUserMutableLiveData
    }

    fun signUpWithEmailAndPassword(email: String, password: String): LiveData<User> {
        val authentificatedUserMutableLiveData = MutableLiveData<User>()

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { authTask ->
            if (authTask.isSuccessful) {
                val isNewUser = authTask.result?.additionalUserInfo?.isNewUser
                val firebaseUser = firebaseAuth.currentUser

                if (firebaseUser != null) {
                    val user = User()
                    user.uid = firebaseUser.uid
                    user.email = firebaseUser.email.toString()

                    if (isNewUser != null) {
                        user.isNewUser = isNewUser
                    }

                    authentificatedUserMutableLiveData.value = user
                }
            } else {
                val user = User(authTask.exception?.message!!)
                authentificatedUserMutableLiveData.value = user
                Error.logErrorMessage(TAG, authTask.exception?.message!!)
            }
        }

        return authentificatedUserMutableLiveData
    }

    fun sendPasswordResetEmail(email: String): LiveData<String> {
        val emailSentMutableLiveData = MutableLiveData<String>()

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { sendTask ->
            if (sendTask.isSuccessful) {
                emailSentMutableLiveData.value = Error.NO_ERROR
            } else {
                emailSentMutableLiveData.value = sendTask.exception?.message!!
                Error.logErrorMessage(TAG, sendTask.exception?.message!!)
            }
        }

        return emailSentMutableLiveData
    }

    fun logOut() {
        firebaseAuth.signOut()
    }

    companion object {
        const val TAG = "AuthRepository"
        const val USERS = "users"
    }

}