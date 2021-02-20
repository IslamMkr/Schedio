package com.ismkr.schedio.utils

import android.app.AlertDialog
import android.content.Context

object Dialog {

    fun errorDialogMessage(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("Try again", null)
                .show()
    }

    fun infoDialogMessage(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("Ok", null)
                .show()
    }

}