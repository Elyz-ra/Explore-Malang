package com.example.exploremalang

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        editTextEmail = findViewById(R.id.editText)
        editTextPassword = findViewById(R.id.editText2)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (email.isEmpty()) {
                editTextEmail.error = "Email is required!"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.error = "Invalid email format!"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                editTextPassword.error = "Password is required!"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                editTextPassword.error = "Password must be at least 6 characters long!"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(baseContext, "Registration successful!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(baseContext, "Registration failed! " + task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}