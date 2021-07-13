package com.ismkr.schedio.models

data class Section(
    val title: String = "",
    val iconId: Int = 0,
    val activities: MutableList<Activity> = mutableListOf()
)