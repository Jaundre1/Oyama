package com.example.oyama

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class SecondaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondary)

        // Hide the action bar
        supportActionBar?.hide()

        // Set the status bar color
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        val bloemfonteinButton: Button = findViewById(R.id.Bloemfontein)
        val capetownButton: Button = findViewById(R.id.Capetown)
        val durbanButton: Button = findViewById(R.id.Durban)
        val pretoriaButton: Button = findViewById(R.id.Pretoria)

        val depotButtons = mapOf(
            bloemfonteinButton to "Bloemfontein Depot",
            capetownButton to "Cape Town Depot",
            durbanButton to "Durban Depot",
            pretoriaButton to "Pretoria Depot"
        )

        depotButtons.forEach { (button, depotName) ->
            button.setOnClickListener {
                val intent = Intent(this, DepotDetailsActivity::class.java)
                intent.putExtra("SELECTED_DEPOT", depotName)
                startActivity(intent)
            }
        }
    }
}
