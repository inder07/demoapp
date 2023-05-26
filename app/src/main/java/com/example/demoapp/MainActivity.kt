package com.example.demoapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demoapp.adapter.NewsAdapter
import com.example.demoapp.databinding.ActivityMainBinding
import com.example.demoapp.helper.Constants
import com.example.demoapp.helper.Resource
import com.example.demoapp.models.Article
import com.example.demoapp.repository.NewsRepository
import com.example.demoapp.viewmodel.NewsVM
import com.example.demoapp.viewmodel.NewsVMPF

class MainActivity : AppCompatActivity() {

    lateinit var newsVM: NewsVM
    lateinit var newsRepository: NewsRepository
    private lateinit var newsAdapter: NewsAdapter
    lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        newsAdapter = NewsAdapter(this@MainActivity)
        binding.rvHome.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        newsRepository = NewsRepository()

        getSupportActionBar()?.setTitle("News App");

        val viewModelProviderFactory = NewsVMPF(application, newsRepository)
        newsVM =
            ViewModelProvider(this, viewModelProviderFactory)[NewsVM::class.java]
        newsVM.getNewsData(Constants.BASE_URL)

        newsVM.getNewsDataMutableLiveData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { resultResponse ->
                        try {
                            setupRecyclerView(resultResponse.articles)
                        } catch (e: Exception) {
                            Toast.makeText(
                                this,
                                Constants.EXCEPTION_ERROR,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                is Resource.Error -> {
                    response.message?.let { errorMessage ->
                        Toast.makeText(
                            this,
                            errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e(TAG, "getNewsLiveData Error Occured")
                    }
                }
                is Resource.Loading -> {
                    Log.e(TAG, "getNewsLiveData Loading Data")
                }
                else -> {
                }
            }

        })

    }

    private fun setupRecyclerView(newsList: List<Article>) {
        Log.d(TAG, "setupDialogRecyclerView: called")
        newsAdapter.updateNewsDataList(newsList)
    }
}
