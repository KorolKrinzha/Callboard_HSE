package com.example.callboardhse.api

import com.example.callboardhse.model.NewsListModel

interface NewsListListener {
    fun onNewsListReceived(newsList: NewsListModel?)
}