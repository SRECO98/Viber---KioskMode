package com.example.kioskappplogikaa.chat_list

import java.util.*

object TestDataGenerator {
    fun generateTestData(size: Int): ArrayList<DataClassChat> {
        val testDataList = ArrayList<DataClassChat>()

        val random = Random()

        val userNames = arrayOf("Alice", "Bob", "Charlie", "David", "Eva")
        val groupNames = arrayOf("Family", "Friends", "Work", "Study", "Sports")
        val lastMessages = arrayOf("Hello!", "How are you?How are you?How are you?How are you?How are you?How are you?How are you?How are you?How are you?How are you?", "What's up?", "Meeting at 2 PM", "See you later!")
        val numberOfMessages = arrayOf("0", "11", "112", "11991", "5")
        val seenOrDeliveredOrSendings = arrayOf("seen", "delivered", "sending", "sending2", "seen2")
        val userProfilePictures = arrayOf(
            "https://example.com/image1.jpg",
            "https://example.com/image2.jpg",
            "https://example.com/image3.jpg",
            "https://example.com/image4.jpg",
            "https://example.com/image5.jpg"
        )

        for (i in 0 until size) {
            val userName = userNames[random.nextInt(userNames.size)]
            val groupName = groupNames[random.nextInt(groupNames.size)]
            val lastMessage = lastMessages[random.nextInt(lastMessages.size)]
            val numberOfMessage = numberOfMessages[random.nextInt(numberOfMessages.size)]
            val timeOfArrivingLastMessage = "12:30 PM"  // You can modify this to generate random times
            val seenOrDeliveredOrSending = seenOrDeliveredOrSendings[random.nextInt(seenOrDeliveredOrSendings.size)]
            val userProfilePicture = userProfilePictures[random.nextInt(userProfilePictures.size)]

            val data = DataClassChat(userName, groupName, lastMessage, numberOfMessage, timeOfArrivingLastMessage, userProfilePicture, seenOrDeliveredOrSending)
            testDataList.add(data)
        }

        return testDataList
    }
}
