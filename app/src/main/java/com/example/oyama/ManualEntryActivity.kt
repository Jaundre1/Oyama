package com.example.oyama

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class ManualEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_entry)

        // Fleet number and registration number inputs
        val fleetNumberEditText: EditText = findViewById(R.id.fleetNumberEditText)
        val registrationNumberEditText: EditText = findViewById(R.id.registrationNumberEditText)

        // Vehicle Type Spinner
        val spinnerVehicleType: Spinner = findViewById(R.id.spinnerVehicleType)

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
