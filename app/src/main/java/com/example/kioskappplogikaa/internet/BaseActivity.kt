package com.example.kioskappplogikaa.internet

import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kioskappplogikaa.R

open class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityListener {

    private val connectivityReceiver = ConnectivityReceiver(this)
    private lateinit var rotatingCircle: ImageView
    private lateinit var noInternetMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.no_internet_layout)

        // Initialize views
        rotatingCircle = findViewById(R.id.rotatingCircle)
        noInternetMessage = findViewById(R.id.noInternetMessage)

    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(connectivityReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(connectivityReceiver)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            Toast.makeText(this, "Connection established.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Connection lost.", Toast.LENGTH_SHORT).show()
        }
    }

}