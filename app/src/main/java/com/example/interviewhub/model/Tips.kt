package com.example.interviewhub.model

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.interviewhub.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class Tips : AppCompatActivity() {
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)

        bottomNavigation = findViewById(R.id.bottomNavigationView)

// Set the bottom navigation view to be selected
        bottomNavigation.selectedItemId = R.id.settings

        // force the bottom navigation bar to always show icon and title
        bottomNavigation.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_LABELED

        // Set click listener for bottom navigation
        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    // Handle Saved Observations menu item click
                    true
                }
                R.id.add -> {
                    startActivity(Intent(this, InterviewObservation::class.java))
                    true
                }
                R.id.view -> {
                    startActivity(Intent(this, InterviewViewObservation::class.java))
                    true
                }

                R.id.notes -> {
                    //Do nothing, we're already in the settings activity
                    true
                }
                R.id.settings -> {
                    // Handle settings menu item click
                    startActivity(Intent(this, Settings::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
