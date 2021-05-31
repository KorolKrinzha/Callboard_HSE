package com.example.callboardhse.api

import android.util.Log
import com.example.callboardhse.model.NewsListModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsListProviderImpl : NewsListProvider {
    private val api = Retrofit.Builder()
        .baseUrl("http://u1293020.isp.regruhosting.ru/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)

    override fun provideNewsList(listener: NewsListListener) {
        api.getNews("get").enqueue(
            object : Callback<NewsListModel> {
                override fun onFailure(call: Call<NewsListModel>, t: Throwable) {
                    Log.e("KEK", "errrrrrrrrorrrr, error = ${t.message}")

                    t.printStackTrace()
                }

                override fun onResponse(call: Call<NewsListModel>, response: Response<NewsListModel>) {
                    listener.onNewsListReceived(response.body())
                }
            }
        )
    }
}