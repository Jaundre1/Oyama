package com.example.oyama

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class QrResultActivity : AppCompatActivity() {

    private val buttonStates = mutableMapOf<Int, Boolean?>()  // Tracks button states
    private var qrData: String? = null
    private var fleetNumber: String? = null
    private var vehicleType: String? = null
    private var vehicleBrand: String? = null
    private var reason: String? = null
    private var registrationNumber: String? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_result)

        // Hide the action bar and set status bar color
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("DepotPrefs", MODE_PRIVATE)

        // Get data from the intent
        qrData = intent.getStringExtra("QR_DATA")
        fleetNumber = intent.getStringExtra("FLEET_NUMBER")
        vehicleType = intent.getStringExtra("VEHICLE_TYPE")
        vehicleBrand = intent.getStringExtra("VEHICLE_BRAND")
        reason = intent.getStringExtra("REASON")
        registrationNumber = intent.getStringExtra("REGISTRATION_NUMBER")

        // Display data based on the source (QR or manual)
        displayRelevantData()

        // Setup buttons and submit logic
        setupButtons()
        setupSubmitButton()
    }

    // Function to display relevant data in TextView based on the source of data (QR or manual)
    private fun displayRelevantData() {
        val textView = findViewById<TextView>(R.id.qrDataTextView)

        // If QR code data is available, display only the first item (either split by newline or comma)
        if (qrData != null) {
            // Split the QR data by either commas or newlines and display only the first item
            val firstItem = qrData?.split(Regex("[,\n]"))?.firstOrNull() ?: "No QR data available"
            textView.text = "Fleet Number: $firstItem"
        }
        // Otherwise, display the Fleet Number for manual entries
        else if (fleetNumber != null) {
            textView.text = "Fleet Number: $fleetNumber"
        }
    }



    // Setup button click listeners
    private fun setupButtons() {
        val buttons = listOf(
            Pair(findViewById<Button>(R.id.yesButton1), findViewById<Button>(R.id.noButton1)),
            Pair(findViewById<Button>(R.id.yesButton2), findViewById<Button>(R.id.noButton2)),
            Pair(findViewById<Button>(R.id.yesButton3), findViewById<Button>(R.id.noButton3)),
            Pair(findViewById<Button>(R.id.yesButton4), findViewById<Button>(R.id.noButton4)),
            Pair(findViewById<Button>(R.id.yesButton5), findViewById<Button>(R.id.noButton5))
        )

        buttons.forEachIndexed { index, (yesButton, noButton) ->
            resetButtonColors(yesButton, noButton)
            buttonStates[index] = null  // Unanswered state

            yesButton.setOnClickListener { onButtonClicked(yesButton, noButton, index, true) }
            noButton.setOnClickListener { onButtonClicked(noButton, yesButton, index, false) }
        }
    }

    // Handle button click event
    private fun onButtonClicked(selectedButton: Button, unselectedButton: Button, index: Int, isYes: Boolean) {
        setButtonColors(selectedButton, unselectedButton)
        buttonStates[index] = isYes
        Log.d("ButtonClicked", "Button at index $index clicked. State: ${buttonStates[index]}")
    }

    // Reset button colors to default
    private fun resetButtonColors(yesButton: Button, noButton: Button) {
        yesButton.setBackgroundColor(Color.GRAY)
        noButton.setBackgroundColor(Color.GRAY)
    }

    // Set selected and unselected button colors
    private fun setButtonColors(selectedButton: Button, unselectedButton: Button) {
        selectedButton.setBackgroundColor(Color.parseColor("#B9322C"))
        unselectedButton.setBackgroundColor(Color.GRAY)
    }

    // Setup submit button logic
    private fun setupSubmitButton() {
        val submitButton: Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            if (validateSelections()) {
                sendDataToLambda()  // Send data to Lambda
                showSuccessDialog()  // Show success dialog
            } else {
                Toast.makeText(this, "Please answer all the questions!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Validate if all questions have been answered
    private fun validateSelections(): Boolean {
        return buttonStates.values.all { it != null }
    }

    // Send data to AWS Lambda function
    private fun sendDataToLambda() {
        val results = buttonStates.values.map { if (it == true) 1 else 0 }

        Log.d("SendDataToLambda", "Results: $results")

        // Retrieve selected depot from SharedPreferences
        val selectedDepot = sharedPreferences.getString("SELECTED_DEPOT", "No Depot Selected") ?: ""

        // Create JSON object for Lambda
        val jsonData = JSONObject().apply {
            put("answers", JSONArray(results))
            put("selectedDepot", selectedDepot)

            if (registrationNumber != null) { // Manual entry
                put("registrationNumber", registrationNumber ?: "")
                put("fleetNumber", fleetNumber ?: "")
                put("vehicleType", vehicleType ?: "")
                put("reason", reason ?: "")
                put("vehicleBrand", vehicleBrand ?: "")
                put("qrData", "Manual") // Add "Manual" for manual entries
            } else if (qrData != null) { // QR scanning data
                put("qrData", qrData?.replace("\n", ",") ?: "")
            }
        }

        val url = URL("https://7g703ccxk8.execute-api.eu-north-1.amazonaws.com/prod/data")

        // Send data to Lambda in a background thread
        Thread {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")
                connection.connectTimeout = 10000
                connection.readTimeout = 10000

                connection.outputStream.use { it.write(jsonData.toString().toByteArray()) }

                val responseCode = connection.responseCode
                runOnUiThread {
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Toast.makeText(this, "Data sent successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to send data. Response Code: $responseCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    // Show success dialog after submission
    private fun showSuccessDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_success, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val doneButton: Button = dialogView.findViewById(R.id.doneButton)
        doneButton.setOnClickListener {
            dialog.dismiss()
            navigateToDepotDetails()  // Navigate to depot details
        }

        dialog.show()
    }

    // Navigate to DepotDetailsActivity
    private fun navigateToDepotDetails() {
        val selectedDepot = sharedPreferences.getString("SELECTED_DEPOT", "")
        val returnIntent = Intent(this, DepotDetailsActivity::class.java).apply {
            putExtra("SELECTED_DEPOT", selectedDepot)
        }
        startActivity(returnIntent)
        finish()
    }
}
