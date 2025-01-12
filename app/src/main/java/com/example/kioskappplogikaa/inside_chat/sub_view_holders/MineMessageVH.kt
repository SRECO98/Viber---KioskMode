package com.example.kioskappplogikaa.inside_chat.sub_view_holders

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kioskappplogikaa.R
import com.example.kioskappplogikaa.inside_chat.DataClassInsideChat
import com.example.kioskappplogikaa.inside_chat.InsideChatActivity
import com.example.kioskappplogikaa.location.PdfActivity

class MineMessageVH(private val view: View, private val activity: InsideChatActivity) : RecyclerView.ViewHolder (view) {

    val mineMessageTextViewMessageText: TextView = view.findViewById(R.id.mineMessageTextViewMessageText)
    val mineMessageImageViewMessageLocation: ImageView = view.findViewById(R.id.mineMessageImageViewMessageLocation)
    val mineMessageImageViewMessageCameraImage: ImageView = view.findViewById(R.id.mineMessageImageViewMessageCameraImage)
    val mineMessageImageViewPdfMessage: ImageView = view.findViewById(R.id.mineMessageImageViewPdfMessage)
    val mineMessageImageViewRouteMessage: ImageView = view.findViewById(R.id.mineMessageImageViewRouteMessage)
    val mineMessageTextViewTimeOfMessage: TextView = view.findViewById(R.id.mineMessageTextViewTimeOfMessage)
    val mineMessageImageViewIsSeen: ImageView = view.findViewById(R.id.mineMessageImageViewIsSeen)
    val mineMessageImageViewLikeMessage: ImageView = view.findViewById(R.id.mineMessageImageViewLikeMessage)
    val mineMessageTextViewCountOfLikesOnMessage: TextView = view.findViewById(R.id.mineMessageTextViewCountOfLikesOnMessage)
    val mineMessageCardViewContainerForPhotoMessage: CardView = view.findViewById(R.id.mineMessageCardViewContainerForPhotoMessage)

