package com.example.callboardhse.api

interface NewsListProvider {
    fun provideNewsList(listener: NewsListListener)
}