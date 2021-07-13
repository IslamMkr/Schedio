package com.ismkr.schedio.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ismkr.schedio.interfaces.OnDatabaseUpdated
import com.ismkr.schedio.models.Priority
import com.ismkr.schedio.models.Project
import com.ismkr.schedio.models.Activity
import com.ismkr.schedio.models.User
import com.ismkr.schedio.utils.DateUtils
import com.ismkr.schedio.utils.Error

class FirestoreRepository {

    private val rootRef = FirebaseFirestore.getInstance()
    private val tasksRef = rootRef.collection(TASKS)
    private val projectsRef = rootRef.collection(PROJECTS)

    fun getUserTodayTasks(user: User) : MutableLiveData<List<Activity>>{
        val dayTasksMutableLiveData = MutableLiveData<List<Activity>>()

        tasksRef.whereEqualTo("uid", user.uid)
                .whereEqualTo("date", DateUtils.todayDate)
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener {
                    snapshot, e ->
                    if (e != null) {
                        e.message?.let { Error.logErrorMessage(TAG, it) }
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        val docs = snapshot.documents
                        dayTasksMutableLiveData.value = extractActivities(docs).toList()

                    } else {
                        dayTasksMutableLiveData.value = null
                    }
                }

        return dayTasksMutableLiveData
    }

    fun getUserProjects(user: User): LiveData<List<Project>> {
        val projectsMutableLiveData = MutableLiveData<List<Project>>()

        projectsRef.whereEqualTo("uid", user.uid)
            .addSnapshotListener {
                    snapshot, e ->
                if (e != null) {
                    e.message?.let { Error.logErrorMessage(TAG, it) }
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val docs = snapshot.documents
                    projectsMutableLiveData.value = extractProjects(docs).toList()

                } else {
                    projectsMutableLiveData.value = null
                }
            }

        return projectsMutableLiveData
    }

    fun getSpecificDayActivities(user: User, date: String): LiveData<List<Activity>> {
        val specificDayActivitiesMutableLiveData = MutableLiveData<List<Activity>>()

        tasksRef.whereEqualTo("uid", user.uid)
                .whereEqualTo("date", date)
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener {
                    snapshot, e ->
                    if (e != null) {
                        e.message?.let { Error.logErrorMessage(TAG, it) }
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        val docs = snapshot.documents
                        specificDayActivitiesMutableLiveData.value = extractActivities(docs).toList()

                    } else {
                        specificDayActivitiesMutableLiveData.value = null
                    }
                }

        return specificDayActivitiesMutableLiveData
    }

    fun addTask(user: User, activity: Activity, listener: OnDatabaseUpdated?) {
        activity.uid = user.uid
        tasksRef.add(activity)
            .addOnSuccessListener {
                listener?.onTaskAddedSuccessfully()
                tasksRef.document(it.id).update("tid", it.id)
            }.addOnFailureListener {
                listener?.onTaskAddFailed()
            }
    }


    private fun extractProjects(docs: MutableList<DocumentSnapshot>): MutableList<Project> {
        val projectsList = mutableListOf<Project>()

        for (document in docs) {
            val project = Project()
            project.pid = document.id
            project.uid = document.get("uid").toString()
            project.name = document.get("name").toString()
            project.description = document.get("description").toString()
            project.deadline = document.get("deadline").toString()
            project.progress = document.get("progress").toString().toInt()
            project.creationDate = document.get("creation_date").toString()
            project.priority = when (document.get("priority").toString()) {
                "High" -> Priority.HIGH
                "Med" -> Priority.MEDIUM
                else -> Priority.LOW
            }
            projectsList.add(project)
        }

        return projectsList
    }

    private fun extractActivities(docs: MutableList<DocumentSnapshot>): MutableList<Activity> {
        val activityList = mutableListOf<Activity>()

        for (document in docs) {
            val activity = Activity()
            activity.tid = document.id
            activity.uid = document.get("uid").toString()
            activity.name = document.get("name").toString()
            activity.date = document.get("date").toString()
            activity.time = document.get("time").toString()
            activity.description = document.get("description").toString()
            activity.duration = document.get("duration").toString()
            if (document.get("tasks") != null) {
                activity.tasks.addAll(document.get("tasks") as MutableList<String>)
            }
            activity.link = document.get("link").toString()
            activity.creationDate = document.get("creationDate").toString()
            activityList.add(activity)
        }

        return activityList
    }

    companion object {
        private const val TAG = "FirestoreRepository"
        private const val TASKS = "tasks"
        private const val PROJECTS = "projects"
    }

}