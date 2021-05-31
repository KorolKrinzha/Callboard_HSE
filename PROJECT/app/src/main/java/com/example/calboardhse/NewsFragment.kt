package com.example.callboardhse

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class NewsFragment : Fragment(R.layout.news_fragment_layout) {

    private lateinit var textTitle: TextView
    private lateinit var newsBody: TextView
    var navigationController: NavigationController? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textTitle = view.findViewById(R.id.title)
        newsBody = view.findViewById(R.id.news_body)
    }

}