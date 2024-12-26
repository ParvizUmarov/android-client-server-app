package com.example.easycodeclientserverapp

import android.app.Application
import com.example.easycodeclientserverapp.view.ManageResources
import com.example.easycodeclientserverapp.data.repository.BaseRepository
import com.example.easycodeclientserverapp.data.cloud.JokeService
import com.example.easycodeclientserverapp.view.ViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    lateinit var viewModel: ViewModel

    override fun onCreate() {
        super.onCreate()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.google.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        viewModel = ViewModel(
            BaseRepository(
                retrofit.create(JokeService::class.java),
                ManageResources.Base(this)
            )
        )
    }
}