package com.ismkr.schedio.models

data class Task(
    var name: String = "",
    var date: String = "",
    var time: String = "",
    var description: String = "",
    var link: String = "None"
) {
    var tid: String = ""
    var uid: String = ""
    var duration: String = ""
    var subTasks = mutableListOf<String>()
    var creationDate: String = ""
}