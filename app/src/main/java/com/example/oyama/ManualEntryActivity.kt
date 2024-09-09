package com.example.oyama

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.oyama.R


class ManualEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_entry)


        // Vehicle Type Spinner
        val spinnerVehicleType: Spinner = findViewById(R.id.spinnerVehicleType)
        ArrayAdapter.createFromResource(
            this,
            R.array.vehicle_types,  // Use the string-array defined in strings.xml
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerVehicleType.adapter = adapter
        }

        // You can add more logic here for handling the submit button, etc.
    }
}
