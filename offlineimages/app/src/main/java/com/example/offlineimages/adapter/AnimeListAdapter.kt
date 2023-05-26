package com.example.offlineimages.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.offlineimages.databinding.AnimeListItemBinding
import java.io.File

class AnimeListAdapter : RecyclerView.Adapter<AnimeListAdapter.ViewHolder>() {
    private var animeList = ArrayList<String>()
    lateinit var context:Context
    fun setAnimeList(animeList: MutableList<String>, context: Context) {
        this.animeList = animeList as ArrayList<String>
        this.context=context
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: AnimeListItemBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AnimeListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

      /*  val imageFile: File = File(context.filesDir, animeList[position].absolutePath)
        Glide.with(holder.itemView)
            .load( File(imageFile))
            .into(holder.binding.animeImage)*/
        val imageFile = animeList[position]
        Glide.with(holder.itemView)
            .load(File(imageFile))
            .into(holder.binding.animeImage)
    }

    override fun getItemCount(): Int {
        return animeList.size
    }
}