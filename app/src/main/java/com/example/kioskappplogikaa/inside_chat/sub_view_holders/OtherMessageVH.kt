package com.example.kioskappplogikaa.inside_chat.sub_view_holders

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kioskappplogikaa.R
import com.example.kioskappplogikaa.inside_chat.DataClassInsideChat
import com.example.kioskappplogikaa.inside_chat.InsideChatActivity
import com.example.kioskappplogikaa.location.PdfActivity

class OtherMessageVH(private val view: View, private val activity: InsideChatActivity) : RecyclerView.ViewHolder (view) {

    val otherMessageImageViewUserPicture: ImageView = view.findViewById(R.id.otherMessageImageViewUserPicture)
    val otherMessageImageViewMessageLocation: ImageView = view.findViewById(R.id.otherMessageImageViewMessageLocation)
    val otherMessageImageViewMessageCameraImage: ImageView = view.findViewById(R.id.otherMessageImageViewMessageCameraImage)
    val otherMessageImageViewPdfMessage: ImageView = view.findViewById(R.id.otherMessageImageViewPdfMessage)
    val otherMessageImageViewRouteMessage: ImageView = view.findViewById(R.id.otherMessageImageViewRouteMessage)
    val otherMessageCardViewUserPicture: CardView = view.findViewById(R.id.otherMessageCardViewUserPicture)
    val otherMessageTextViewMessageText: TextView = view.findViewById(R.id.otherMessageTextViewMessageText)
    val otherMessageTextViewTimeOfMessage: TextView = view.findViewById(R.id.otherMessageTextViewTimeOfMessage)
    val otherMessageImageViewLikeMessage: ImageView = view.findViewById(R.id.otherMessageImageViewLikeMessage)
    val otherMessageTextViewCountOfLikesOnMessage: TextView = view.findViewById(R.id.otherMessageTextViewCountOfLikesOnMessage)
    val otherMessageCardViewContainerForPhotoMessage: CardView = view.findViewById(R.id.otherMessageCardViewContainerForPhotoMessage)

