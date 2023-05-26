package com.example.demoapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.demoapp.databinding.ActivityNewsDetailsBinding

class NewsDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityNewsDetailsBinding

    var title = ""
    var description = ""
    var img = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_details)

        getSupportActionBar()?.setTitle("News Details");

        try {
            title= intent.getStringExtra("title").toString()
            description= intent.getStringExtra("description").toString()
            img= intent.getStringExtra("img").toString()

            binding.apply {
                tvTitle.setText(title)
                tvDescription.setText(description)
                Glide.with(this@NewsDetailsActivity)
                    .load(img)
                    .into(binding.newsImage)
            }
        }catch (e:Exception)
        {
            Toast.makeText(this,"An Error has Occured",Toast.LENGTH_SHORT).show()
            finish()
        }


    }
}