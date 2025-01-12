package com.example.kioskappplogikaa.add_contact

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kioskappplogikaa.R
import com.example.kioskappplogikaa.internet.BaseActivity

class AddContactActivity : BaseActivity() {

    private lateinit var addContactActButtonBack: AppCompatButton
    private lateinit var addContactActButtonNext: AppCompatButton
    private lateinit var addContactActRecyclerViewListOfContacts: RecyclerView
    private lateinit var addContactActMultipleMessagesContainer: ConstraintLayout
    private lateinit var arrayListListOfPossibleContacts: ArrayList<DataClassAddContact>
    private lateinit var addContactAdapter: AddContactAdapter
    private lateinit var addContactActConstLayoutInternetConnectContainer: ConstraintLayout
    private lateinit var addContactActRotatingCircle: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        // Set the status bar color
        val window: Window = window
        window.statusBarColor = getColor(R.color.purple)

        addContactActButtonBack = findViewById(R.id.addContactActButtonBack)
        addContactActButtonNext = findViewById(R.id.addContactActButtonNext)
        addContactActRecyclerViewListOfContacts = findViewById(R.id.addContactActRecyclerViewListOfContacts)
        addContactActMultipleMessagesContainer = findViewById(R.id.addContactActMultipleMessagesContainer)
        addContactActConstLayoutInternetConnectContainer = findViewById(R.id.addContactActConstLayoutInternetConnectContainer)
        addContactActRotatingCircle = findViewById(R.id.addContactActRotatingCircle)
        addContactActRecyclerViewListOfContacts.layoutManager = LinearLayoutManager(this)
        addContactActRecyclerViewListOfContacts.setHasFixedSize(true)

        // Add DividerItemDecoration
        val dividerItemDecoration = DividerItemDecoration(addContactActRecyclerViewListOfContacts.context, DividerItemDecoration.VERTICAL)
        addContactActRecyclerViewListOfContacts.addItemDecoration(dividerItemDecoration)

        arrayListListOfPossibleContacts = Util.generateTestData()
        addContactAdapter = AddContactAdapter(this, arrayListListOfPossibleContacts)
        addContactActRecyclerViewListOfContacts.adapter = addContactAdapter

        addContactActButtonBack.setOnClickListener{
            finish()
        }
        addContactActMultipleMessagesContainer.setOnClickListener {
            arrayListListOfPossibleContacts = Util.generateTestData2()
            addContactAdapter.updateData(arrayListListOfPossibleContacts)
            addContactActButtonNext.visibility = View.VISIBLE
            isSendMultipleMessagesLayoutVissible = true
        }
        addContactActButtonNext.setOnClickListener{
            //make edittext for message and send it to all users we checked, also take list of checked users.
        }
    }

    private var isSendMultipleMessagesLayoutVissible = false
    override fun onBackPressed() {
        if(isSendMultipleMessagesLayoutVissible){
            arrayListListOfPossibleContacts = Util.generateTestData()
            addContactAdapter.updateData(arrayListListOfPossibleContacts)
            addContactActButtonNext.visibility = View.GONE
            isSendMultipleMessagesLayoutVissible = false
        }else{
            super.onBackPressed()
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if(!isConnected){
            addContactActConstLayoutInternetConnectContainer.visibility = View.VISIBLE
            val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation)
            addContactActRotatingCircle.startAnimation(rotateAnimation)
        }else{
            addContactActConstLayoutInternetConnectContainer.visibility = View.GONE
            addContactActRotatingCircle.clearAnimation()
        }
    }
}