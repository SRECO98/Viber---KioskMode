package com.example.kioskappplogikaa.add_contact

class Util {
    companion object{
        fun generateTestData(): ArrayList<DataClassAddContact> {
            val numberOfItems = 10 // Adjust as needed
            val data = arrayListOf<DataClassAddContact>()

            for (i in 1..numberOfItems) {
                val randomName = "Contact $i"
                val randomImage = "image_$i.jpg"

                // Create an instance of your data class and add it to the list
                val item = DataClassAddContact(randomName, randomImage, false, false)
                data.add(item)
            }

            return data
        }

        fun generateTestData2(): ArrayList<DataClassAddContact> {
            val numberOfItems = 10 // Adjust as needed
            val data = arrayListOf<DataClassAddContact>()

            for (i in 1..numberOfItems) {
                val randomName = "Contact $i"
                val randomImage = "image_$i.jpg"

                // Create an instance of your data class and add it to the list
                val item = DataClassAddContact(randomName, randomImage, true, false)
                data.add(item)
            }

            return data
        }
    }

}