package com.example.oyama

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class ManualEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_entry)

        // Hide the action bar
        supportActionBar?.hide()

        // Vehicle Type Spinner
        val spinnerVehicleType: Spinner = findViewById(R.id.spinnerVehicleType)
        ArrayAdapter.createFromResource(
            this,
            R.array.vehicle_types,
            R.layout.spinner_item // Custom layout for spinner item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item) // Custom layout for dropdown view
            spinnerVehicleType.adapter = adapter
        }

        // Reason Spinner
        val spinnerReason: Spinner = findViewById(R.id.spinnerReason)
        ArrayAdapter.createFromResource(
            this,
            R.array.reason_types,
            R.layout.spinner_item // Custom layout for spinner item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item) // Custom layout for dropdown view
            spinnerReason.adapter = adapter
        }
    }
}
