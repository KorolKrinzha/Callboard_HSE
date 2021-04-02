package com.example.callboardhse.api

import com.example.callboardhse.model.NewsListModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("/{source}")
    fun getNews(
        @Path("source") source: String,

    ): Call<NewsListModel>
}