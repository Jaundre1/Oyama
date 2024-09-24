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
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class QrResultActivity : AppCompatActivity() {

    private val buttonStates = mutableMapOf<Int, Boolean?>()
    private var qrData: String? = null
    private var fleetNumber: String? = null
    private var vehicleType: String? = null
    private var vehicleBrand: String? = null
    private var reason: String? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_result)

        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("DepotPrefs", MODE_PRIVATE)

        // Get data from intent
        qrData = intent.getStringExtra("QR_DATA")
        fleetNumber = intent.getStringExtra("FLEET_NUMBER")
        vehicleType = intent.getStringExtra("VEHICLE_TYPE")
        vehicleBrand = intent.getStringExtra("VEHICLE_BRAND")
        reason = intent.getStringExtra("REASON")

        val textView = findViewById<TextView>(R.id.qrDataTextView)

        // Display concatenated data, including fleet number and other details
        val concatenatedData = """
            Fleet Number: $fleetNumber
            Vehicle Type: $vehicleType
            Vehicle Brand: $vehicleBrand
            Reason: $reason
            Answers: ${buttonStates.values.joinToString(", ") { if (it == true) "1" else "0" }}
            QR Data: ${qrData?.replace("\n", ",") ?: "No QR data available"}
        """.trimIndent()

        textView.text = concatenatedData

        // Setup buttons
        setupButtons()

        // Submit button
        val submitButton: Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            if (validateSelections()) {
                sendDataToLambda()  // Send the results to the Lambda function
                showSuccessDialog()
            } else {
                Toast.makeText(this, "Please answer all the questions!", Toast.LENGTH_SHORT).show()
            }
        }
    }

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
            buttonStates[index] = null // Use null to represent unanswered
            yesButton.setOnClickListener { onButtonClicked(yesButton, noButton, index, true) }
            noButton.setOnClickListener { onButtonClicked(noButton, yesButton, index, false) }
        }
    }

    private fun onButtonClicked(selectedButton: Button, unselectedButton: Button, index: Int, isYes: Boolean) {
        setButtonColors(selectedButton, unselectedButton)
        buttonStates[index] = isYes // true for Yes, false for No
        Log.d("ButtonClicked", "Button at index $index clicked. State updated to ${buttonStates[index]}.")
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
        return buttonStates.values.all { it != null } // Ensure all questions have been answered
    }

    private fun sendDataToLambda() {
        val results = buttonStates.values.map { if (it == true) "1" else "0" }

        // Retrieve the selected depot from SharedPreferences
        val selectedDepot = sharedPreferences.getString("SELECTED_DEPOT", "No Depot Selected")

        // Create JSON data
        val jsonData = JSONObject().apply {
            put("answers", results) // Include answers in JSON
            put("qrData", qrData?.replace("\n", ",")) // Include formatted QR data
            put("fleetNumber", fleetNumber ?: "") // Include fleet number
            put("vehicleType", vehicleType ?: "") // Include vehicle type
            put("vehicleBrand", vehicleBrand ?: "") // Include vehicle brand
            put("reason", reason ?: "") // Include reason
            put("selectedDepot", selectedDepot) // Include selected depot
        }

        val url = URL("https://7g703ccxk8.execute-api.eu-north-1.amazonaws.com/prod/data")
        Thread {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST" // Use POST to send data
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")
                connection.connectTimeout = 10000
                connection.readTimeout = 10000

                connection.outputStream.use { outputStream ->
                    outputStream.write(jsonData.toString().toByteArray())
                }

                val responseCode = connection.responseCode
                runOnUiThread {
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Toast.makeText(this, "Data sent successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to send data, Response Code: $responseCode", Toast.LENGTH_SHORT).show()
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

    private fun showSuccessDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_success, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val doneButton: Button = dialogView.findViewById(R.id.doneButton)
        doneButton.setOnClickListener {
            dialog.dismiss()
            navigateToDepotDetails()
        }

        dialog.show()
    }

    private fun navigateToDepotDetails() {
        val selectedDepot = intent.getStringExtra("SELECTED_DEPOT")
        val returnIntent = Intent(this, DepotDetailsActivity::class.java).apply {
            putExtra("SELECTED_DEPOT", selectedDepot)
        }
        startActivity(returnIntent)
        finish()
    }
}
