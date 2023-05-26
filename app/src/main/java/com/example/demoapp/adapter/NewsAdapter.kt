package com.example.demoapp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demoapp.NewsDetailsActivity
import com.example.demoapp.databinding.NewsListItemBinding
import com.example.demoapp.models.Article

class NewsAdapter(context: Context) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private val TAG = "NewsAdapter"
    private val newsDataList = mutableListOf<Article>()
     var ctx: Context =context

    inner class NewsViewHolder(val binding: NewsListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding =
            NewsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }
    override fun onBindViewHolder(holder: NewsAdapter.NewsViewHolder, position: Int) {
        val newsDataListItem = newsDataList[position]
        holder.binding.apply {
            tvTitle.setText(newsDataListItem.title)
            Glide.with(holder.itemView)
                .load(newsDataListItem.urlToImage)
                .into(holder.binding.newsImage)
        }

        holder.itemView.setOnClickListener {
            val actv = Intent(ctx,NewsDetailsActivity::class.java)
            actv.putExtra("title", newsDataListItem.title)
            actv.putExtra("description", newsDataListItem.description)
            actv.putExtra("img", newsDataListItem.urlToImage)
            ctx.startActivity(actv)
        }

    }
    override fun getItemCount(): Int {
        return newsDataList.size
    }

    fun updateNewsDataList(joDataList: List<Article>) {
        val diffCallback = NewsListDiffCallback(this.newsDataList, joDataList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.newsDataList.clear()
        this.newsDataList.addAll(joDataList)
        diffResult.dispatchUpdatesTo(this)
        Log.e(TAG, "updatedNewsList: ${joDataList.size}")
    }

    class NewsListDiffCallback(
        private val oldList: List<Article>,
        private val newList: List<Article>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].title == newList[newItemPosition].title
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }


}