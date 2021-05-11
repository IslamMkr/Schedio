package com.ismkr.schedio.models

data class Task(
    var tid: String = "",
    var uid: String = "",
    var name: String = "",
    var date: String = "",
    var time: String = "",
    var description: String = "",
    var duration: String = ""
) {
    val subTasks = mutableListOf<String>()
    var creationDate: String = ""
    val status: String = "New"
}