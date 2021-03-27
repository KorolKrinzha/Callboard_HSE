package com.example.callboardhse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.example.calboardhse.ProfileFragment
import com.example.callboardhse.api.NewsListProviderImpl

class MainActivity : AppCompatActivity() {

    private val newsFragment = NewsFragment()
    private val newsListFragment = NewsListFragment()
    private val changeoverprofile = ProfileFragment()
    private lateinit var navigationController: NavigationController
    private lateinit var profilebutton: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {

        val profilebutton = findViewById<LinearLayout>(R.id.profile)
        val firebutton = findViewById<LinearLayout>(R.id.fire)
        val heartbutton = findViewById<LinearLayout>(R.id.heart)
        val starbutton = findViewById<LinearLayout>(R.id.star)


/*
        profilebutton.setOnClickListener {

            val fragment = Profile()
            fragment.visibility


        }
*/



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationController = NavigationControllerImpl(
                newsFragment,
                newsListFragment,
                supportFragmentManager,
                changeoverprofile,
                findViewById(R.id.news_list_holder),
                findViewById(R.id.news_holder),
                findViewById(R.id.main),
                this::finish
        )
        newsListFragment.newsListProvider =
                NewsListProviderImpl()
        newsListFragment.navigationController = navigationController
        newsFragment.navigationController = navigationController
        navigationController.open()

        val profile_button = findViewById<LinearLayout>(R.id.profile)
        profile_button.setOnClickListener {

            navigationController.openprofile()


        }


    }

    override fun onDestroy() {
        super.onDestroy()
        navigationController.close()
    }
}