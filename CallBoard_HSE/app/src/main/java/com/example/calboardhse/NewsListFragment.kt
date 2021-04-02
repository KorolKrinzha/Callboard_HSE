package com.example.callboardhse

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.callboardhse.api.NewsListListener
import com.example.callboardhse.api.NewsListProvider
import com.example.callboardhse.model.NewsListModel

class NewsListFragment : Fragment(R.layout.news_list_fragment_layout) {
    var navigationController: NavigationController? = null
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var newsListView: RecyclerView
    lateinit var loadingView: View
    lateinit var newsListProvider: NewsListProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        newsListView = view.findViewById(R.id.news_list)
        loadingView = view.findViewById(R.id.loading)
        swipeRefreshLayout.setOnRefreshListener {
            refreshNewsList()
        }
        refreshNewsList()
        newsListView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun refreshNewsList() {
        newsListProvider.provideNewsList(object : NewsListListener {
            override fun onNewsListReceived(newsList: NewsListModel?) {
                swipeRefreshLayout.isRefreshing = false
                newsList?.let { list ->
                    loadingView.visibility = View.GONE
                    newsListView.visibility = View.VISIBLE
                    newsListView.adapter =  NewsListAdapter(list)
                    newsListView.adapter?.notifyDataSetChanged()
                }
            }
        })
    }
}