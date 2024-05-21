package com.example.interviewhub.model

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.interviewhub.Authentication.Login
import com.example.interviewhub.R
import com.example.interviewhub.unity.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Settings : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var user: User
    private lateinit var editCurrentPassword: EditText
    private lateinit var editNewPassword: EditText
    private lateinit var editDeleteAccountPassword: EditText
    private lateinit var btnChangePassword: Button
    private lateinit var btnDeleteAccount: Button
    private lateinit var btnLogout: Button
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        editCurrentPassword = findViewById(R.id.editCurrentPassword)
        editNewPassword = findViewById(R.id.editNewPassword)
        btnChangePassword = findViewById(R.id.btnChangePassword)
        editDeleteAccountPassword = findViewById(R.id.editDeleteAccountPassword)
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount)
        btnLogout = findViewById(R.id.btnLogout)
        bottomNavigation = findViewById(R.id.bottomNavigationView)

        btnChangePassword.setOnClickListener {
            // Implement password change functionality
            val currentPassword = editCurrentPassword.text.toString().trim()
            val newPassword = editNewPassword.text.toString().trim()

            // Perform password change logic using Firebase Auth
            val user = auth.currentUser
            val credential = EmailAuthProvider.getCredential(user!!.email!!, currentPassword)

            user.reauthenticate(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        user.updatePassword(newPassword)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Password changed successfully",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    startActivity(Intent(this, Login::class.java))
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Password could not be changed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_LONG).show()
                    }
                }

            editNewPassword.text.clear()
        }
        btnDeleteAccount.setOnClickListener {
            // Implement account deletion functionality
            // Get the user's current password
            val currentPassword = editDeleteAccountPassword.text.toString().trim()

            // Check if the password is correct
            val user = auth.currentUser
            val credential = EmailAuthProvider.getCredential(user!!.email!!, currentPassword)

            user.reauthenticate(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Password correct, deleting account",
                            Toast.LENGTH_LONG
                        ).show()

                        // Perform delete account logic using Firebase Auth
                        auth.currentUser?.delete()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_LONG)
                                        .show()
                                    startActivity(Intent(this, Login::class.java))
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Account could not be deleted",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                        // delete user from firestore
                        val userRef = db.collection("users").document(auth.currentUser!!.uid)
                        userRef.delete()
                            .addOnSuccessListener {
                                Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_LONG)
                                    .show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    "Account could not be deleted",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        editNewPassword.text.clear()
                    } else {
                        Toast.makeText(this, "Password incorrect", Toast.LENGTH_LONG).show()
                    }
                }
        }

        btnLogout.setOnClickListener {
            // Perform logout logic using Firebase Auth
            auth.signOut()

            // Notify the user and start the login activity
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, Login::class.java))
        }

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
                    // Handle settings menu item click
                    startActivity(Intent(this, Tips::class.java))
                    true
                }
                R.id.settings -> {
                    //Do nothing, we're already in the settings activity
                    true
                }
                else -> false
            }
        }
    }
}
