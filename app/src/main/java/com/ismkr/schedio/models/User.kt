package com.ismkr.schedio.models

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class User(
    var uid: String = "",
    var name: String = "",
    @SuppressWarnings("WeakerAccess")
    var email: String = ""
) : Serializable {

    @Exclude
    var isNewUser = true
    @Exclude
    var isAuthenticated = false
    @Exclude
    var isCreated = false

}