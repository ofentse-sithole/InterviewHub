package com.example.interviewhub.model

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.interviewhub.R
import com.example.interviewhub.unity.SaveInterview
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class InterviewObservation : AppCompatActivity() {
    private lateinit var question: EditText
    private lateinit var answer : EditText
    private lateinit var tip: EditText
    private lateinit var difficulty: EditText
    private lateinit var submit: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interview_observation)

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser
        db = FirebaseFirestore.getInstance()

        //Assign the variables
        question = findViewById(R.id.edtInterviewQ1)
        answer = findViewById(R.id.edtInterviewAnswer)
        tip = findViewById(R.id.edtInterviewTip)
        difficulty = findViewById(R.id.edtInterviewDifficulty)
        submit = findViewById(R.id.btnSubmit)

        //Submit button will save in Firebase
        submit.setOnClickListener {
            saveObservationToFirestore()
        }

    }

    //method that assist in saving within the database
    private fun saveObservationToFirestore() {
        val questions = question.text.toString()
        val answers = answer.text.toString()
        val tips = tip.text.toString()
        val difficulties = difficulty.text.toString()

        val user = firebaseAuth.currentUser

        if (user != null) {
            if (questions.isNotEmpty() && answers.isNotEmpty()) {
                val observation = SaveInterview(questions, answers, tips, difficulties)

                // Get the user's unique ID
                val userId = user.uid

                db.collection("users").document(userId).collection("observations")
                    .add(observation)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(
                            this,
                            "Observation successfully saved!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error adding observation: $e", Toast.LENGTH_SHORT)
                            .show()
                    }
            } else {
                Toast.makeText(
                    this,
                    "Species and Location fields are required.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}