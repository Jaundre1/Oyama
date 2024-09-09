package com.example.oyama

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class ManualEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_entry)

        // Vehicle Type Spinner
        val spinnerVehicleType: Spinner = findViewById(R.id.spinnerVehicleType)

        // Hide the action bar
        supportActionBar?.hide()

        // Set the status bar color
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        // Array of vehicle types
        val vehicleTypes = resources.getStringArray(R.array.vehicle_types)

        // Adapter for spinner
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, // Simple dropdown layout
            vehicleTypes
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // Apply the adapter to the spinner
        spinnerVehicleType.adapter = adapter
    }
}
