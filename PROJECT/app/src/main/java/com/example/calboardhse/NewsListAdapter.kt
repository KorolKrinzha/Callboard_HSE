package com.example.callboardhse

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.callboardhse.model.NewsListModel

class NewsListAdapter(
    private val model: NewsListModel
) : RecyclerView.Adapter<NewsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return model.results.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(model.results[position])
    }
}