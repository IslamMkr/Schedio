package com.ismkr.schedio.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ismkr.schedio.R
import com.ismkr.schedio.models.User
import com.ismkr.schedio.viewmodels.SplashViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        waitAndFinish()
    }

    private fun waitAndFinish() {
        lifecycleScope.launch {
            delay(3000)
            checkIfUserIsAuthentified()
        }
    }

    private fun checkIfUserIsAuthentified() {
        splashViewModel.checkIfUserIsAuthentified()
        splashViewModel.isUserAuthentificatedLiveData.observe(
            this,
            { user ->
                if (!user.isAuthenticated) {
                    goToLogInActivity()
                } else {
                    getUserFromDatabase(user.uid)
                }
            }
        )
    }

    private fun getUserFromDatabase(uid: String) {
        splashViewModel.setUID(uid)
        splashViewModel.userLiveData.observe(
            this,
            { user ->
                goToHomeActivity(user)
            }
        )
    }

    private fun goToHomeActivity(user: User) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
        finish()
    }

    private fun goToLogInActivity() {
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
        finish()
    }

}