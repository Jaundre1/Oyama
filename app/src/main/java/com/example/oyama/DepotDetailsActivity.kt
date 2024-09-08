package com.example.oyama

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class DepotDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_depot_details)

        // Hide the action bar
        supportActionBar?.hide()

        // Set the status bar color
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        val selectedDepotTextView: TextView = findViewById(R.id.selectedDepotTextView)
        val scanQRCodeButton: Button = findViewById(R.id.ScanQRCode)
        val manualEntryButton: Button = findViewById(R.id.ManualEntry)

        // Get the selected depot from the intent
        val selectedDepot = intent.getStringExtra("SELECTED_DEPOT")
        selectedDepotTextView.text = selectedDepot

        // Set click listener for Scan QR Code button
        scanQRCodeButton.setOnClickListener {
            val intent = Intent(this, QrScannerActivity::class.java)
            startActivity(intent)
        }

        // Set click listener for Manual Entry button
        manualEntryButton.setOnClickListener {
            // Handle manual entry button click here
            // Example: show a Toast or start another activity
        }
    }
}
