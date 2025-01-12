package com.example.kioskappplogikaa.inside_chat

import android.media.Image

class DataClassInsideChat (
    val viewType: Int,
    val typeOfMessage: String?,
    val userOtherMessageText: String?, //only for other view
    val userOtherMessageLocation: Map<String, Double>?,
    val userOtherMessageCameraImage: String?,
    val userOtherMessageTime: String?, //only for other view
    val userOtherMessagePdfUrl: String?,
    val userOtherMessageMapRouteCoods: Map<String, Double>?,
    val userMyMessageText: String?,  //only for mine view
    val userMyMessageLocation: Map<String, Double>?,
    val userMyMessageCameraImage: String?,
    val userMyMessageTime: String?, //only for mine view
    val userMyMessagePdfUrl: String?,
    val userMyMessageMapRouteCoods: Map<String, Double>?,
    val userMyMessageIsSeen: String?, //only for mine view
    val isMessageLikedByMe: String?, //for mine and other
    val countOfLikesForGroups: String? = "0", //for mine and other but in groups chat!
    val userTimeControllerInChat: String?, //only for time view
    val messageFromSameUser: String?, //only for other view
)