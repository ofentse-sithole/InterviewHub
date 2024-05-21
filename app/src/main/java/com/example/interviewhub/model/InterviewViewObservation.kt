package com.example.interviewhub.model

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewhub.R
import com.example.interviewhub.databinding.ActivityInterviewViewObservationBinding
import com.example.interviewhub.unity.InterviewObservationAdapter
import com.example.interviewhub.unity.SaveInterview
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseUser

class InterviewViewObservation : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding: ActivityInterviewViewObservationBinding
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var InterviewObservationAdapter: InterviewObservationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interview_view_observation)

        // Initialize Firebase Authentication and Firestore reference

        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser ?: return // Check if the user is authenticated
        firestore = FirebaseFirestore.getInstance()

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.observationRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        InterviewObservationAdapter = InterviewObservationAdapter()
        recyclerView.adapter = InterviewObservationAdapter

        // Display observations from Firestore
        displayObservations()

        }

    private fun displayObservations() {
        // Get the user's observations from Firestore
        firestore.collection("users").document(currentUser.uid).collection("observations")
            .get()
            .addOnCompleteListener { documents ->
                val observations = mutableListOf<SaveInterview>()
                for (document in documents.result!!) {
                    val questions = document.getString("questions") ?: ""
                    val answers = document.getString("answers") ?: ""
                    val tips = document.getString("tips") ?: ""
                    val difficulties = document.getString("difficulties") ?: ""
                    val observation = SaveInterview(questions, answers, tips, difficulties)
                    observations.add(observation)
                }
                InterviewObservationAdapter.submitList(observations)
            }
            .addOnFailureListener { e ->
                // Log the error
                println("Error getting documents: $e")
            }
        }
    }
