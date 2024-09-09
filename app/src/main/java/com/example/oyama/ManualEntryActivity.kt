package com.example.oyama

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class ManualEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_entry)

        // Hide the action bar
        supportActionBar?.hide()

        // Set the status bar color
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        // Find the button by ID
        val newPageButton: Button = findViewById(R.id.newPageButton)

        // Set click listener for the button
        newPageButton.setOnClickListener {
            // Handle button click here
            // For example, show a Toast or start another activity
        }
    }
}
