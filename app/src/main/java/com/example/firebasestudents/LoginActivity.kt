package com.example.firebasestudents

import android.R.attr
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import com.google.firebase.auth.AuthResult

import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnCompleteListener

import android.R.attr.password
import android.R.attr.password
import android.content.Intent


class LoginActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)

        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

        loginButton.setOnClickListener{
            val emailValue = email.text.toString()
            val passwordValue = password.text.toString()

            if(emailValue.isNullOrEmpty() || passwordValue.isNullOrEmpty())
            {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
            else{
                login(emailValue, passwordValue)
            }
        }

        registerButton.setOnClickListener {
            val emailValue = email.text.toString()
            val passwordValue = password.text.toString()

            if(emailValue.isNullOrEmpty() || passwordValue.isNullOrEmpty())
            {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
            else{
                register(emailValue, passwordValue)
            }
        }

    }

    private fun register(emailValue: String, passwordValue: String) {
        mAuth.createUserWithEmailAndPassword(emailValue, passwordValue)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "Registration is successful", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Registration is not successful", Toast.LENGTH_SHORT)
                        .show()
                }

                // ...
            }
    }

    private fun login(emailValue: String, passwordValue: String) {
        mAuth.signInWithEmailAndPassword(emailValue, passwordValue)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    navigateToApp()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "wrong credentials", Toast.LENGTH_SHORT).show()
                }

                // ...
            }
    }

    private fun navigateToApp() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()

        if(loggedIn())
        {
            navigateToApp()
        }


    }

    private fun loggedIn(): Boolean {
        return mAuth.currentUser != null
    }
}