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

        // Find views
        val selectedDepotTextView: TextView = findViewById(R.id.selectedDepotTextView)
        val scanQRCodeButton: Button = findViewById(R.id.ScanQRCode)
        val manualEntryButton: Button = findViewById(R.id.ManualEntry)
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
    }

    fun getDataFromHolder(): String {
        return dataHolder.toString()
    }
}
