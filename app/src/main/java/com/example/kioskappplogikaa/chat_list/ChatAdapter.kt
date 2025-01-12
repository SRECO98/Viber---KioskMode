package com.example.kioskappplogikaa.chat_list

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.kioskappplogikaa.R
import com.example.kioskappplogikaa.inside_chat.InsideChatActivity
import com.squareup.picasso.Picasso

class ChatAdapter(private val context: Context, private val chatList: ArrayList<DataClassChat>) :
    RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chat_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = chatList[position]

        /*val imageUrl = currentItem.getUserProfilePictureUrl()
        Picasso.get().load(imageUrl).into(holder.chatActImageViewProfilePicture)*/

        holder.chatActTextViewNameOfUser.text = currentItem.userName
        holder.chatActTextViewLastMessage.text = currentItem.lastMessage
        holder.chatActTextViewTimeOfLastMessage.text = currentItem.timeOfArrivingLastMessage
        holder.chatActTextViewNumberOfMessages.text = currentItem.numberOfNewMessagess

        //changing the look of TextView depending of number of messages user gets.
        val numberOfMessages = currentItem.numberOfNewMessagess?.toIntOrNull() ?: 0
        setCircularBackgroundSize(holder.chatActTextViewNumberOfMessages, numberOfMessages)

        if(numberOfMessages != 0){ //if we have a new message its purple color.
            holder.chatActTextViewTimeOfLastMessage.setTextColor(Color.parseColor("#7A038B"))
        }else{ //else it is gray color.
            holder.chatActTextViewTimeOfLastMessage.setTextColor(Color.parseColor("#686868"))
        }

        holder.chatActConstLayoutContainer.setOnClickListener {
            val intent = Intent(context, InsideChatActivity::class.java).apply {
                putExtra("chatTitle", holder.chatActTextViewNameOfUser.text.toString())
                startActivity(context, this, null)
            }
        }
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val chatActImageViewProfilePicture: ImageView = itemView.findViewById(R.id.chatActImageViewProfilePicture)
        val chatActTextViewNameOfUser: TextView = itemView.findViewById(R.id.chatActTextViewNameOfUser)
        val chatActTextViewLastMessage: TextView = itemView.findViewById(R.id.chatActTextViewLastMessage)
        val chatActTextViewTimeOfLastMessage: TextView = itemView.findViewById(R.id.chatActTextViewTimeOfLastMessage)
        val chatActTextViewNumberOfMessages: TextView = itemView.findViewById(R.id.chatActTextViewNumberOfMessages)
        val chatActConstLayoutContainer: ConstraintLayout = itemView.findViewById(R.id.chatActConstLayoutContainer)
    }

    private fun setCircularBackgroundSize(textView: TextView, numberOfMessages: Int) {
        if (numberOfMessages > 0) {
            textView.text = numberOfMessages.toString()

            // Adjust the size based on the number of digits
            val newSize = when {
                numberOfMessages < 10 -> 20 // One digit
                numberOfMessages < 100 -> 26 // Two digits
                numberOfMessages < 1000 -> 32 // Three digits
                numberOfMessages < 10000 -> 38 // Four digits
                else -> 44 // Four or more digits (adjust as needed)
            }

            // Convert dp to pixels
            val pixelsWidth = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                newSize.toFloat(),
                textView.resources.displayMetrics
            ).toInt()

            val heightValue = 20
            // Convert dp to pixels
            val pixelsHeight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                heightValue.toFloat(),
                textView.resources.displayMetrics
            ).toInt()

            // Set width and height
            val layoutParams = textView.layoutParams
            layoutParams.width = pixelsWidth
            layoutParams.height = pixelsHeight
            textView.layoutParams = layoutParams

            /*// Set text size based on dimensions
            val textSizeMultiplier = 0.5f // Adjust as needed
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixelsWidth * textSizeMultiplier)*/

            // Debugging: Log the dimensions and text size
            Log.d("TextViewDimensions", "Width: ${layoutParams.width}, Height: ${layoutParams.height}, TextSize: ${textView.textSize}")
        } else {
            // Handle the case where there are no messages
            textView.text = ""
            textView.visibility = View.INVISIBLE // or View.GONE, depending on your design
        }
    }



}
