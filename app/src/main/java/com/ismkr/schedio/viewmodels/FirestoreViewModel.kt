package com.ismkr.schedio.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ismkr.schedio.interfaces.OnDatabaseUpdated
import com.ismkr.schedio.models.Project
import com.ismkr.schedio.models.Task
import com.ismkr.schedio.models.User
import com.ismkr.schedio.repositories.FirestoreRepository

class FirestoreViewModel(application: Application) : AndroidViewModel(application) {

    private val firestoreRepository = FirestoreRepository()

    lateinit var userTodayTasksLiveData: LiveData<List<Task>>
    lateinit var userProjectsLiveData: LiveData<List<Project>>
    lateinit var userSpecificDayTasksLiveData: LiveData<List<Task>>

    fun getUserTodayTasks(user: User) {
        userTodayTasksLiveData = firestoreRepository.getUserTodayTasks(user)
    }

    fun getUserProjects(user: User) {
        userProjectsLiveData = firestoreRepository.getUserProjects(user)
    }

    fun getSpecificDayTasks(user: User, date: String) {
        userSpecificDayTasksLiveData = firestoreRepository.getSpecificDayTasks(user, date)
    }

    fun addTask(user: User, task: Task, listener: OnDatabaseUpdated? = null) {
        firestoreRepository.addTask(user, task, listener)
    }

}