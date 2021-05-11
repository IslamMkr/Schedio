package com.ismkr.schedio.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ismkr.schedio.R
import com.ismkr.schedio.databinding.ActivityHomeBinding
import com.ismkr.schedio.models.Project
import com.ismkr.schedio.models.Task
import com.ismkr.schedio.models.User
import com.ismkr.schedio.viewmodels.FirestoreViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var user: User
    private lateinit var firestoreViewModel: FirestoreViewModel

    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestoreViewModel = ViewModelProvider(this).get(FirestoreViewModel::class.java)
        user = intent.getSerializableExtra("user") as User

        setUpNavigation()
    }

    private fun setUpNavigation() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        binding.bottomNav.setupWithNavController(navHostFragment.navController)
    }

    fun getUserTodayTasks(): LiveData<List<Task>>{
        firestoreViewModel.getUserTodayTasks(user)
        return firestoreViewModel.userTodayTasksLiveData
    }

    fun getUserProjects(): LiveData<List<Project>> {
        firestoreViewModel.getUserProjects(user)
        return firestoreViewModel.userProjectsLiveData
    }

    fun getSpecificDayTasks(date: String): LiveData<List<Task>> {
        firestoreViewModel.getSpecificDayTasks(user, date)
        return firestoreViewModel.userSpecificDayTasksLiveData
    }

    fun getUser(): User {
        return user
    }

    fun logout() {
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
        finish()
    }

}