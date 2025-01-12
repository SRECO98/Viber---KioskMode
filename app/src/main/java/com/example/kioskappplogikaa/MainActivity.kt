package com.example.kioskappplogikaa

import android.app.admin.DevicePolicyManager
import android.app.admin.SystemUpdatePolicy
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.os.UserManager
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.kioskappplogikaa.chat_list.ChatActivity
import com.example.kioskappplogikaa.databinding.ActivityMainBinding
import com.example.kioskappplogikaa.internet.BaseActivity
import com.google.android.material.button.MaterialButton

class MainActivity : BaseActivity() {

    private lateinit var adminComponentName: ComponentName
    private lateinit var devicePolicyManager: DevicePolicyManager
    private lateinit var binding: ActivityMainBinding

    private lateinit var buttonStartLockMode: MaterialButton
    private lateinit var buttonStopLockMode: MaterialButton
    private lateinit var buttonOpenListOfFriends: MaterialButton
    private lateinit var mainConstLayoutInternetConnectContainer: ConstraintLayout
    private lateinit var mainRotatingCircle: ImageView

    companion object {
        const val LOCK_ACTIVITY_KEY = "com.tcom.kioskmodeapp.MainActivity"
        const val REQUEST_CODE_ENABLE_ADMIN = 1 // You can use any integer value
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        buttonStartLockMode = findViewById(R.id.btStartLockTask)
        buttonStopLockMode = findViewById(R.id.btStopLockTask)
        buttonOpenListOfFriends = findViewById(R.id.buttonOpenListOfFriends)
        mainConstLayoutInternetConnectContainer = findViewById(R.id.mainConstLayoutInternetConnectContainer)
        mainRotatingCircle = findViewById(R.id.mainRotatingCircle)

        adminComponentName = MyDeviceAdminReceiver.getComponentName(this)
        devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

        if (isAdmin()) {
            Toast.makeText(this, "App is admin of device.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "App is not admin of device.", Toast.LENGTH_SHORT).show()
        }

        buttonStartLockMode.setOnClickListener {
            Log.i("TAG", "buttons start, AdmiN: ${isAdmin()}")
            setKioskPolicies(true, isAdmin())
        }
        buttonStopLockMode.setOnClickListener {
            setKioskPolicies(false, isAdmin())
            Log.i("TAG", "buttons stop, AdmiN: ${isAdmin()}")
            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                //if we have activities a, b, c and for example we call A from C activity,
                // it will remove all activity from the stack and left only A taht we are calling
                putExtra(LOCK_ACTIVITY_KEY, false)
                startActivity(this)
            }
        }
        buttonOpenListOfFriends.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }

    }

    private fun isAdmin() = devicePolicyManager.isDeviceOwnerApp(packageName)

    private fun setKioskPolicies(enable: Boolean, isAdmin: Boolean) {
        if(isAdmin){
            Log.i("TAG", "AWWWWWWWW32")
            setRestrictions(enable)
            enableStayOnWhilePluggedIn(enable)
            setUpdatePolicy(enable)
            //setAsHomeApp(enable)
            setKeyGuardEnabled(enable)
        }
        setLockTask(enable, isAdmin)
        setImmersiveMode(enable)
    }

    @Suppress("DEPRECATION")
    /*Immersive mode is a feature in Android that allows you to hide the status bar (the bar at the
    top of the screen that displays notifications and system information) and the navigation bar
    (the bar at the bottom of the screen with navigation buttons) to provide a more immersive,
    full-screen experience to the user.*/
    private fun setImmersiveMode(enable: Boolean) {
        if(enable){
            val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    // This flag ensures that the layout remains stable even when the status bar and navigation bar are hidden.
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    //This flag allows your app's content to be displayed fullscreen, including behind the status bar.
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    //This flag allows your app's content to be displayed behind the navigation bar.
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    //This flag hides the navigation bar.
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    // This flag makes the app fullscreen, hiding the status bar.
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            //This flag enables a sticky immersive mode, where the bars reappear briefly when the user swipes from the edge of the screen.
            window.decorView.systemUiVisibility = flags
        }else{
            val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.decorView.systemUiVisibility = flags
        }
    }

    private fun setLockTask(start: Boolean, isAdmin: Boolean) {
        if(isAdmin){
            devicePolicyManager.setLockTaskPackages(
                adminComponentName, if(start){
                    arrayOf(packageName, "com.google.android.apps.maps")
                }else{
                    arrayOf()
                }
            )
        }
        if(start){
            startLockTask()
        }else{
            stopLockTask()
        }
    }

    private fun setKeyGuardEnabled(enable: Boolean) {
        /*Setting the keyguard to disabled has the same effect as choosing "None" as the screen lock type.
        However, this call has no effect if a password, pin or pattern is currently set. If a password,
        pin or pattern is set after the keyguard was disabled, the keyguard stops being disabled.*/
        // true disables the keyguard, false reenables it.
        devicePolicyManager.setKeyguardDisabled(adminComponentName, !enable)
    }

    private fun setAsHomeApp(enable: Boolean) {
        Log.i("TAG", "packageName $packageName")
        if(enable){
            val intentFilter = IntentFilter(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME) //When user press home it will open our app.
                addCategory(Intent.CATEGORY_DEFAULT)
            }
            devicePolicyManager.addPersistentPreferredActivity( //tells the device to consider your app as the default home app.
                adminComponentName, intentFilter, ComponentName(packageName, MainActivity::class.java.name)
            )
        }else{
            devicePolicyManager.clearPackagePersistentPreferredActivities(
                adminComponentName, packageName
            )
            finish()
        }
    }

    private fun setUpdatePolicy(enable: Boolean) {
        if(enable){
            devicePolicyManager.setSystemUpdatePolicy(
                adminComponentName,
                SystemUpdatePolicy.createWindowedInstallPolicy(60, 120)
                //if system is eligible between 60 and 120 minutes it will be updated. No idea why i need this!
            )
        }else{
            devicePolicyManager.setSystemUpdatePolicy(adminComponentName, null)
        }
    }


    private fun enableStayOnWhilePluggedIn(active: Boolean) = if(active) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            devicePolicyManager.setGlobalSetting(
                adminComponentName,
                Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                (
                        BatteryManager.BATTERY_PLUGGED_AC or
                                BatteryManager.BATTERY_PLUGGED_USB or
                                BatteryManager.BATTERY_PLUGGED_WIRELESS or
                                BatteryManager.BATTERY_PLUGGED_DOCK).toString()
            )
        } else {
            devicePolicyManager.setGlobalSetting(
                adminComponentName,
                Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                (
                        BatteryManager.BATTERY_PLUGGED_AC or
                                BatteryManager.BATTERY_PLUGGED_USB or
                                BatteryManager.BATTERY_PLUGGED_WIRELESS).toString()
            )
        }
    }else{
        devicePolicyManager.setGlobalSetting(adminComponentName, Settings.Global.STAY_ON_WHILE_PLUGGED_IN, "0")
    }

    private fun setRestrictions(disallow: Boolean) {
        setUserRestriction(UserManager.DISALLOW_SAFE_BOOT, disallow)
        setUserRestriction(UserManager.DISALLOW_FACTORY_RESET, disallow)
        setUserRestriction(UserManager.DISALLOW_ADD_USER, disallow)
        setUserRestriction(UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA, disallow)
        setUserRestriction(UserManager.DISALLOW_ADJUST_VOLUME, disallow)
        devicePolicyManager.setStatusBarDisabled(adminComponentName, disallow)
    }

    private fun setUserRestriction(restriction: String, disallow: Boolean) = if(disallow) {
        devicePolicyManager.addUserRestriction(adminComponentName, restriction)
    }else{
        devicePolicyManager.clearUserRestriction(adminComponentName, restriction)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if(!isConnected){
            mainConstLayoutInternetConnectContainer.visibility = View.VISIBLE
            val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation)
            mainRotatingCircle.startAnimation(rotateAnimation)
        }else{
            mainConstLayoutInternetConnectContainer.visibility = View.GONE
            mainRotatingCircle.clearAnimation()
        }
    }
}