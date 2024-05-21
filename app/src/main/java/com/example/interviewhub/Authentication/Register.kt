package com.example.interviewhub.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.interviewhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



class Register : AppCompatActivity() {

    private lateinit var Fullname: EditText
    private lateinit var Email: EditText
    private lateinit var Password: EditText
    private lateinit var SignUp : Button
    private lateinit var SignUpText : TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Intialize firebase authetication and firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        //Assigning values
        Fullname = findViewById(R.id.txt_name_signup)
        Email = findViewById(R.id.txt_email_sign_up)
        Password = findViewById(R.id.txt_password_sign_up)
        SignUp = findViewById(R.id.btn_sign_up)
        SignUpText = findViewById(R.id.txt_sign_in)

        //Registering the user so they're details are stored in the database
        SignUpText.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            Toast.makeText(this, "Signing In", Toast.LENGTH_LONG).show()
        }

        //Signing Up users and storing details within the database
        SignUp.setOnClickListener {
            val username = Email.text.toString()
            val password = Password.text.toString()

            if (TextUtils.isEmpty(username)  || TextUtils.isEmpty(password) ) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val message =
                                "Registration successful: Username=$username, Password=$password"
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                            val currentUser = auth.currentUser
                            val user = hashMapOf(
                                "username" to username,
                                "password" to password
                            )

                            if (currentUser != null) {
                                db.collection("users").document(currentUser.uid)
                                    .set(user)
                                    .addOnSuccessListener {
                                        val intent = Intent(this, Login::class.java)
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            this,
                                            "Failed to add user to Firestore",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        } else {
                            val exception = task.exception
                            Toast.makeText(this, exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}