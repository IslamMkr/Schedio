package com.ismkr.schedio.models

data class Project(
        var pid: String = "",
        var uid: String = "",
        var name: String = "",
        var description: String = "",
        var deadline: String = ""
) {
    var progress: Float = 0.0f
    var taskList = mutableListOf<Task>()
    var team = mutableListOf<String>()
    var creationDate: String = ""
    var status: String = "New"
}