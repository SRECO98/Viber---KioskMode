package com.example.kioskappplogikaa.chat_list

data class DataClassChat(
    var userName: String?,
    var groupName: String,
    var lastMessage: String,
    var numberOfNewMessagess: String?,
    var timeOfArrivingLastMessage: String?,
    var userProfilePicture: String,
    var seenOrDeliveredOrSending: String?,
){
    fun getUserProfilePictureUrl(): String {
        return userProfilePicture
    }
}
