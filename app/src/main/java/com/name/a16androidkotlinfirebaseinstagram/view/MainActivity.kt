package com.name.a16androidkotlinfirebaseinstagram.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.name.a16androidkotlinfirebaseinstagram.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var userMail : String
    private lateinit var password : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth= Firebase.auth

       if (auth.currentUser != null) {
           val intent =Intent(this, FeedActivity::class.java)
           startActivity(intent)
           finish()
       }


    }






    fun signInClicked (view: View) {
        userMail = binding.emailText.text.toString()
        password= binding.passwordText.text.toString()

        if (userMail.equals("") || password.equals("")){
            Toast.makeText(this,"Enter password or Email", Toast.LENGTH_LONG).show()
        } else {
            auth.signInWithEmailAndPassword(userMail, password).addOnSuccessListener {

                val intent=Intent(this@MainActivity, FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }






    }

    fun signUpClicked (view: View) {

        userMail = binding.emailText.text.toString()
        password= binding.passwordText.text.toString()

        if (userMail.equals("") || password.equals("")){
            Toast.makeText(this,"Enter mail and password", Toast.LENGTH_LONG).show()

        } else {
            auth.createUserWithEmailAndPassword(userMail, password)
                .addOnSuccessListener {
                    val intent = Intent(this@MainActivity, FeedActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
        }


    }




}