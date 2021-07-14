package com.ismkr.schedio.models

data class Activity (
    var name: String = "",
    var date: String = "",
    var time: String = "",
    var description: String = "",
    var link: String = "None"
) {
    var tid: String = ""
    var uid: String = ""
    var duration: String = ""
    var tasks = mutableListOf<Task>()
    var creationDate: String = ""
}