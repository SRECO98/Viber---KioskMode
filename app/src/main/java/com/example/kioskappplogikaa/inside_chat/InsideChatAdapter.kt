package com.example.kioskappplogikaa.inside_chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kioskappplogikaa.R
import com.example.kioskappplogikaa.inside_chat.sub_view_holders.MineMessageVH
import com.example.kioskappplogikaa.inside_chat.sub_view_holders.OtherMessageVH
import com.example.kioskappplogikaa.inside_chat.sub_view_holders.TimeControllerVH

class InsideChatAdapter(private val list: List<DataClassInsideChat>, private val activity: InsideChatActivity): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        const val layoutOtherMessage: Int = 1
        const val layoutMineMessage: Int = 2
        const val layoutDateOFMessage: Int = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            layoutOtherMessage -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_other_message, parent, false)
                OtherMessageVH(view, activity = activity)
            }
            layoutMineMessage -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_mine_message, parent, false)
                MineMessageVH(view = view, activity = activity)
            }
            layoutDateOFMessage -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_date_of_message, parent, false)
                TimeControllerVH(view)
            }
            else -> {
                throw IllegalArgumentException("Unknown view type: $viewType")
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        when(holder.itemViewType){
            layoutOtherMessage -> {
                val otherMessageVH = holder as OtherMessageVH
                otherMessageVH.bind(item)
            }
            layoutMineMessage -> {
                val mineMessageVH = holder as MineMessageVH
                mineMessageVH.bind(item)
            }
            layoutDateOFMessage -> {
                val timeControllerVH = holder as TimeControllerVH
                timeControllerVH.bind(item)
            }
            else -> throw IllegalArgumentException("Unknown view type: ${holder.itemViewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        return item.viewType
    }

}