package com.example.kioskappplogikaa.internet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class ConnectivityReceiver(private val listener: ConnectivityListener) : BroadcastReceiver() {

    interface ConnectivityListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    override fun onReceive(context: Context, intent: Intent) {
        listener.onNetworkConnectionChanged(isNetworkConnected(context))
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}