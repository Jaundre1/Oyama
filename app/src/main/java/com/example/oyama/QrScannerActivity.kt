package com.example.oyama

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

class QrScannerActivity : ComponentActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var cameraExecutor: ExecutorService
    private var isProcessingBarcode = AtomicBoolean(false)  // Flag to avoid multiple launches

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scanner)

        // Set the status bar color
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        previewView = findViewById(R.id.previewView)
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Request camera permission
        requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = androidx.camera.core.Preview.Builder().build()
            val barcodeAnalyzer = BarcodeAnalyzer()

            preview.setSurfaceProvider(previewView.surfaceProvider)

            val imageAnalysis = ImageAnalysis.Builder()
                .build()
                .apply {
                    setAnalyzer(cameraExecutor, barcodeAnalyzer)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
        }, ContextCompat.getMainExecutor(this))
    }

    private inner class BarcodeAnalyzer : ImageAnalysis.Analyzer {
        private val scannerOptions = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
        private val scanner = BarcodeScanning.getClient(scannerOptions)

        override fun analyze(image: ImageProxy) {
            if (isProcessingBarcode.get()) {  // If already processing, skip further frames
                image.close()
                return
            }

            val mediaImage = image.image
            if (mediaImage != null) {
                val inputImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
                scanner.process(inputImage)
                    .addOnSuccessListener { barcodes ->
                        if (barcodes.isNotEmpty()) {
                            // Mark that we're processing a barcode
                            isProcessingBarcode.set(true)

                            for (barcode in barcodes) {
                                val rawValue = barcode.rawValue
                                if (rawValue != null) {
                                    // Start the QrResultActivity and pass the QR data
                                    val intent = Intent(this@QrScannerActivity, QrResultActivity::class.java)
                                    intent.putExtra("QR_DATA", rawValue)
                                    startActivity(intent)

                                    // Exit the current activity
                                    finish()

                                    // Break out after processing the first barcode
                                    break
                                }
                            }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@QrScannerActivity, "Error processing barcode", Toast.LENGTH_SHORT).show()
                    }
                    .addOnCompleteListener {
                        image.close() // Important to close the image
                    }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
