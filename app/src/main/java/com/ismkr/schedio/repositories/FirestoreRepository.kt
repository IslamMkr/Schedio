package com.ismkr.schedio.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ismkr.schedio.interfaces.OnDatabaseUpdated
import com.ismkr.schedio.models.Priority
import com.ismkr.schedio.models.Project
import com.ismkr.schedio.models.Task
import com.ismkr.schedio.models.User
import com.ismkr.schedio.utils.DateUtils
import com.ismkr.schedio.utils.Error

class FirestoreRepository {

    private val rootRef = FirebaseFirestore.getInstance()
    private val tasksRef = rootRef.collection(TASKS)
    private val projectsRef = rootRef.collection(PROJECTS)

    fun getUserTodayTasks(user: User) : MutableLiveData<List<Task>>{
        val dayTasksMutableLiveData = MutableLiveData<List<Task>>()

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
                        dayTasksMutableLiveData.value = extractTasks(docs).toList()

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

    fun getSpecificDayTasks(user: User, date: String): LiveData<List<Task>> {
        val specificDayTasksMutableLiveData = MutableLiveData<List<Task>>()

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
                        specificDayTasksMutableLiveData.value = extractTasks(docs).toList()

                    } else {
                        specificDayTasksMutableLiveData.value = null
                    }
                }

        return specificDayTasksMutableLiveData
    }

    fun addTask(user: User, task: Task, listener: OnDatabaseUpdated?) {
        task.uid = user.uid
        tasksRef.add(task)
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

    private fun extractTasks(docs: MutableList<DocumentSnapshot>): MutableList<Task> {
        val tasksList = mutableListOf<Task>()

        for (document in docs) {
            val task = Task()
            task.tid = document.id
            task.uid = document.get("uid").toString()
            task.name = document.get("name").toString()
            task.date = document.get("date").toString()
            task.time = document.get("time").toString()
            task.description = document.get("description").toString()
            task.duration = document.get("duration").toString()
            task.subTasks = document.get("subTasks") as MutableList<String>
            task.link = document.get("link").toString()
            task.creationDate = document.get("creationDate").toString()
            tasksList.add(task)
        }

        return tasksList
    }

    companion object {
        private const val TAG = "FirestoreRepository"
        private const val TASKS = "tasks"
        private const val PROJECTS = "projects"
    }

}