package com.example.kioskappplogikaa.inside_chat

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kioskappplogikaa.R
import com.example.kioskappplogikaa.internet.BaseActivity
import com.example.kioskappplogikaa.location.CameraActivity
import com.example.kioskappplogikaa.location.LocationHelperClass
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class InsideChatActivity : BaseActivity() {

    companion object{
        lateinit var insChatActImageViewZoomedPicture: ImageView
        lateinit var insChatActConstLayoutBackgroundForImage: ConstraintLayout
    }

    private lateinit var insChatActButtonBack: AppCompatButton
    private lateinit var insChatActTextViewTitleOfChat: TextView
    private lateinit var insChatActTextViewLastSeen: TextView
    private lateinit var insideChatActRecyclerViewContainer: RecyclerView
    private lateinit var insChatActButtonMessageOptions: AppCompatButton
    private lateinit var insideChatActEditTextMessage: EditText
    private lateinit var insChatActCardViewSendMessage: ConstraintLayout
    private lateinit var insideChatActConstLayoutOptionsContainer: ConstraintLayout
    private lateinit var insideChatActImageViewMakeAPicture: ImageView
    private lateinit var insideChatActImageViewSendLocation: ImageView
    private lateinit var insChatActConstLayoutInternetConnectContainer: ConstraintLayout
    private lateinit var insChatActRotatingCircle: ImageView

    private lateinit var locationHelperClass: LocationHelperClass
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    lateinit var randomData: List<DataClassInsideChat>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inside_chat)

        // Set the status bar color
        val window: Window = window
        window.statusBarColor = getColor(R.color.purple)

        //location logic///////////////////////////////////////////////////////////////////////////
        locationHelperClass = LocationHelperClass(this)
        if(!locationHelperClass.checkAvailabilityOfGooglePlayServices()){
            Toast.makeText(this, "You need to install Play Services App from PlayStore!", Toast.LENGTH_SHORT).show()
        }

        //get location but google will automaticly say is it from COURSE OR FINE location.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationHelperClass.getLocationUpdates()
        ///////////////////////////////////////////////////////////////////////////////////////////

        insChatActButtonBack = findViewById(R.id.insChatActButtonBack)
        insChatActTextViewTitleOfChat = findViewById(R.id.insChatActTextViewTitleOfChat)
        insChatActTextViewLastSeen = findViewById(R.id.insChatActTextViewLastSeen)
        insideChatActRecyclerViewContainer = findViewById(R.id.insideChatActRecyclerViewContainer)
        insChatActButtonMessageOptions = findViewById(R.id.insChatActButtonMessageOptions)
        insideChatActEditTextMessage = findViewById(R.id.insideChatActEditTextMessage)
        insChatActCardViewSendMessage = findViewById(R.id.insChatActCardViewSendMessage)
        insideChatActConstLayoutOptionsContainer = findViewById(R.id.insideChatActConstLayoutOptionsContainer)
        insChatActConstLayoutBackgroundForImage = findViewById(R.id.insChatActConstLayoutBackgroundForImage)
        insChatActImageViewZoomedPicture = findViewById(R.id.insChatActImageViewZoomedPicture)
        insideChatActImageViewMakeAPicture = findViewById(R.id.insideChatActImageViewMakeAPicture)
        insideChatActImageViewSendLocation = findViewById(R.id.insideChatActImageViewSendLocation)
        insChatActConstLayoutInternetConnectContainer = findViewById(R.id.insChatActConstLayoutInternetConnectContainer)
        insChatActRotatingCircle = findViewById(R.id.insChatActRotatingCircle)

        insideChatActRecyclerViewContainer.layoutManager = LinearLayoutManager(this)
        insideChatActRecyclerViewContainer.setHasFixedSize(true)
        randomData = Util.generateRandomInsideChatData()
        val adapter = InsideChatAdapter(randomData, this)
        insideChatActRecyclerViewContainer.adapter = adapter

        //scroll to the last message in chat.
        insideChatActRecyclerViewContainer.scrollToPosition(adapter.itemCount - 1)

        //get title of chat
        insChatActTextViewTitleOfChat.text = intent.getStringExtra("chatTitle")
        //insChatActTextViewLastSeen.text = getLastSeenFromServer()
        insChatActButtonBack.setOnClickListener {
            finish()
        }
        insChatActButtonMessageOptions.setOnClickListener {
            setButtonOptionsInsideChat()
        }
        insChatActCardViewSendMessage.setOnClickListener {
            val textMessage = insideChatActEditTextMessage.text.toString()
            sendMessageToAnotherUser("text", textMessage, null, null)
            insideChatActEditTextMessage.text.clear() //clear text
            insideChatActEditTextMessage.clearFocus() //clear cursor from EditText
            //closing keyboard after sending message.
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(insideChatActEditTextMessage.windowToken, 0)
        }
        insideChatActImageViewMakeAPicture.setOnClickListener {
            makeAPicture()
        }
        insideChatActImageViewSendLocation.setOnClickListener {
            //real test current location
            //sendMessageToAnotherUser("currentLocationMessage", null, null, mapOf("latitude" to Util.currentLastLatitude, "longitude" to Util.currentLastLongitude))
            sendMessageToAnotherUser("routeMessage", null, null, mapOf("startLat" to 43.8563, "startLng" to 18.4131, "destinationLat" to 44.7866, "destinationLng" to 20.4489))
            setButtonOptionsInsideChat() //close options menu inside chat
            insideChatActImageViewSendLocation.isEnabled = false
        }

    }

    private val YOUR_REQUEST_CODE = 4
    private fun makeAPicture() {
        val captureImageIntent = Intent(this, CameraActivity::class.java)
        startActivityForResult(captureImageIntent, YOUR_REQUEST_CODE)
    }

    private fun sendMessageToAnotherUser(typeOfView: String, textMessage: String?, cameraMessage: String?, locationData: Map<String, Double>?) {

        //Add logic to update database on server!

        when(typeOfView){
            "text" -> {
                val newChatItem = DataClassInsideChat(
                    viewType = 2,
                    typeOfMessage = typeOfView,
                    userOtherMessageText = "",
                    userOtherMessageLocation = null,
                    userOtherMessageCameraImage = null,
                    userOtherMessageTime = "",
                    userMyMessageText = textMessage,
                    userMyMessageLocation = null,
                    userMyMessageCameraImage = null,
                    userMyMessageTime = "11:23 PM",
                    userMyMessageIsSeen = "seen",
                    isMessageLikedByMe = "true",
                    countOfLikesForGroups = "12",
                    userTimeControllerInChat = "",
                    messageFromSameUser = "false",
                    userOtherMessagePdfUrl = null,
                    userOtherMessageMapRouteCoods = null,
                    userMyMessagePdfUrl = null,
                    userMyMessageMapRouteCoods = null,
                )
                val newList = randomData + newChatItem
                val adapter = InsideChatAdapter(newList, this)
                insideChatActRecyclerViewContainer.adapter = adapter
                //scroll to the last message in chat.
                insideChatActRecyclerViewContainer.scrollToPosition(adapter.itemCount - 1)
            }
            "currentLocationMessage" -> {
                val newChatItem = DataClassInsideChat(
                    viewType = 2,
                    typeOfMessage = typeOfView,
                    userOtherMessageText = "",
                    userOtherMessageLocation = null,
                    userOtherMessageCameraImage = null,
                    userOtherMessageTime = "",
                    userMyMessageText = "",
                    userMyMessageLocation = locationData,
                    userMyMessageCameraImage = null,
                    userMyMessageTime = "11:23 PM",
                    userMyMessageIsSeen = "seen",
                    isMessageLikedByMe = "true",
                    countOfLikesForGroups = "12",
                    userTimeControllerInChat = "",
                    messageFromSameUser = "false",
                    userOtherMessagePdfUrl = null,
                    userOtherMessageMapRouteCoods = null,
                    userMyMessagePdfUrl = null,
                    userMyMessageMapRouteCoods = null,
                )
                val newList = randomData + newChatItem
                val adapter = InsideChatAdapter(newList, this)
                insideChatActRecyclerViewContainer.adapter = adapter
                //scroll to the last message in chat.
                insideChatActRecyclerViewContainer.scrollToPosition(adapter.itemCount - 1)
            }
            "imageMessage" -> {
                val newChatItem = DataClassInsideChat(
                    viewType = 2,
                    typeOfMessage = typeOfView,
                    userOtherMessageText = "",
                    userOtherMessageLocation = null,
                    userOtherMessageCameraImage = null,
                    userOtherMessageTime = "",
                    userMyMessageText = null,
                    userMyMessageLocation = null,
                    userMyMessageCameraImage = cameraMessage,
                    userMyMessageTime = "11:23 PM",
                    userMyMessageIsSeen = "seen",
                    isMessageLikedByMe = "true",
                    countOfLikesForGroups = "12",
                    userTimeControllerInChat = "",
                    messageFromSameUser = "false",
                    userOtherMessagePdfUrl = null,
                    userOtherMessageMapRouteCoods = null,
                    userMyMessagePdfUrl = null,
                    userMyMessageMapRouteCoods = null,
                )
                val newList = randomData + newChatItem
                val adapter = InsideChatAdapter(newList, this)
                insideChatActRecyclerViewContainer.adapter = adapter
                //scroll to the last message in chat.
                insideChatActRecyclerViewContainer.scrollToPosition(adapter.itemCount - 1)
            }
            "routeMessage" -> {
                val newChatItem = DataClassInsideChat(
                    viewType = 2,
                    typeOfMessage = typeOfView,
                    userOtherMessageText = "",
                    userOtherMessageLocation = null,
                    userOtherMessageCameraImage = null,
                    userOtherMessageTime = "",
                    userMyMessageText = null,
                    userMyMessageLocation = null,
                    userMyMessageCameraImage = null,
                    userMyMessageTime = "11:23 PM",
                    userMyMessageIsSeen = "seen",
                    isMessageLikedByMe = "true",
                    countOfLikesForGroups = "12",
                    userTimeControllerInChat = "",
                    messageFromSameUser = "false",
                    userOtherMessagePdfUrl = null,
                    userOtherMessageMapRouteCoods = null,
                    userMyMessagePdfUrl = null,
                    userMyMessageMapRouteCoods = locationData,
                )
                val newList = randomData + newChatItem
                val adapter = InsideChatAdapter(newList, this)
                insideChatActRecyclerViewContainer.adapter = adapter
                //scroll to the last message in chat.
                insideChatActRecyclerViewContainer.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun setButtonOptionsInsideChat() {
        if(!isContainerForOptionsInsideChatVisisble()){
            insideChatActConstLayoutOptionsContainer.visibility = View.VISIBLE
            insideChatActImageViewSendLocation.isEnabled = true
            animateChildViews()
        }else{
            insideChatActConstLayoutOptionsContainer.visibility = View.GONE
            insideChatActImageViewSendLocation.isEnabled = false
        }
    }

    private fun isContainerForOptionsInsideChatVisisble() : Boolean =
        insideChatActConstLayoutOptionsContainer.visibility == View.VISIBLE

    private fun isImageViewPictureInsideChatOpennedAndVisible() : Boolean =
        insChatActImageViewZoomedPicture.visibility == View.VISIBLE

    private fun animateChildViews() {

        // Iterate over child views and animate them
        for (i in 0 until insideChatActConstLayoutOptionsContainer.childCount) {
            val childView: View = insideChatActConstLayoutOptionsContainer.getChildAt(i)

            // Create an ObjectAnimator to animate translationX from 100% to 0
            val animator = ObjectAnimator.ofFloat(
                childView,
                "translationX",
                insideChatActConstLayoutOptionsContainer.width.toFloat(),
                0f
            )

            // Set duration and interpolator
            animator.duration = 700
            animator.interpolator = AccelerateDecelerateInterpolator()

            // Start the animation
            animator.start()
        }
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        if(isImageViewPictureInsideChatOpennedAndVisible()){ //close openned picture
            insChatActImageViewZoomedPicture.visibility = View.GONE
            insChatActConstLayoutBackgroundForImage.visibility = View.GONE
        }else if(isContainerForOptionsInsideChatVisisble()){ //close options
            insideChatActConstLayoutOptionsContainer.visibility = View.GONE
        }else{ //close activity
            finish()
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        locationHelperClass.onActivityResume(fusedLocationProviderClient = fusedLocationProviderClient)
    }

    override fun onPause() {
        super.onPause()
        locationHelperClass.onActivityPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.i("TAG", "Open function 6")
        if (requestCode == YOUR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val capturedImageUri = data?.getStringExtra("capturedImageUri")
            if (!capturedImageUri.isNullOrEmpty()) {
                sendMessageToAnotherUser("imageMessage", null, capturedImageUri, null)
            }
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if(!isConnected){
            insChatActConstLayoutInternetConnectContainer.visibility = View.VISIBLE
            val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation)
            insChatActRotatingCircle.startAnimation(rotateAnimation)
        }else{
            insChatActConstLayoutInternetConnectContainer.visibility = View.GONE
            insChatActRotatingCircle.clearAnimation()
        }
    }
}
