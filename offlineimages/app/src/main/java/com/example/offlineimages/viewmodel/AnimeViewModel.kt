package com.example.offlineimages.viewmodel

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.offlineimages.helper.Constants
import com.example.offlineimages.helper.Resource
import com.example.offlineimages.models.AnimeDbModel
import com.example.offlineimages.models.Data
import com.example.offlineimages.repository.AnimeGetSetRepository
import com.example.offlineimages.room.animedb.AnimeSqlDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.IOException


class AnimeViewModel (private val context: Activity, private val repository: AnimeGetSetRepository) : ViewModel() {

    lateinit var rootPath: File
    var aniDatabase = AnimeSqlDb.getDatabase(context  )
    lateinit var folderPath: File
    val getAnimeLiveData: MutableLiveData<Resource<AnimeDbModel>> =
        MutableLiveData()
    lateinit var aniList:ArrayList<Data>
    fun getAniDetails(
        size:Int?,
        page:Int?,
        baseUrl: String,
    ) = viewModelScope.launch(Dispatchers.IO) {
        size?.let { page?.let { it1 -> safeCallAniApi(baseUrl, it, it1) } }
    }

    suspend fun safeCallAniApi(baseUrl: String, size:Int,page:Int) {
        getAnimeLiveData.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                val response = repository.getSetAnimeDetails(size, page,baseUrl)
                getAnimeLiveData.postValue(handleStoryDetailsResponse(response))

            } else {
                getAnimeLiveData.postValue(Resource.Error(Constants.NO_INTERNET))

            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> getAnimeLiveData.postValue(Resource.Error("${t.message}"))
                else -> getAnimeLiveData.postValue(Resource.Error(Constants.CONFIG_ERROR))
            }
        }
    }

    private fun handleStoryDetailsResponse(response: Response<AnimeDbModel>): Resource<AnimeDbModel>? {
        var errorMessage = ""
        aniList= ArrayList()
        if (response.isSuccessful) {

            response.body()?.let { aniDetailsResponse ->
                if(aniDetailsResponse!=null)
                {
                    aniDatabase.animeDao().insertAnimeDetails(aniDetailsResponse.data as ArrayList<Data>)
                    viewModelScope.launch(Dispatchers.IO) {
                        aniDetailsResponse.data.forEach {
                            downloadImg(it.image,it._id)
                        }
                    }
                }

            }
            response.body()?.let { storyDetailsResponse ->
                return Resource.Success(storyDetailsResponse)
            }
        } else if (response.errorBody() != null) {
            val errorObject = response.errorBody()?.let {
                JSONObject(it.charStream().readText())
            }
            errorObject?.let {
                errorMessage = it.getString("errorMessage")
            }
        }
        return Resource.Error(errorMessage)
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    private suspend fun downloadImg(imageUrl: String,filename:String) {
        //rootPath= File(this.getFilesDir(), "AudioFiles")
        rootPath= File(context.getFilesDir(), "AniImages")
        folderPath=File(rootPath,"accomodation")
        if(!folderPath.exists())
        {
            folderPath.mkdirs()
        }

        val request = Request.Builder()
            .url(imageUrl)
            .build()

        val response = OkHttpClient().newCall(request).execute()
        val inputStream = response.body?.byteStream()

        inputStream?.use { input ->
            File(folderPath, "$filename.jpg").outputStream().use { output ->
                input.copyTo(output)
                Log.d("storesuyccess","sucessful.lysotred" )
            }
        }
    }


/*
    private fun onStartDownloadAudioFiles( storageUrl: String, filename: String){
        Log.d("languraber","hindi5")

        rootPath= File(this@AnimeViewModel.getFilesDir(), "Images")

        if(!rootPath.exists())
        {
            rootPath.mkdirs()
        }
        var localFile = File(rootPath,   "$filename.zip")

        if(rootPath.exists() && localFile.exists()  )
        {
            Log.d("FileExists","FileExists")
        }

    }*/
}