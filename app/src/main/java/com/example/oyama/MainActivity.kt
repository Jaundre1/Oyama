package com.example.oyama

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Hide the action bar
        supportActionBar?.hide()

        // Set the status bar color
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        // Set up the selectDepotButton to navigate to SecondaryActivity
        val selectDepotButton: Button = findViewById(R.id.selectDepotButton)
        selectDepotButton.setOnClickListener {
            val intent = Intent(this, SecondaryActivity::class.java)
            startActivity(intent)
        }
    }
}
