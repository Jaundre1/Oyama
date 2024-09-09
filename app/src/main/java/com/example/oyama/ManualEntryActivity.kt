package com.example.oyama

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class ManualEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_entry)

        // Hide the action bar
        supportActionBar?.hide()

        // Set the status bar color
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        // Find the views by ID
        val fleetNumberInput: EditText = findViewById(R.id.fleetNumberInput)
        val regNoInput: EditText = findViewById(R.id.regNoInput)
        val vehicleTypeAutoComplete: AutoCompleteTextView = findViewById(R.id.vehicleTypeAutoComplete)
        val submitButton: Button = findViewById(R.id.newPageButton)

        // Create an ArrayAdapter for the AutoCompleteTextView
        val vehicleTypes = arrayOf("Bus Type 1", "Bus Type 2", "Bus Type 3")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, vehicleTypes)
        vehicleTypeAutoComplete.setAdapter(adapter)

        // Set click listener for the submit button
        submitButton.setOnClickListener {
            val fleetNumber = fleetNumberInput.text.toString()
            val regNo = regNoInput.text.toString()
            val vehicleType = vehicleTypeAutoComplete.text.toString()

            // Handle button click and input values here
            // For example, show a Toast or start another activity
        }
    }
}
