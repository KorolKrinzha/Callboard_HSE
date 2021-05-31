package com.example.callboardhse

import android.view.View
import androidx.fragment.app.FragmentManager
import com.example.calboardhse.ProfileFragment


class NavigationControllerImpl(
    private val newsFragment: NewsFragment,
    private val newsListFragment: NewsListFragment,
    private val fragmentManager: FragmentManager,
    private val profileManager: ProfileFragment,
    private val newsListFragmentHolder: View,
    private val newsFragmentHolder: View,
    private val changeoverprofile: View,
    private val finishCallback: () -> Unit
) : NavigationController, FragmentManager.OnBackStackChangedListener {

    init {
        fragmentManager.addOnBackStackChangedListener(this)
    }

    override fun open() {
        if (fragmentManager.findFragmentByTag(NEWS_LIST) != null) {
            return
        }
        fragmentManager.beginTransaction().add(newsListFragmentHolder.id, newsListFragment, NEWS_LIST).addToBackStack(NEWS_LIST).commit()
    }

    override fun onNewsClick() {
        if (fragmentManager.findFragmentByTag(NEWS) != null) {
            return
        }
        newsFragmentHolder.visibility = View.VISIBLE
        fragmentManager.beginTransaction().add(newsFragmentHolder.id, newsFragment, NEWS).addToBackStack(NEWS).commit()
    }

    override fun onNewsClosed() {
        newsFragmentHolder.visibility = View.GONE
        fragmentManager.popBackStack()
    }

    override fun openprofile() {
        if (fragmentManager.findFragmentByTag(PROFILE)!=null){
            return
        }

        changeoverprofile.visibility = View.VISIBLE
        fragmentManager.beginTransaction().add(changeoverprofile.id, profileManager, PROFILE).addToBackStack(PROFILE).commit()

    }

    override fun close() {
        fragmentManager.removeOnBackStackChangedListener(this)
    }

    override fun onBackStackChanged() {
        if (fragmentManager.backStackEntryCount == 0) {
            finishCallback.invoke()
        }
        if (fragmentManager.backStackEntryCount == 1) {
            newsFragmentHolder.visibility = View.GONE
        }
    }

    private companion object {
        const val NEWS = "news"
        const val NEWS_LIST = "news_list"
        const val PROFILE = "profile"
    }
}