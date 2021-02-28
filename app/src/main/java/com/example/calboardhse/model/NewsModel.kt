package com.example.callboardhse.model

data class NewsModel(
    val section: String,
    val subsection: String,
    val title: String,
    val abstract: String,
    val url: String,
    val byline: String
) {
    class Builder(
        var title: String = "",
        var abstract: String = "",
        var newsUrl: String = "",
        var section: String = "",
        var subsection: String = "",
        var byline: String = ""
    ) {
        fun build() = NewsModel(section, subsection, title, abstract, newsUrl, byline)
    }
}