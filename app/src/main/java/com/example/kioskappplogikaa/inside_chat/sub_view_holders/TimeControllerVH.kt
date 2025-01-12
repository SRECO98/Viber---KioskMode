package com.example.kioskappplogikaa.inside_chat.sub_view_holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kioskappplogikaa.R
import com.example.kioskappplogikaa.inside_chat.DataClassInsideChat

class TimeControllerVH(private val view: View) : RecyclerView.ViewHolder (view) {

    val dateOfMessageTextViewDate: TextView = view.findViewById(R.id.dateOfMessageTextViewDate)

    fun bind(item: DataClassInsideChat) {
        dateOfMessageTextViewDate.text = item.userTimeControllerInChat
    }
}