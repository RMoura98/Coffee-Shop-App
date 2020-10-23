package com.feup.cmov.acme_client

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    val MIN_PASSWORD_LENGTH = 6 // val = const

    lateinit var emailInput: EditText
    lateinit var passwordInput: EditText
    lateinit var loginButton: Button
    lateinit var createAccountButton: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewInitializations()
    }

    private fun viewInitializations() {
        emailInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        createAccountButton = findViewById(R.id.createNewAccountButton)
    }

    private fun validateInput(): Boolean {
        var emailText = emailInput.text.toString()
        var passwordText = passwordInput.text.toString()

        if (emailText == "") {
            emailInput.error = "Please Enter Email"
            return false
        }
        if (passwordText == "") {
            passwordInput.error = "Please Enter Password"
            return false
        }

        // checking the proper email format
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            emailInput.error = "Please Enter Valid Email"
            return false
        }

        // checking minimum password Length
        if (passwordText.length < MIN_PASSWORD_LENGTH) {
            passwordInput.error = "Password Length must be more than " + MIN_PASSWORD_LENGTH + "characters"
            return false
        }
        return true
    }

    fun clickLogIn(v: View) {
        if (validateInput()) {
            var emailText = emailInput.text.toString()
            var passwordText = passwordInput.text.toString()

            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
            // call API

            //if good change to new window
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun clickCreateAccount(v: View) {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }
}