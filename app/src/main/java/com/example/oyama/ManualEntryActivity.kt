package com.example.oyama

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class ManualEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_entry)

        // Hide the action bar
        supportActionBar?.hide()

        // Initialize and set up Vehicle Type Spinner
        val spinnerVehicleType: Spinner = findViewById(R.id.spinnerVehicleType)
        ArrayAdapter.createFromResource(
            this,
            R.array.vehicle_types,
            R.layout.spinner_item // Custom layout for spinner item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item) // Custom layout for dropdown view
            spinnerVehicleType.adapter = adapter
        }

        // Initialize and set up Vehicle Brand Spinner
        val spinnerVehicleBrand: Spinner = findViewById(R.id.spinnerVehicleBrand)
        ArrayAdapter.createFromResource(
            this,
            R.array.vehicle_brands,
            R.layout.spinner_item // Custom layout for spinner item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item) // Custom layout for dropdown view
            spinnerVehicleBrand.adapter = adapter
        }

        // Initialize and set up Reason Spinner
        val spinnerReason: Spinner = findViewById(R.id.spinnerReason)
        ArrayAdapter.createFromResource(
            this,
            R.array.reason_types,
            R.layout.spinner_item // Custom layout for spinner item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item) // Custom layout for dropdown view
            spinnerReason.adapter = adapter
        }

        // Find the "Add" button and the EditText for fleet number
        val submitButton: Button = findViewById(R.id.submitButton)
        val fleetNumberEditText: EditText = findViewById(R.id.fleetNumberEditText)

        // Set the click listener for the "Add" button
        submitButton.setOnClickListener {
            // Get the fleet number entered by the user
            val fleetNumber = fleetNumberEditText.text.toString()

            // Get selected values from spinners
            val vehicleType = spinnerVehicleType.selectedItem.toString()
            val vehicleBrand = spinnerVehicleBrand.selectedItem.toString()
            val reason = spinnerReason.selectedItem.toString()

            // Check if the fleet number is not empty
            if (fleetNumber.isNotEmpty()) {
                // Write data to file
                val file = File(filesDir, "temporary_data.txt")
                file.writeText("$fleetNumber;$vehicleType;$vehicleBrand;$reason") // Write all data

                // Create an intent to start QrResultActivity and pass the fleet number and other data
                val intent = Intent(this, QrResultActivity::class.java).apply {
                    putExtra("FLEET_NUMBER", fleetNumber) // Pass fleet number
                    putExtra("VEHICLE_TYPE", vehicleType)
                    putExtra("VEHICLE_BRAND", vehicleBrand)
                    putExtra("REASON", reason)
                    putExtra("IS_MANUAL_ENTRY", true) // Indicate that the entry is manual
                }
                startActivity(intent)
            } else {
                // Show a message if the fleet number is empty
                Toast.makeText(this, "Please enter a fleet number", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
