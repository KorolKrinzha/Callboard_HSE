package com.example.callboardhse.model

data class NewsModel(
        var title: String = "",
        var place: String = "",
        var dates: String = "",
        var currentdate: String = "",
        var contacts: String = "",
        var content: String = ""
) {
    class Builder(
        var title: String = "",


        var place: String = "",
        var dates: String = "",
        var currentdate: String = "",
        var contacts: String = "",
        var content: String = ""
    ) {
        fun build() = NewsModel(title, place, dates, currentdate, contacts, content)
    }
}

//const val TITLE = "title"
//const val PLACE = "place"
//const val DATES = "dates"
//const val CURRENTDATE = "currentdate"
//const val CONTACTS = "contacts"
//const val CONTENT = "content"