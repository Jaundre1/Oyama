package com.example.oyama

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class QrResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_result)

        // Hide the ActionBar and set the status bar color
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        // Initialize buttons
        val buttons = listOf(
            Pair(findViewById<Button>(R.id.yesButton1), findViewById<Button>(R.id.noButton1)),
            Pair(findViewById<Button>(R.id.yesButton2), findViewById<Button>(R.id.noButton2)),
            Pair(findViewById<Button>(R.id.yesButton3), findViewById<Button>(R.id.noButton3)),
            Pair(findViewById<Button>(R.id.yesButton4), findViewById<Button>(R.id.noButton4)),
            Pair(findViewById<Button>(R.id.yesButton5), findViewById<Button>(R.id.noButton5))
        )

        // Set initial button colors
        buttons.forEach { (yesButton, noButton) ->
            resetButtonColors(yesButton, noButton)
        }

        // Set click listeners
        buttons.forEach { (yesButton, noButton) ->
            yesButton.setOnClickListener { setButtonColors(yesButton, noButton) }
            noButton.setOnClickListener { setButtonColors(noButton, yesButton) }
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
}
