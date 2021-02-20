package com.ismkr.schedio.utils

import android.content.Context
import android.util.Log

object Error {

    fun logErrorMessage(tag: String, message: String) {
        Log.e(tag, message)
    }

    fun manageErrors(context: Context, message: String) : Boolean {
        when (message) {
            EMAIL_AND_PASSWORD_ERROR -> Dialog.errorDialogMessage(context, "Password do not match your email", EMAIL_AND_PASSWORD_ERROR_MESSAGE)
            SHORT_PASSWORD -> Dialog.errorDialogMessage(context, "" + "Invalid password", SHORT_PASSWORD)
            EMAIL_BADLY_FORMATTED_ERROR -> Dialog.errorDialogMessage(context, "Invalide email", EMAIL_BADLY_FORMATTED_ERROR)
            ACCOUNT_ALREADY_EXIST_ERROR -> Dialog.errorDialogMessage(context, "Email already used", ACCOUNT_ALREADY_EXIST_ERROR)

            USER_DON_NOT_EXIST_ERROR -> Dialog.errorDialogMessage(context, "User do not exist", NO_USER_ERROR_MESSAGE)
            else -> return true
        }

        return false
    }

    const val NO_ERROR = "Email sent"

    private const val EMAIL_AND_PASSWORD_ERROR = "The password is invalid or the user does not have a password."
    private const val ACCOUNT_ALREADY_EXIST_ERROR = "The email address is already in use by another account."
    private const val EMAIL_BADLY_FORMATTED_ERROR = "The email address is badly formatted."
    private const val SHORT_PASSWORD = "The given password is invalid. [ Password should be at least 6 characters ]"
    private const val USER_DON_NOT_EXIST_ERROR = "There is no user record corresponding to this identifier. The user may have been deleted."

    private const val EMAIL_AND_PASSWORD_ERROR_MESSAGE = "The password is invalid or your account does not have a password."
    private const val NO_USER_ERROR_MESSAGE = "There is no user record corresponding to this email."

}