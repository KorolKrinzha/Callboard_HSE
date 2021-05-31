package com.example.callboardhse.model

data class NewsListModel(

    val results: List<NewsModel>
) {
    class Builder(

        var news: List<NewsModel> = listOf()
    ) {
        fun build() = NewsListModel(news)
    }
}