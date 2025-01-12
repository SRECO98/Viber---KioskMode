package com.example.kioskappplogikaa.location

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.kioskappplogikaa.R
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private val REQUEST_CAMERA_PERMISSION = 100
    private lateinit var cameraActTemporaryCover: ConstraintLayout
    private lateinit var cameraActPreviewViewCameraContainer: PreviewView
    private lateinit var cameraActButtonCapturePhoto: ImageView
    private lateinit var cameraActImageViewCapturedPhoto: ImageView
    private lateinit var cameraActButtonGoodPhoto: ImageView
    private lateinit var cameraActButtonBadPhoto: ImageView
    private lateinit var cameraActButtonCloseCamera: ImageView

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraProvider: ProcessCameraProvider
    private var camera: Camera? = null
    private lateinit var savedUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        cameraActTemporaryCover = findViewById(R.id.cameraActTemporaryCover)
        cameraActPreviewViewCameraContainer = findViewById(R.id.cameraActPreviewViewCameraContainer)
        cameraActButtonCapturePhoto = findViewById(R.id.cameraActButtonCapturePhoto)
        cameraActImageViewCapturedPhoto = findViewById(R.id.cameraActImageViewCapturedPhoto)
        cameraActButtonGoodPhoto = findViewById(R.id.cameraActButtonGoodPhoto)
        cameraActButtonBadPhoto = findViewById(R.id.cameraActButtonBadPhoto)
        cameraActButtonCloseCamera = findViewById(R.id.cameraActButtonCloseCamera)

        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        // Initialize the ImageCapture instance
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build()

        startCamera()

        cameraActButtonCapturePhoto.setOnClickListener {
            cameraActButtonCapturePhoto.isEnabled = false
            captureAPhoto()
        }
        cameraActButtonGoodPhoto.setOnClickListener {
            //displaying the image inside the chat
            displayImageInChat(savedUri)
        }
        cameraActButtonBadPhoto.setOnClickListener {
            cameraActButtonGoodPhoto.visibility = View.GONE
            cameraActButtonBadPhoto.visibility = View.GONE
            cameraActImageViewCapturedPhoto.visibility = View.GONE
            cameraActTemporaryCover.visibility = View.GONE
            cameraActButtonCapturePhoto.visibility = View.VISIBLE
            cameraActButtonCapturePhoto.isEnabled = true
        }
        cameraActButtonCloseCamera.setOnClickListener {
            finish()
        }
    }

    private fun captureAPhoto() {
        val metadata = ImageCapture.Metadata().apply {

        }
        Log.i("TAG", "Open function 0")
        // Create a file to save the captured image
        val outputFile = File(externalMediaDirs.first(), "${System.currentTimeMillis()}.jpg")

        // Set up the output options
        val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile)
            .setMetadata(metadata)
            .build()

        Log.i("TAG", "Open function 1")

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object: ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.i("TAG", "Open function 2")
                    // Image capture successful
                    savedUri = Uri.fromFile(outputFile)

                    cameraActTemporaryCover.visibility = View.VISIBLE
                    cameraActImageViewCapturedPhoto.visibility = View.VISIBLE
                    cameraActButtonGoodPhoto.visibility = View.VISIBLE
                    cameraActButtonBadPhoto.visibility = View.VISIBLE
                    cameraActButtonCapturePhoto.visibility = View.GONE

                    Glide.with(this@CameraActivity)
                        .load(Uri.parse(savedUri.toString()))
                        .into(cameraActImageViewCapturedPhoto) // Replace with your ImageView reference in the chat layout

                }

                override fun onError(exception: ImageCaptureException) {
                    Log.i("TAG", "Open function 3")
                    // Image capture failed
                    exception.printStackTrace()
                    // Handle the error as needed
                }
            }
        )
    }

    private fun displayImageInChat(imageUri: Uri) {
        Log.i("TAG", "Open function 5")
        // Optionally, you can send the image URI to your chat server or handle it as needed in your chat application
        // sendMessageWithImage(imageUri)

        // Create an Intent to pass the image URI back to InsideChatActivity
        val returnIntent = Intent()
        returnIntent.putExtra("capturedImageUri", imageUri.toString())
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    fun startCamera(){
        if(hasCameraPermission()){
            cameraExecutor = Executors.newSingleThreadExecutor()
            cameraProviderFuture = ProcessCameraProvider.getInstance(this)
            startCameraLogicAndBinding()
        }else{
            requestCameraPermission()
        }
    }

    private fun startCameraLogicAndBinding() {
        val preview = Preview.Builder().build()

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        cameraProviderFuture.addListener({
            try {
                cameraProvider = cameraProviderFuture.get()
                val camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                // Handle exceptions
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))

        preview.setSurfaceProvider(cameraActPreviewViewCameraContainer.getSurfaceProvider())
    }

    fun hasCameraPermission(): Boolean = ContextCompat.checkSelfPermission(this,
        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CAMERA_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraLogicAndBinding()
            } else {
                Toast.makeText(this, "You must give us permission for usingcamera inside your app!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraProvider.unbindAll()
        cameraExecutor.shutdown()
    }
}