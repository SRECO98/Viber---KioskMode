package com.example.kioskappplogikaa.location

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.kioskappplogikaa.R
import com.github.barteksc.pdfviewer.PDFView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class PdfActivity : AppCompatActivity() {


    val REQUEST_WRITE_STORAGE_PERMISSION = 1
    private lateinit var pdfActPdfView: PDFView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        pdfActPdfView = findViewById(R.id.pdfActPdfView)
        checkPermissionAndDownloadAndOpenPdfFile()

    }

    private fun checkPermissionAndDownloadAndOpenPdfFile() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            downloadAndOpenPdfFile()
        } else {
            // Request the permission.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_STORAGE_PERMISSION
            )
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ){
                downloadAndOpenPdfFile()
            }
        }
    }

    private fun downloadAndOpenPdfFile(){
        // URL of the PDF file to be downloaded
        val pdfUrl = "https://example.com/path/to/your/pdf/file.pdf"

        // Directory where the PDF will be saved locally
        val downloadDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        // Local file to save the PDF
        val localFile = File(downloadDirectory, "sample.pdf")

        GlobalScope.launch(Dispatchers.IO) {
            try {
                // Download the PDF from the URL
                val url = URL(pdfUrl)
                val connection = url.openConnection()
                val inputStream = connection.getInputStream()
                val outputStream = FileOutputStream(localFile)

                val buffer = ByteArray(1024)
                var bytesRead: Int

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                inputStream.close()
                outputStream.close()

                // Load and display the downloaded PDF file
                runOnUiThread {
                    pdfActPdfView.fromFile(localFile).load()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}