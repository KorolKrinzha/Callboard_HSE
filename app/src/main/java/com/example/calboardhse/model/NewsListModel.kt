package com.example.callboardhse.model

data class NewsListModel(
    val status: String,
    val num_results: Int,
    val results: List<NewsModel>
) {
    class Builder(
        var success: String = "",
        var newsAmount: Int = 0,
        var news: List<NewsModel> = listOf()
    ) {
        fun build() = NewsListModel(success, newsAmount, news)
    }
}