package com.example.oyama

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class DepotDetailsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    // Initialize the data holder to temporarily store data
    private val dataHolder = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_depot_details)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("DepotPrefs", MODE_PRIVATE)

        // Hide the action bar
        supportActionBar?.hide()

        // Set the status bar color
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        val selectedDepotTextView: TextView = findViewById(R.id.selectedDepotTextView)
        val scanQRCodeButton: Button = findViewById(R.id.ScanQRCode)
        val manualEntryButton: Button = findViewById(R.id.ManualEntry)
        val doneButton: Button = findViewById(R.id.doneButton) // Reference to the Done button from your XML

        val backArrow: ImageView = findViewById(R.id.backArrow)

        // Retrieve the selected depot from SharedPreferences
        val selectedDepot = sharedPreferences.getString("SELECTED_DEPOT", "No Depot Selected")
        selectedDepotTextView.text = selectedDepot

        // Store the selected depot in the data holder as the first item
        dataHolder.append("Depot Selected: $selectedDepot\n")

        // Set click listener for Scan QR Code button
        scanQRCodeButton.setOnClickListener {
            val intent = Intent(this, QrScannerActivity::class.java)
            startActivity(intent)
        }

        // Set click listener for Manual Entry button
        manualEntryButton.setOnClickListener {
            val intent = Intent(this, ManualEntryActivity::class.java)
            startActivity(intent)
        }

        // Set click listener for back arrow button
        backArrow.setOnClickListener {
            onBackPressed()
        }

        // Set click listener for Done button to process, clear data, and reset the app state
        doneButton.setOnClickListener {
            sendDataToAWS()  // Send data to AWS or process it
            clearDataHolder() // Clear the data holder after submission
            resetApp() // Reset the app state after done
        }
    }

    // Function to process and send data to AWS (example implementation)
    private fun sendDataToAWS() {
        val dataToSend = dataHolder.toString()
        // Code to send dataToSend to AWS or process it as CSV, etc.
    }

    // Function to clear the data holder after "Done"
    private fun clearDataHolder() {
        dataHolder.clear()  // Clears the temporary data
    }

    // Function to reset the app state (clear any other data and reset UI)
    private fun resetApp() {
        // Clear any shared preferences or other persistent data if needed
        sharedPreferences.edit().clear().apply()  // Clears SharedPreferences
        // Optionally, reset UI components if needed or reload the activity
        val intent = Intent(this, DepotDetailsActivity::class.java)
        startActivity(intent)
        finish()  // Finish the current activity so it starts fresh
    }

    // You can create a method to retrieve the data from the holder if needed
    fun getDataFromHolder(): String {
        return dataHolder.toString()
    }
}
