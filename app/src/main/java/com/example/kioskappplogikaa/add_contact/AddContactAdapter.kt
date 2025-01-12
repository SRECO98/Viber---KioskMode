package com.example.kioskappplogikaa.add_contact

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.kioskappplogikaa.R
import com.example.kioskappplogikaa.inside_chat.InsideChatActivity
import java.util.ArrayList


class AddContactAdapter(private val activity: Activity, private val addContactList: ArrayList<DataClassAddContact>) : RecyclerView.Adapter<AddContactAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.add_contact_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return addContactList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = addContactList[position]

        //set image of user.
        //holder.addContactActImageViewProfilePicture = currentItem.imageOfContact
        holder.addContactActTextViewNameOfContact.text = currentItem.nameOfContact

        holder.addContactActImageViewProfilePicture.setOnClickListener {
            //openContactProfilePicture()
        }
        holder.addContactActElementContainer.setOnClickListener {
            //openInsideChatWithThisUser()
            val intent = Intent(activity, InsideChatActivity::class.java).apply {
                putExtra("chatTitle", holder.addContactActTextViewNameOfContact.text.toString())
                startActivity(activity, this, null)
                activity.finish()
            }
        }

        holder.addContactActImageViewCheckForMultipleMessage.setOnClickListener {
            if(currentItem.isButtonForMultipleUsersChecked){
                holder.addContactActImageViewCheckForMultipleMessage.setImageResource(R.drawable.add_contact_multiple_message_unchecked)
                currentItem.isButtonForMultipleUsersChecked = false
            }else{
                holder.addContactActImageViewCheckForMultipleMessage.setImageResource(R.drawable.add_contact_multiple_message_checked)
                currentItem.isButtonForMultipleUsersChecked = true
            }
        }
        holder.addContactActCardViewContainerForCheckButtons.setOnClickListener {
            if(currentItem.isButtonForMultipleUsersChecked){
                holder.addContactActImageViewCheckForMultipleMessage.setImageResource(R.drawable.add_contact_multiple_message_unchecked)
                currentItem.isButtonForMultipleUsersChecked = false
            }else{
                holder.addContactActImageViewCheckForMultipleMessage.setImageResource(R.drawable.add_contact_multiple_message_checked)
                currentItem.isButtonForMultipleUsersChecked = true
            }
        }

        if(currentItem.buttonMultipleContactsVisible){
            holder.addContactActImageViewCheckForMultipleMessage.visibility = View.VISIBLE
        }else{
            holder.addContactActImageViewCheckForMultipleMessage.visibility = View.GONE
        }
    }

    // Method to update the data in the adapter
    fun updateData(newDataList: List<DataClassAddContact>) {
        addContactList.clear()
        addContactList.addAll(newDataList)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val addContactActImageViewProfilePicture: ImageView = itemView.findViewById(R.id.addContactActImageViewProfilePicture)
        val addContactActImageViewCheckForMultipleMessage: ImageView = itemView.findViewById(R.id.addContactActImageViewCheckForMultipleMessage)
        val addContactActTextViewNameOfContact: TextView = itemView.findViewById(R.id.addContactActTextViewNameOfContact)
        val addContactActElementContainer: ConstraintLayout = itemView.findViewById(R.id.addContactActElementContainer)
        val addContactActCardViewContainerForCheckButtons: CardView = itemView.findViewById(R.id.addContactActCardViewContainerForCheckButtons)
    }
}