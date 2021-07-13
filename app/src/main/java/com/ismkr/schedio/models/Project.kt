package com.ismkr.schedio.models

data class Project(
        var pid: String = "",
        var uid: String = "",
        var name: String = "",
        var description: String = "",
        var deadline: String = "",
        var priority: Priority = Priority.LOW
) {
    var progress: Int = 0
    var taskList = mutableListOf<Activity>()
    var team = mutableListOf<String>()
    var creationDate: String = ""
    var status: String = "New"
}