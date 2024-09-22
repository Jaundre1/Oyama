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
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class QrResultActivity : AppCompatActivity() {

    private val buttonStates = mutableMapOf<Int, Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_result)

        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        val textView = findViewById<TextView>(R.id.qrDataTextView)
        val file = File(filesDir, "temporary_data.txt")

        if (file.exists()) {
            val lines = file.readLines()
            if (lines.isNotEmpty()) {
                val firstLine = lines[0]
                val parts = firstLine.split(";")
                if (parts.isNotEmpty()) {
                    textView.text = parts[0]
                } else {
                    textView.text = "No data available"
                }
            } else {
                textView.text = "No data available"
            }
        } else {
            textView.text = "No data available"
        }

        val buttons = listOf(
            Pair(findViewById<Button>(R.id.yesButton1), findViewById<Button>(R.id.noButton1)),
            Pair(findViewById<Button>(R.id.yesButton2), findViewById<Button>(R.id.noButton2)),
            Pair(findViewById<Button>(R.id.yesButton3), findViewById<Button>(R.id.noButton3)),
            Pair(findViewById<Button>(R.id.yesButton4), findViewById<Button>(R.id.noButton4)),
            Pair(findViewById<Button>(R.id.yesButton5), findViewById<Button>(R.id.noButton5))
        )

        buttons.forEachIndexed { index, (yesButton, noButton) ->
            resetButtonColors(yesButton, noButton)
            buttonStates[index] = false
        }

        buttons.forEachIndexed { index, (yesButton, noButton) ->
            yesButton.setOnClickListener {
                setButtonColors(yesButton, noButton)
                buttonStates[index] = true
            }
            noButton.setOnClickListener {
                setButtonColors(noButton, yesButton)
                buttonStates[index] = true
            }
        }

        val submitButton: Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            if (validateSelections()) {
                writeResultsToFile(file) // Write results to the file
                file.delete() // Deletes the temporary file
                showSuccessDialog()
            } else {
                Toast.makeText(this, "Please answer all the questions!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resetButtonColors(yesButton: Button, noButton: Button) {
        yesButton.setBackgroundColor(Color.GRAY)
        noButton.setBackgroundColor(Color.GRAY)
    }

    private fun setButtonColors(selectedButton: Button, unselectedButton: Button) {
        selectedButton.setBackgroundColor(Color.parseColor("#B9322C"))
        unselectedButton.setBackgroundColor(Color.GRAY)
    }

    private fun validateSelections(): Boolean {
        return buttonStates.values.all { it }
    }

    private fun showSuccessDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_success, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val doneButton: Button = dialogView.findViewById(R.id.doneButton)
        doneButton.setOnClickListener {
            dialog.dismiss()

            val selectedDepot = intent.getStringExtra("SELECTED_DEPOT")
            val returnIntent = Intent(this, DepotDetailsActivity::class.java).apply {
                putExtra("SELECTED_DEPOT", selectedDepot)
            }
            startActivity(returnIntent)
            finish()
        }

        dialog.show()
    }

    // Write results to a file
    private fun writeResultsToFile(file: File) {
        try {
            FileWriter(file, true).use { writer ->
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val currentDateTime = dateFormat.format(Date())
                val results = buttonStates.map { if (it.value) "1" else "0" }.joinToString(",")
                writer.write("Date: $currentDateTime, Answers: [$results]\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error writing to file", Toast.LENGTH_SHORT).show()
        }
    }
}
