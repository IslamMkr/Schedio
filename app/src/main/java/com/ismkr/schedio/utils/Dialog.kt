package com.ismkr.schedio.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import com.ismkr.schedio.R
import com.ismkr.schedio.adapters.TaskWithRemoveOptionAdapter
import com.ismkr.schedio.models.Task

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