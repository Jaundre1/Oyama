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
import java.io.File

class QrResultActivity : AppCompatActivity() {

    // To track the selected states for each question
    private val buttonStates = mutableMapOf<Int, Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_result)

        // Hide the ActionBar and set the status bar color to white
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        // Display the fleet number from the file
        val textView = findViewById<TextView>(R.id.qrDataTextView)
        val file = File(filesDir, "temporary_data.txt")

        if (file.exists()) {
            val lines = file.readLines()
            if (lines.isNotEmpty()) {
                val firstLine = lines[0]
                val parts = firstLine.split(";")
                if (parts.isNotEmpty()) {
                    textView.text = parts[0] // Display only the fleet number
                } else {
                    textView.text = "No data available"
                }
            } else {
                textView.text = "No data available"
            }
        } else {
            textView.text = "No data available"
        }

        // Initialize buttons (Yes/No pairs for questions)
        val buttons = listOf(
            Pair(findViewById<Button>(R.id.yesButton1), findViewById<Button>(R.id.noButton1)),
            Pair(findViewById<Button>(R.id.yesButton2), findViewById<Button>(R.id.noButton2)),
            Pair(findViewById<Button>(R.id.yesButton3), findViewById<Button>(R.id.noButton3)),
            Pair(findViewById<Button>(R.id.yesButton4), findViewById<Button>(R.id.noButton4)),
            Pair(findViewById<Button>(R.id.yesButton5), findViewById<Button>(R.id.noButton5))
        )

        // Initialize button states and colors
        buttons.forEachIndexed { index, (yesButton, noButton) ->
            resetButtonColors(yesButton, noButton)
            buttonStates[index] = false // Initially, all questions are unselected
        }

        // Set click listeners for yes/no buttons
        buttons.forEachIndexed { index, (yesButton, noButton) ->
            yesButton.setOnClickListener {
                setButtonColors(yesButton, noButton)
                buttonStates[index] = true // "Yes" is selected
            }
            noButton.setOnClickListener {
                setButtonColors(noButton, yesButton)
                buttonStates[index] = true // "No" is selected
            }
        }

        // Set click listener for the Submit button
        val submitButton: Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            if (validateSelections()) {
                // Clear temporary data
                file.delete() // Deletes the file

                showSuccessDialog()
            } else {
                Toast.makeText(this, "Please answer all the questions!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Resets both buttons to gray (initial state)
    private fun resetButtonColors(yesButton: Button, noButton: Button) {
        yesButton.setBackgroundColor(Color.GRAY)
        noButton.setBackgroundColor(Color.GRAY)
    }

    // Sets the selected button to red and the unselected one to gray
    private fun setButtonColors(selectedButton: Button, unselectedButton: Button) {
        selectedButton.setBackgroundColor(Color.parseColor("#B9322C"))
        unselectedButton.setBackgroundColor(Color.GRAY)
    }

    // Validate if all questions have been answered (either Yes or No)
    private fun validateSelections(): Boolean {
        return buttonStates.values.all { it }
    }

    // Shows a success dialog when all questions are answered
    private fun showSuccessDialog() {
        // Inflate custom dialog layout
        val dialogView = layoutInflater.inflate(R.layout.dialog_success, null)

        // Create and show the dialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false) // Make it non-cancelable
            .create()

        // Handle the Done button in the dialog
        val doneButton: Button = dialogView.findViewById(R.id.doneButton)
        doneButton.setOnClickListener {
            dialog.dismiss() // Dismiss the dialog

            // Retrieve selected depot data and navigate back to DepotDetailsActivity
            val selectedDepot = intent.getStringExtra("SELECTED_DEPOT")
            val returnIntent = Intent(this, DepotDetailsActivity::class.java).apply {
                putExtra("SELECTED_DEPOT", selectedDepot)
            }
            startActivity(returnIntent)
            finish() // Finish the current activity
        }

        dialog.show()
    }
}
