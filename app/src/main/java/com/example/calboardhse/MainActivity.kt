package com.example.callboardhse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.callboardhse.api.NewsListProviderImpl

class MainActivity : AppCompatActivity() {

    private val newsFragment = NewsFragment()
    private val newsListFragment = NewsListFragment()
    private lateinit var navigationController: NavigationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationController = NavigationControllerImpl(
            newsFragment,
            newsListFragment,
            supportFragmentManager,
            findViewById(R.id.news_list_holder),
            findViewById(R.id.news_holder),
            this::finish
        )
        newsListFragment.newsListProvider =
            NewsListProviderImpl()
        newsListFragment.navigationController = navigationController
        newsFragment.navigationController = navigationController
        navigationController.open()
    }

    override fun onDestroy() {
        super.onDestroy()
        navigationController.close()
    }
}