    fun bind(item: DataClassInsideChat) {

        when(item.typeOfMessage){
            "text" -> {
                otherMessageTextViewMessageText.visibility = View.VISIBLE
                otherMessageImageViewMessageLocation.visibility = View.GONE
                otherMessageImageViewMessageCameraImage.visibility = View.GONE
                otherMessageCardViewContainerForPhotoMessage.visibility = View.GONE
                otherMessageImageViewPdfMessage.visibility = View.GONE
                otherMessageImageViewRouteMessage.visibility = View.GONE
                otherMessageTextViewMessageText.text = item.userOtherMessageText
            }
            "currentLocationMessage" -> {
                otherMessageTextViewMessageText.visibility = View.GONE
                otherMessageImageViewMessageLocation.visibility = View.VISIBLE
                otherMessageImageViewMessageCameraImage.visibility = View.GONE
                otherMessageCardViewContainerForPhotoMessage.visibility = View.GONE
                otherMessageImageViewPdfMessage.visibility = View.GONE
                otherMessageImageViewRouteMessage.visibility = View.GONE
                otherMessageImageViewMessageLocation.setImageResource(R.drawable.inside_chat_add_location)

                otherMessageImageViewMessageLocation.setOnClickListener {
                    openGoogleMapApplication(item = item)
                }
            }
            "imageMessage" -> {
                otherMessageTextViewMessageText.visibility = View.GONE
                otherMessageImageViewMessageLocation.visibility = View.GONE
                otherMessageImageViewMessageCameraImage.visibility = View.VISIBLE
                otherMessageCardViewContainerForPhotoMessage.visibility = View.VISIBLE
                otherMessageImageViewPdfMessage.visibility = View.GONE
                otherMessageImageViewRouteMessage.visibility = View.GONE

                // Use the capturedImageUri to display the image in your chat UI
                Glide.with(activity)
                    .load(Uri.parse(item.userOtherMessageCameraImage))
                    .into(otherMessageImageViewMessageCameraImage) // Replace with your ImageView reference in the chat layout

                otherMessageImageViewMessageCameraImage.setOnClickListener {
                    openPhotoFromChat(item.userOtherMessageCameraImage!!)
                }
            }
            "pdfMessage" -> {
                otherMessageTextViewMessageText.visibility = View.GONE
                otherMessageImageViewMessageLocation.visibility = View.GONE
                otherMessageImageViewMessageCameraImage.visibility = View.GONE
                otherMessageCardViewContainerForPhotoMessage.visibility = View.GONE
                otherMessageImageViewPdfMessage.visibility = View.VISIBLE
                otherMessageImageViewRouteMessage.visibility = View.GONE

                otherMessageImageViewPdfMessage.setOnClickListener {
                    activity.startActivity(Intent(activity.applicationContext, PdfActivity::class.java))
                }

            }
            "routeMessage" -> {
                otherMessageTextViewMessageText.visibility = View.GONE
                otherMessageImageViewMessageLocation.visibility = View.GONE
                otherMessageImageViewMessageCameraImage.visibility = View.GONE
                otherMessageCardViewContainerForPhotoMessage.visibility = View.GONE
                otherMessageImageViewPdfMessage.visibility = View.GONE
                otherMessageImageViewRouteMessage.visibility = View.VISIBLE

                otherMessageImageViewRouteMessage.setOnClickListener {
                    openRouteInGoogleMaps(item.userOtherMessageMapRouteCoods)
                }
            }
        }

        if(item.isMessageLikedByMe == "true"){
            //staviti sliciu kad jeste lajkovana otherMessageImageViewLikeMessage
            otherMessageImageViewLikeMessage.setImageResource(R.drawable.inside_message_like_heart_image)
        }else if(item.isMessageLikedByMe == "false"){
            otherMessageImageViewLikeMessage.setImageResource(R.drawable.inside_message_not_liked_message)
        }

        if(item.countOfLikesForGroups!! > "0"){
            otherMessageTextViewCountOfLikesOnMessage.visibility = View.VISIBLE
            otherMessageTextViewCountOfLikesOnMessage.text = item.countOfLikesForGroups
        }else{
            otherMessageTextViewCountOfLikesOnMessage.visibility = View.GONE
        }

        if(item.messageFromSameUser == "true"){
            otherMessageCardViewUserPicture.visibility = View.INVISIBLE
        }else if(item.messageFromSameUser == "false"){
            otherMessageCardViewUserPicture.visibility = View.VISIBLE
        }else{
            Log.i("TAG", "Not accepted value for 'messageFromSameUser' in DataClassInsideChat.")
        }

        otherMessageTextViewTimeOfMessage.text = item.userOtherMessageTime
        //postaviti sliku iz baze
        otherMessageImageViewUserPicture.setImageResource(R.drawable.ic_launcher_foreground)
    }

    private fun openPhotoFromChat(image: String) {

        Glide.with(activity)
            .load(Uri.parse(image))
            .into(InsideChatActivity.insChatActImageViewZoomedPicture)

        InsideChatActivity.insChatActImageViewZoomedPicture.visibility = View.VISIBLE
        InsideChatActivity.insChatActConstLayoutBackgroundForImage.visibility = View.VISIBLE

    }

    private fun openGoogleMapApplication(item: DataClassInsideChat){
        val latitude = item.userOtherMessageLocation?.get("latitude")!!
        val longitude = item.userOtherMessageLocation["longitude"]!!
        val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q$latitude,$longitude(label)")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        //check is googlemap installed on phone.
        if(mapIntent.resolveActivity(activity.packageManager) != null){//open google map
            activity.startActivity(mapIntent)
        }else{ //open in browser.
            val mapsUri = "https://www.google.com/maps/search/?api=1&query$latitude,$longitude"
            val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapsUri))
            activity.startActivity(browseIntent)
        }
    }

    private fun openRouteInGoogleMaps(item:  Map<String, Double>?) {

        val startLat = item?.get("startLat")!!
        val startLng = item?.get("startLng")!!
        val destinationLat = item?.get("destinationLat")!!
        val destinationLng = item?.get("destinationLng")!!

        val uri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=$startLat,$startLng&destination=$destinationLat,$destinationLng")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")

        if (intent.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(intent)
        } else {
            // If Google Maps is not installed, you can open the map in a browser
            val browserIntent = Intent(Intent.ACTION_VIEW, uri)
            activity.startActivity(browserIntent)
        }
    }
}