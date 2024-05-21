package com.example.interviewhub.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.interviewhub.R
import com.example.interviewhub.model.Home
import com.example.interviewhub.unity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {
    private lateinit var Email:TextView
    private lateinit var Password: TextView
    private lateinit var SignIn : Button
    private lateinit var SignInText : TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Intialize firebase authetication and firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        //Assigning values
        Email = findViewById(R.id.txt_email_sign_in)
        Password = findViewById(R.id.txt_password_sign_in)
        SignIn = findViewById(R.id.btn_sign_in)
        SignInText = findViewById(R.id.txt_signin)

        //Registering the user so they're details are stored in the database
        SignInText.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            Toast.makeText(this, "Signing Up", Toast.LENGTH_LONG).show()
        }

        //Signing in when the authentication is valid
        SignIn.setOnClickListener {
            val usernameText = Email.text.toString().trim()
            val passwordText = Password.text.toString().trim()

            if (!TextUtils.isEmpty(usernameText) && !TextUtils.isEmpty(passwordText)) {
                db.collection("users").whereEqualTo("username", usernameText).get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            val userData = documents.documents[0].toObject(User::class.java)
                            if (userData != null && userData.password == passwordText) {
                                auth.signInWithEmailAndPassword(usernameText, passwordText)
                                    .addOnCompleteListener(this) { task ->
                                        if (task.isSuccessful) {
                                            val message = "Login successful: Username=$usernameText"
                                            val intent = Intent(this, Home::class.java)
                                            startActivity(intent)
                                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                                        } else {
                                            val message = "Login failed: ${task.exception?.message}"
                                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                val message = "Incorrect username or password"
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val message = "User not found"
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener { exception ->
                        val message = "Error: ${exception.message}"
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}