    fun bind(item: DataClassInsideChat) {
        when(item.typeOfMessage){
            "text" -> {
                mineMessageTextViewMessageText.visibility = View.VISIBLE
                mineMessageImageViewMessageLocation.visibility = View.GONE
                mineMessageImageViewMessageCameraImage.visibility = View.GONE
                mineMessageCardViewContainerForPhotoMessage.visibility = View.GONE
                mineMessageImageViewPdfMessage.visibility = View.GONE
                mineMessageImageViewRouteMessage.visibility = View.GONE
                mineMessageTextViewMessageText.text = item.userMyMessageText
            }
            "currentLocationMessage" -> {
                mineMessageTextViewMessageText.visibility = View.GONE
                mineMessageImageViewMessageLocation.visibility = View.VISIBLE
                mineMessageImageViewMessageCameraImage.visibility = View.GONE
                mineMessageCardViewContainerForPhotoMessage.visibility = View.GONE
                mineMessageImageViewPdfMessage.visibility = View.GONE
                mineMessageImageViewRouteMessage.visibility = View.GONE
                mineMessageImageViewMessageLocation.setImageResource(R.drawable.inside_chat_add_location)

                mineMessageImageViewMessageLocation.setOnClickListener {
                    openGoogleMapApplication(item = item)
                }
            }
            "imageMessage" -> {
                mineMessageTextViewMessageText.visibility = View.GONE
                mineMessageImageViewMessageLocation.visibility = View.GONE
                mineMessageImageViewMessageCameraImage.visibility = View.VISIBLE
                mineMessageCardViewContainerForPhotoMessage.visibility = View.VISIBLE
                mineMessageImageViewPdfMessage.visibility = View.GONE
                mineMessageImageViewRouteMessage.visibility = View.GONE

                // Use the capturedImageUri to display the image in your chat UI
                Glide.with(activity)
                    .load(Uri.parse(item.userMyMessageCameraImage))
                    .into(mineMessageImageViewMessageCameraImage) // Replace with your ImageView reference in the chat layout

                mineMessageImageViewMessageCameraImage.setOnClickListener {
                    openPhotoFromChat(item.userMyMessageCameraImage!!)
                }
            }
            "pdfMessage" -> {
                mineMessageTextViewMessageText.visibility = View.GONE
                mineMessageImageViewMessageLocation.visibility = View.GONE
                mineMessageImageViewMessageCameraImage.visibility = View.GONE
                mineMessageCardViewContainerForPhotoMessage.visibility = View.GONE
                mineMessageImageViewPdfMessage.visibility = View.VISIBLE
                mineMessageImageViewRouteMessage.visibility = View.GONE

                mineMessageImageViewPdfMessage.setOnClickListener {
                    activity.startActivity(Intent(activity.applicationContext, PdfActivity::class.java))
                }

            }
            "routeMessage" -> {
                mineMessageTextViewMessageText.visibility = View.GONE
                mineMessageImageViewMessageLocation.visibility = View.GONE
                mineMessageImageViewMessageCameraImage.visibility = View.GONE
                mineMessageCardViewContainerForPhotoMessage.visibility = View.GONE
                mineMessageImageViewPdfMessage.visibility = View.GONE
                mineMessageImageViewRouteMessage.visibility = View.VISIBLE

                mineMessageImageViewRouteMessage.setOnClickListener {
                    openRouteInGoogleMaps(item.userMyMessageMapRouteCoods)
                }
            }
        }

        when (item.userMyMessageIsSeen) {
            "Seen" -> {
                //make drawable for three posibilities.
                mineMessageImageViewIsSeen.setImageResource(R.drawable.inside_message_seen_image)
            }
            "Delivered" -> {
                mineMessageImageViewIsSeen.setImageResource(R.drawable.inside_message_delivered_message_image)
            }
            "Sending" -> {
                mineMessageImageViewIsSeen.setImageResource(R.drawable.inside_message_image_sending)
            }
            "NotDelivered" -> {
                mineMessageImageViewIsSeen.setImageResource(R.drawable.inside_message_message_not_send)
            }
        }

            if(item.isMessageLikedByMe == "true"){
                //make images when i like message and when i dont.
                mineMessageImageViewLikeMessage.setImageResource(R.drawable.inside_message_like_heart_image)
            }else if(item.isMessageLikedByMe == "false"){
                mineMessageImageViewLikeMessage.setImageResource(R.drawable.inside_message_not_liked_message)
            }


        if(item.countOfLikesForGroups!! > "0"){
            mineMessageTextViewCountOfLikesOnMessage.visibility = View.VISIBLE
            mineMessageTextViewCountOfLikesOnMessage.text = item.countOfLikesForGroups
        }else{
            mineMessageTextViewCountOfLikesOnMessage.visibility = View.GONE
        }

        mineMessageTextViewTimeOfMessage.text = item.userMyMessageTime
    }

    private fun openPhotoFromChat(image: String) {

        Glide.with(activity)
            .load(Uri.parse(image))
            .into(InsideChatActivity.insChatActImageViewZoomedPicture)

        InsideChatActivity.insChatActImageViewZoomedPicture.visibility = View.VISIBLE
        InsideChatActivity.insChatActConstLayoutBackgroundForImage.visibility = View.VISIBLE

    }

    private fun openGoogleMapApplication(item: DataClassInsideChat){
        val latitude = item.userMyMessageLocation?.get("latitude")!!
        val longitude = item.userMyMessageLocation["longitude"]!!

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

        try {
            activity.startActivity(intent)
            Log.i("TAG", "Opened Google Maps app for navigation")
        } catch (e: ActivityNotFoundException) {
            Log.e("TAG", "Google Maps app not found, opening in browser.")
            // If Google Maps is not installed, you can open the map in a browser
            val browserIntent = Intent(Intent.ACTION_VIEW, uri)
            activity.startActivity(browserIntent)
        }
    }
}