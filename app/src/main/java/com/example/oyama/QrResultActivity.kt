package com.example.oyama

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QrResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_result)

        val qrCodeResult = intent.getStringExtra("QR_RESULT")

        val qrCodeResultTextView: TextView = findViewById(R.id.qrCodeResultTextView)
        qrCodeResultTextView.text = qrCodeResult
    }
}
