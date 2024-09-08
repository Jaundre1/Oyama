package com.example.oyama

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity

class ConfirmationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        // Retrieve the error flag from the intent
        val isError = intent.getBooleanExtra("ERROR_FLAG", false)

        // Set up the TextView
        val confirmationTextView: TextView = findViewById(R.id.confirmationTextView)

        // Display an error message if needed
        if (isError) {
            confirmationTextView.text = "Error"
        } else {
            // Display success message if there is no error
            confirmationTextView.text = "QR Code has been successfully confirmed!"
        }
    }
}
