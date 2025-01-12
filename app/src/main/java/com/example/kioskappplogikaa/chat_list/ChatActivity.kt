package com.example.kioskappplogikaa.chat_list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kioskappplogikaa.R
import com.example.kioskappplogikaa.add_contact.AddContactActivity
import com.example.kioskappplogikaa.internet.BaseActivity

class ChatActivity : BaseActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var newRecycleView : RecyclerView
    private lateinit var arrayListContacts: ArrayList<DataClassChat>
    private lateinit var chatActImageViewAddNewContact: ImageView
    private lateinit var chatActConstLayoutInternetConnectContainer: ConstraintLayout
    private lateinit var chatActRotatingCircle: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Set the status bar color
        val window: Window = window
        window.statusBarColor = getColor(R.color.purple)

        chatActImageViewAddNewContact = findViewById(R.id.chatActImageViewAddNewContact)
        newRecycleView = findViewById(R.id.chatActRecyclerViewListOfContacts)
        chatActConstLayoutInternetConnectContainer = findViewById(R.id.chatActConstLayoutInternetConnectContainer)
        chatActRotatingCircle = findViewById(R.id.chatActRotatingCircle)
        newRecycleView.layoutManager = LinearLayoutManager(this)
        newRecycleView.setHasFixedSize(true)

        // Add DividerItemDecoration
        val dividerItemDecoration = DividerItemDecoration(newRecycleView.context, DividerItemDecoration.VERTICAL)
        newRecycleView.addItemDecoration(dividerItemDecoration)

        arrayListContacts = TestDataGenerator.generateTestData(20) // Change 20 to the desired size
        chatAdapter = ChatAdapter(this, arrayListContacts)
        newRecycleView.adapter = chatAdapter

        chatActImageViewAddNewContact.setOnClickListener {
            startActivity(Intent(this, AddContactActivity::class.java))
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if(!isConnected){
            chatActConstLayoutInternetConnectContainer.visibility = View.VISIBLE
            val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation)
            chatActRotatingCircle.startAnimation(rotateAnimation)
        }else{
            chatActConstLayoutInternetConnectContainer.visibility = View.GONE
            chatActRotatingCircle.clearAnimation()
        }
    }
}