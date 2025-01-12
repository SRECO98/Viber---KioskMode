package com.example.kioskappplogikaa

import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MyDeviceAdminReceiver : DeviceAdminReceiver() {

    companion object {
        fun getComponentName(context: Context): ComponentName{
            return ComponentName(context.applicationContext, MyDeviceAdminReceiver::class.java)
        }
    }

    /*override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)

        // Device admin is enabled
        Toast.makeText(context, "Device admin enabled", Toast.LENGTH_SHORT).show()

        // Grant permission for Google Maps to run
        val packageName = "com.google.android.apps.maps"
        grantPermissionForPackage(context, packageName)

        // Check if the package is allowed to be suspended (log the result)
        val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val adminComponentName = getComponentName(context)
        val isSuspended = devicePolicyManager.isPackageSuspended(adminComponentName, packageName)
        Log.i("TAG", "Is $packageName suspended? $isSuspended")
    }

    private fun grantPermissionForPackage(context: Context, packageName: String) {
        val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val adminComponentName = getComponentName(context)

        // Allow the specified package to run
        devicePolicyManager.setPackagesSuspended(adminComponentName, arrayOf(packageName), false)
    }*/
}