package com.example.offlineimages.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.offlineimages.repository.AnimeGetSetRepository

class AnimeViewModelFactory(
    private val context: Activity,
    private val anigetsetrepo:  AnimeGetSetRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AnimeViewModel::
            class.java ))
        {
            return AnimeViewModel(context,anigetsetrepo) as T
        }
        else
        {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}