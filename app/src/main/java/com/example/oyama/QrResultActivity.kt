package com.example.oyama

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

class QrResultActivity : AppCompatActivity() {

    // To track the selected states for each question
    private val buttonStates = mutableMapOf<Int, Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_result)

        // Hide the ActionBar and set the status bar color
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        // Retrieve QR data from the Intent
        val qrData = intent.getStringExtra("QR_DATA")

        // Hide the action bar
        supportActionBar?.hide()

        // Set the status bar color
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        // Display the first part of the QR data
        val textView = findViewById<TextView>(R.id.qrDataTextView)
        qrData?.let {
            val dataParts = it.split(";")
            val firstPart = dataParts.getOrNull(0) ?: "No data available"
            textView.text = firstPart
        } ?: run {
            textView.text = "No QR code data available"
        }

        // Initialize buttons and their corresponding "yes/no" pair
        val buttons = listOf(
            Pair(findViewById<Button>(R.id.yesButton1), findViewById<Button>(R.id.noButton1)),
            Pair(findViewById<Button>(R.id.yesButton2), findViewById<Button>(R.id.noButton2)),
            Pair(findViewById<Button>(R.id.yesButton3), findViewById<Button>(R.id.noButton3)),
            Pair(findViewById<Button>(R.id.yesButton4), findViewById<Button>(R.id.noButton4)),
            Pair(findViewById<Button>(R.id.yesButton5), findViewById<Button>(R.id.noButton5))
        )

        // Set initial button colors and initialize state map
        buttons.forEachIndexed { index, (yesButton, noButton) ->
            resetButtonColors(yesButton, noButton)
            buttonStates[index] = false // Mark as unselected initially
        }

        // Set click listeners for yes/no buttons
        buttons.forEachIndexed { index, (yesButton, noButton) ->
            yesButton.setOnClickListener {
                setButtonColors(yesButton, noButton)
                buttonStates[index] = true // Mark "yes" selected
            }
            noButton.setOnClickListener {
                setButtonColors(noButton, yesButton)
                buttonStates[index] = true // Mark "no" selected
            }
        }

        // Set click listener for the Submit button
        val submitButton: Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            if (validateSelections()) {
                showSuccessDialog()
            } else {
                Toast.makeText(this, "Please answer all the questions!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Resets both buttons to gray
    private fun resetButtonColors(yesButton: Button, noButton: Button) {
        yesButton.setBackgroundColor(Color.GRAY)
        noButton.setBackgroundColor(Color.GRAY)
    }

    // Sets the specified 'selected' button to red and the 'unselected' button to gray
    private fun setButtonColors(selectedButton: Button, unselectedButton: Button) {
        selectedButton.setBackgroundColor(Color.parseColor("#B9322C"))
        unselectedButton.setBackgroundColor(Color.GRAY)
    }

    // Validate if all selections are made (Yes or No for each question)
    private fun validateSelections(): Boolean {
        // Check if all buttons are selected (true means selected)
        return buttonStates.all { it.value }
    }

    // Shows the success dialog
    private fun showSuccessDialog() {
        // Inflate the dialog layout
        val dialogView = layoutInflater.inflate(R.layout.dialog_success, null)

        // Create and show the dialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Get reference to the Done button and set its listener
        val doneButton: Button = dialogView.findViewById(R.id.doneButton)
        doneButton.setOnClickListener {
            dialog.dismiss() // Close the dialog

            // Go back to the DepotDetailsActivity with the selected depot
            val selectedDepot = intent.getStringExtra("SELECTED_DEPOT")
            val returnIntent = Intent(this, DepotDetailsActivity::class.java).apply {
                putExtra("SELECTED_DEPOT", selectedDepot)
            }
            startActivity(returnIntent)
            finish() // Close the current activity
        }

        dialog.show()
    }
}
