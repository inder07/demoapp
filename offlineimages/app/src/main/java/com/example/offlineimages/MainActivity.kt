package com.example.offlineimages

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.offlineimages.adapter.AnimeListAdapter
import com.example.offlineimages.databinding.ActivityMainBinding
import com.example.offlineimages.helper.Constants
import com.example.offlineimages.repository.AnimeGetSetRepository
import com.example.offlineimages.viewmodel.AnimeViewModel
import com.example.offlineimages.viewmodel.AnimeViewModelFactory
import java.io.File

class MainActivity : AppCompatActivity() {
    //viewmodels
    lateinit var mainViewModel: AnimeViewModel
    lateinit var mainRepository: AnimeGetSetRepository
    val imageFiles = mutableListOf<String>()
    lateinit var rootPath: File
    lateinit var binding: ActivityMainBinding
    private var animeListAdapter: AnimeListAdapter? = null

    lateinit var folderPath: File


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        mainRepository = AnimeGetSetRepository()

        val viewModelProviderFactory = AnimeViewModelFactory(this, mainRepository)
        mainViewModel =
            ViewModelProvider(this, viewModelProviderFactory)[AnimeViewModel::class.java]
        mainViewModel.getAniDetails(10, 2, Constants.BASE_URL)

        mainViewModel.getAnimeLiveData.observe(this) {
            if (it != null) {
                Log.d("anidetails", it.toString())
            }
        }

        rootPath = File(this.getFilesDir(), "AniImages")
        folderPath=File(rootPath,"accomodation")
        folderPath.listFiles()?.forEach { file ->
            if (file.isFile && file.extension == "jpg") {
                imageFiles.add(file.toString())
            }
        }

        //showOnRecyclerView();
        animeListAdapter =AnimeListAdapter()
        animeListAdapter?.setAnimeList(imageFiles,this)
        binding!!.imagesRc.adapter=animeListAdapter
        binding!!.imagesRc.layoutManager = LinearLayoutManager(this)
        binding!!.imagesRc .setHasFixedSize(true)

        Log.d("listofimageslocal",imageFiles.toString())


    }
}
