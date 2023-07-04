package com.example.tugasreal

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tugasreal.databinding.ItemArtikelBinding
import com.example.tugasreal.model.News

class NewsAdapter (
    private val listNews:ArrayList<News>
    ):RecyclerView.Adapter<NewsAdapter.NewsViewHolder>()
{
    inner class NewsViewHolder(itemView:ItemArtikelBinding):RecyclerView.ViewHolder(itemView.root){
        private val binding = itemView
        fun bind(news: News){
            with(binding){
                Glide.with(itemView).load(news.urlToImage).into(imgPoster)
                tvTitle.text = news.title
                tvYear.text = news.publishedAt
                tvDescription.text = news.description
                linkArticle.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(news.url)
                    linkArticle.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(ItemArtikelBinding.inflate(LayoutInflater.from(parent.context),
        parent, false
        ))
    }

    override fun getItemCount():Int = listNews.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int){
        holder.bind(listNews[position])
    }
}