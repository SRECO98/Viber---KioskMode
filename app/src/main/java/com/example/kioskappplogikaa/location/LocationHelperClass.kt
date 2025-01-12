package com.example.kioskappplogikaa.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.kioskappplogikaa.inside_chat.Util
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task

class LocationHelperClass(private val activity: Activity) {

    val MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION: Int = 0
    val ERROR_DIALOG_RESPONSE_CODE = 1
    val REQUEST_CHECK_SETTINGS = 1

    //staviti logiku u InsideChat da pocne hvatati lokaciju a na press na lokaciju neka
    // samo uzme zadnje kordinate i posalje u chat, kontam ovo je najbolji nacin.
    lateinit var builder: LocationSettingsRequest.Builder
    lateinit var client: SettingsClient
    lateinit var task: Task<LocationSettingsResponse>
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var permissionRequested = false
    var settingsChangeRequested = false

    fun openGoogleMapsAndSendLocation(){
        //
    }

    fun getLocationUpdates(){
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                for (location in locationResult.locations){
                    displayLocationData(location)
                }
            }
            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                super.onLocationAvailability(locationAvailability)

                if(locationAvailability.isLocationAvailable){
                    Toast.makeText(activity.applicationContext, "You turned location data On!", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(activity.applicationContext, "You turned location data Off!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun checkAvailabilityOfGooglePlayServices() : Boolean{
        val result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity.applicationContext)

        if(result == ConnectionResult.SUCCESS){ //if it exist set it to true.
            return true
        }else{ //if it is not installed on phone then we will get system message what is problem.
            GoogleApiAvailability.getInstance().getErrorDialog(activity, result, ERROR_DIALOG_RESPONSE_CODE)?.show()
            return false
        }
    }

    fun onActivityResume(fusedLocationProviderClient: FusedLocationProviderClient){
        startLocationUpdates(fusedLocationProviderClient = fusedLocationProviderClient)
    }

    fun onActivityPause(){
        stopLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates(fusedLocationProviderClient: FusedLocationProviderClient){
        this.fusedLocationProviderClient = fusedLocationProviderClient
        if(checkPermissionForLocation()){
            Log.i("TAG", "Function startLocation started.")
            //it is happening in this thread when its null.

            locationRequest = LocationRequest.Builder(1000L)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .build()

            builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            client = LocationServices.getSettingsClient(activity)
            task = client.checkLocationSettings(builder.build())

            task.addOnSuccessListener { //location is turned on.
                Log.i("TAG", "Location is enabled.")
                getLastKnownLocation()
            }


            task.addOnFailureListener { exception ->  //when location is not turned on in user settings, we need to change it.
                Log.i("TAG", "Location is not enabled.")

                if(exception is ResolvableApiException){ //if type of exception is ResolvableApiException
                    try{
                        if(!settingsChangeRequested){
                            val resolvable = exception as ResolvableApiException
                            resolvable.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS); //asking user to turn on location ?
                            settingsChangeRequested = true
                        }else{
                            Toast.makeText(activity.applicationContext, "You need to activate location in settings to get data.", Toast.LENGTH_LONG).show()
                        }
                    }catch (sendException: IntentSender.SendIntentException){
                        Log.i("TAG", "Error from catch locationhere is no permissionRequest: ${sendException.message}")
                    }
                }
            }

            this.fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }else{
            if(!permissionRequested){ //we will ask for permission only once.
                //ask for permission to get location if its not granted
                requestPermissionForFineLocation()
                permissionRequested = true
            }else{
                Toast.makeText(activity.applicationContext, "There is no permission for getting location data.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun stopLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(){
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener { location ->
            if(location != null){ //last known location in location.
                displayLocationData(location = location)
            }else{
                Toast.makeText(activity.applicationContext, "There is no last known location.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissionForLocation(): Boolean{
        return ContextCompat.checkSelfPermission(activity.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun displayLocationData(location: Location){
        Util.currentLastLatitude = location.latitude
        Util.currentLastLongitude = location.longitude
        Util.timeOfGettingLastData = location.time

        Log.i("TAG", "Location data: $Util.currentLastLatitude  $Util.currentLastLongitude  $Util.timeOfGettingLastData")
    }

    private fun requestPermissionForFineLocation(){
        ActivityCompat.requestPermissions(activity, arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION)
    }
}