package com.example.demoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.demoapp.helper.Constants
import com.example.demoapp.helper.Resource
import com.example.demoapp.helper.Utils
import com.example.demoapp.models.ResponseModel
import com.example.demoapp.repository.NewsRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsVM(
    application: Application,
    private val newsRepository: NewsRepository
) : AndroidViewModel(application) {

    val getNewsDataMutableLiveData: MutableLiveData<Resource<ResponseModel>> =
        MutableLiveData()

    fun getNewsData(
        baseUrl: String,
    ) = viewModelScope.launch {
        safeAPICallGetContainerQuery(baseUrl)
    }

    private fun handleContainerQueryResponse(response: Response<ResponseModel>): Resource<ResponseModel> {
        var errorMessage = "Api Call Failed"
        if (response.isSuccessful) {
            response.body()?.let { response ->
                if (response.articles.isNotEmpty())
                {
                    return Resource.Success(response)
                }
                return Resource.Error("No Data Found")
            }
        } else{
            return Resource.Error(errorMessage)
        }
        return Resource.Error(errorMessage)
    }

    private suspend fun safeAPICallGetContainerQuery(
        baseUrl: String) {
        getNewsDataMutableLiveData.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication())) {
                val response = newsRepository.getNewsData("tesla","2023-05-21","publishedAt","d14d93c5dc7646a7b66870dbd4f8692d",baseUrl)
                getNewsDataMutableLiveData.postValue(handleContainerQueryResponse(response))
            } else {
                getNewsDataMutableLiveData.postValue(Resource.Error(Constants.NO_INTERNET))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> getNewsDataMutableLiveData.postValue(Resource.Error("${t.message}"))
                else -> getNewsDataMutableLiveData.postValue(Resource.Error(Constants.CONFIG_ERROR))
            }
        }
    }
}