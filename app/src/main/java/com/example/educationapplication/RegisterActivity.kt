package com.example.educationapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        val registerBtn = findViewById<Button>(R.id.registerBtn)
        val emailEditText = findViewById<EditText>(R.id.type_email)
        val passwordEditText = findViewById<EditText>(R.id.type_password)
        val retypepasswordEditText = findViewById<EditText>(R.id.retype_password)

        val LoginLink = findViewById<TextView>(R.id.login_link)

        LoginLink.setOnClickListener {
            // Open RegisterActivity
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        registerBtn.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val retypePassword = retypepasswordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && retypePassword.isNotEmpty()) {
                if (password == retypePassword) {
                    // Passwords match, proceed with registration
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // Registration failed
                                val exception = task.exception as FirebaseAuthException?
                            }
                        }
                } else {
                    // Passwords don't match, show an error message
                    Toast.makeText(this@RegisterActivity, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Show an error message indicating that all fields are required
                Toast.makeText(this@RegisterActivity, "All fields are required.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}