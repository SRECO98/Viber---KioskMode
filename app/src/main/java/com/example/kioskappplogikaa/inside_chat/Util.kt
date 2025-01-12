package com.example.kioskappplogikaa.inside_chat

import com.example.kioskappplogikaa.R
import java.util.Random

class Util {

    companion object {
        fun generateRandomInsideChatData(): List<DataClassInsideChat> {
            val data = mutableListOf<DataClassInsideChat>()
            val random = Random()

            var lastViewType: Int? = null

            repeat(50) {
                val randomIndex = random.nextInt(100)

                val viewType = if (random.nextBoolean() && lastViewType != null) {
                    // Use the last view type
                    lastViewType!!
                } else {
                    // Choose a new view type
                    val newViewType = when {
                        it % 3 == 0 -> InsideChatAdapter.layoutOtherMessage
                        it % 3 == 1 -> InsideChatAdapter.layoutMineMessage
                        else -> InsideChatAdapter.layoutDateOFMessage
                    }
                    lastViewType = newViewType
                    newViewType
                }

                data.add(
                    DataClassInsideChat(
                        viewType = viewType,
                        typeOfMessage = "text",
                        userOtherMessageText = "Other user message $randomIndex",
                        userOtherMessageLocation = null,
                        userOtherMessageCameraImage = null,
                        userOtherMessageTime = "12:3$randomIndex PM",
                        userMyMessageText = "My message $randomIndex",
                        userMyMessageLocation = null,
                        userMyMessageCameraImage = null,
                        userMyMessageTime = "1:2$randomIndex PM",
                        userMyMessageIsSeen = getRandomSeenStatus(random),
                        isMessageLikedByMe = random.nextBoolean().toString(),
                        countOfLikesForGroups = random.nextInt(10).toString(),
                        userTimeControllerInChat = "Today at 3:$randomIndex PM",
                        messageFromSameUser = random.nextBoolean().toString(),
                        userOtherMessagePdfUrl = null,
                        userOtherMessageMapRouteCoods = null,
                        userMyMessagePdfUrl = null,
                        userMyMessageMapRouteCoods = null,
                    )
                )
            }

            return data
        }

        private fun getRandomSeenStatus(random: Random): String {
            val statuses = listOf("Seen", "Delivered", "Sending", "NotDelivered")
            return statuses[random.nextInt(statuses.size)]
        }

        var currentLastLatitude: Double = 0.0
        var currentLastLongitude: Double = 0.0
        var timeOfGettingLastData: Long = 1
    }

}