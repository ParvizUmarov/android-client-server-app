package com.example.easycodeclientserverapp

import android.app.Application
import com.example.easycodeclientserverapp.data.cache.CacheDataSource
import com.example.easycodeclientserverapp.data.cache.JokeCache
import com.example.easycodeclientserverapp.data.cloud.CloudDataSource
import com.example.easycodeclientserverapp.view.ManageResources
import com.example.easycodeclientserverapp.data.repository.BaseRepository
import com.example.easycodeclientserverapp.data.cloud.JokeService
import com.example.easycodeclientserverapp.view.ViewModel
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    lateinit var viewModel: ViewModel
    private lateinit var realm: Realm

    override fun onCreate() {
        super.onCreate()
        val config = RealmConfiguration.Builder(schema = setOf(JokeCache::class)).build()
        realm = Realm.open(config)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.google.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val manageResources = ManageResources.Base(this)

        viewModel = ViewModel(
            BaseRepository(
                CloudDataSource.Base(
                    retrofit.create(JokeService::class.java),
                    manageResources),
                CacheDataSource.Base(realm, manageResources),
            )
        )
    }
}