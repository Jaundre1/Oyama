package com.example.oyama

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class QrResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_result)

        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        val yesButton1: Button = findViewById(R.id.yesButton1)
        val noButton1: Button = findViewById(R.id.noButton1)
        val yesButton2: Button = findViewById(R.id.yesButton2)
        val noButton2: Button = findViewById(R.id.noButton2)
        val yesButton3: Button = findViewById(R.id.yesButton3)
        val noButton3: Button = findViewById(R.id.noButton3)
        val yesButton4: Button = findViewById(R.id.yesButton4)
        val noButton4: Button = findViewById(R.id.noButton4)
        val yesButton5: Button = findViewById(R.id.yesButton5)
        val noButton5: Button = findViewById(R.id.noButton5)

        fun resetButtonColors(yesButton: Button, noButton: Button) {
            yesButton.setBackgroundColor(Color.GRAY)
            noButton.setBackgroundColor(Color.GRAY)
        }

        fun setButtonColors(yesButton: Button, noButton: Button) {
            yesButton.setBackgroundColor(Color.parseColor("#B9322C"))
            noButton.setBackgroundColor(Color.GRAY)
        }

        resetButtonColors(yesButton1, noButton1)
        resetButtonColors(yesButton2, noButton2)
        resetButtonColors(yesButton3, noButton3)
        resetButtonColors(yesButton4, noButton4)
        resetButtonColors(yesButton5, noButton5)

        yesButton1.setOnClickListener {
            setButtonColors(yesButton1, noButton1)
        }
        noButton1.setOnClickListener {
            setButtonColors(noButton1, yesButton1)
        }
        yesButton2.setOnClickListener {
            setButtonColors(yesButton2, noButton2)
        }
        noButton2.setOnClickListener {
            setButtonColors(noButton2, yesButton2)
        }
        yesButton3.setOnClickListener {
            setButtonColors(yesButton3, noButton3)
        }
        noButton3.setOnClickListener {
            setButtonColors(noButton3, yesButton3)
        }
        yesButton4.setOnClickListener {
            setButtonColors(yesButton4, noButton4)
        }
        noButton4.setOnClickListener {
            setButtonColors(noButton4, yesButton4)
        }
        yesButton5.setOnClickListener {
            setButtonColors(yesButton5, noButton5)
        }
        noButton5.setOnClickListener {
            setButtonColors(noButton5, yesButton5)
        }
    }
}
