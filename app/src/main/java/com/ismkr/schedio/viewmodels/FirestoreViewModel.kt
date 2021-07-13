package com.ismkr.schedio.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ismkr.schedio.R
import com.ismkr.schedio.interfaces.OnDatabaseUpdated
import com.ismkr.schedio.models.Project
import com.ismkr.schedio.models.Activity
import com.ismkr.schedio.models.Section
import com.ismkr.schedio.models.User
import com.ismkr.schedio.repositories.FirestoreRepository
import com.ismkr.schedio.utils.DateUtils

class FirestoreViewModel(application: Application) : AndroidViewModel(application) {

    private val firestoreRepository = FirestoreRepository()

    lateinit var userTodayTasksLiveData: LiveData<List<Activity>>
    lateinit var userProjectsLiveData: LiveData<List<Project>>
    lateinit var userSpecificDayTasksLiveData: LiveData<List<Section>>

    fun getUserTodayTasks(user: User) {
        userTodayTasksLiveData = firestoreRepository.getUserTodayTasks(user)
    }

    fun getUserProjects(user: User) {
        userProjectsLiveData = firestoreRepository.getUserProjects(user)
    }

    fun getSpecificDayTasks(user: User, date: String, lifecycleOwner: LifecycleOwner) {
        userSpecificDayTasksLiveData = sectionGroupedActivities(user, date, lifecycleOwner)
    }

    private fun sectionGroupedActivities(user: User, date: String, lifecycleOwner: LifecycleOwner): LiveData<List<Section>> {
        val sectionsListLiveData = MutableLiveData<List<Section>>()

        firestoreRepository.getSpecificDayActivities(user, date).observe(
            lifecycleOwner, {
                        sectionsListLiveData.value = groupActivitiesInSections(it)
            }
        )

        return sectionsListLiveData
    }

    fun addTask(user: User, activity: Activity, listener: OnDatabaseUpdated? = null) {
        firestoreRepository.addTask(user, activity, listener)
    }

    private fun groupActivitiesInSections(activitiesList: List<Activity>?): List<Section> {
        val inProgress = Section("In Progress", R.drawable.ic_time)
        val todo = Section("To Do", R.drawable.ic_task)
        val done = Section("Done", R.drawable.ic_work_done)

        if (!activitiesList.isNullOrEmpty()) {
            activitiesList.forEach {
                when (DateUtils.compareTime(it.time, it.duration)) {
                    1       -> inProgress.activities.add(it)
                    -1      -> done.activities.add(it)
                    else    -> todo.activities.add(it)
                }
            }
        }

        val sections = mutableListOf<Section>()
        sections.add(inProgress)
        sections.add(todo)
        sections.add(done)

        return sections.toList()
    }

}