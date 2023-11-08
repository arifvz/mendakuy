package com.arif.mendakuy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.arif.mendakuy.data.AuthManager
import com.arif.mendakuy.databinding.ActivityMainBinding
import com.arif.mendakuy.ui.login.LoginActivity

import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                    navView.itemActiveIndicatorColor(R.color.primary200)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_explore -> {
                    navController.navigate(R.id.navigation_explore)
                    navView.itemActiveIndicatorColor(R.color.primary200)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_create -> {
                    if (checkLogin()) {
                        navController.navigate(R.id.navigation_create)
                        navView.itemActiveIndicatorColor(R.color.primary200)
                        return@setOnItemSelectedListener true
                    }
                    return@setOnItemSelectedListener false
                }
                R.id.navigation_guide -> {
                    navController.navigate(R.id.navigation_guide)
                    navView.itemActiveIndicatorColor(R.color.primary200)
                    return@setOnItemSelectedListener true
                    }
                R.id.navigation_profile -> {
                    if (checkLogin()) {
                        navController.navigate(R.id.navigation_profile)
                        navView.itemActiveIndicatorColor(R.color.primary200)

                        return@setOnItemSelectedListener true
                    }
                    return@setOnItemSelectedListener false
                }
            }
            return@setOnItemSelectedListener false
        }
    }

    private fun checkLogin(): Boolean {
        val isLogin = AuthManager.isLogin(this)
        if (!isLogin) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        return isLogin
    }


}

private fun BottomNavigationView.itemActiveIndicatorColor(primary200: Int) {

}
