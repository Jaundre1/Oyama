package com.example.oyama

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

class SendDataActivity : AppCompatActivity() {

    private lateinit var answers: List<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_result)

        // Submit button to send data
        val submitButton: Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            sendDataToAWS()
        }
    }

    private fun readDataFromFile(): List<Int> {
        val inputStream: FileInputStream = openFileInput("temporary_data.txt") // Replace with your file name
        val reader = BufferedReader(InputStreamReader(inputStream))
        val data = mutableListOf<Int>()

        reader.use {
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                // Assuming each line in your file is an integer (0 or 1)
                data.add(line?.toInt() ?: 0) // Add default value if parsing fails
            }
        }
        return data
    }

    private fun sendDataToAWS() {
        answers = readDataFromFile()

        if (answers.size != 5) {
            Toast.makeText(this, "Please answer all questions", Toast.LENGTH_SHORT).show()
            return
        }

        val timestamp = getCurrentTimestamp()
        val dataModel = DataModel(answers, timestamp)

        val apiService = ApiClient.apiService
        apiService.sendData(dataModel).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@SendDataActivity, "Data sent successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SendDataActivity, "Failed to send data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@SendDataActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getCurrentTimestamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}
