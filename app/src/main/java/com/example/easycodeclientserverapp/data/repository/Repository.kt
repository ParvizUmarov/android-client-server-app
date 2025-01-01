package com.example.easycodeclientserverapp.data.repository

import android.util.Log
import com.example.easycodeclientserverapp.data.cache.CacheDataSource
import com.example.easycodeclientserverapp.data.cache.JokeCacheCallback
import com.example.easycodeclientserverapp.data.callback.ResultCallback
import com.example.easycodeclientserverapp.data.cloud.CloudDataSource
import com.example.easycodeclientserverapp.data.cloud.JokeCloudCallback
import com.example.easycodeclientserverapp.data.dto.JokeCloud
import com.example.easycodeclientserverapp.data.entity.BaseJoke
import com.example.easycodeclientserverapp.data.entity.FailedJoke
import com.example.easycodeclientserverapp.data.error.Error
import com.example.easycodeclientserverapp.view.ManageResources

class BaseRepository(
    private val cloudDataSource: CloudDataSource,
    private val cacheDataSource: CacheDataSource
) :
    Repository {

    private var callback: ResultCallback? = null
    private var getJokeFromCache = false

    private var jokeCloudTemporary: JokeCloud? = null

    override fun getJoke() {

        if (getJokeFromCache) {
            cacheDataSource.fetch(object : JokeCacheCallback {
                override fun provideJoke(joke: JokeCloud) {
                    jokeCloudTemporary = joke
                    callback?.provideSuccess(joke.toFavoriteUi())
                }

                override fun provideError(error: Error) {
                    callback?.provideError(error)
                }

            })

        } else {
            cloudDataSource.fetch(object : JokeCloudCallback {
                override fun provideJokeCloud(jokeCloud: JokeCloud) {
                    jokeCloudTemporary = jokeCloud
                    callback?.provideSuccess(jokeCloud.toUi())
                }

                override fun provideError(error: Error) {
                    jokeCloudTemporary = null
                    callback?.provideSuccess(FailedJoke(error.message()))
                }
            })
        }
    }

    override fun clear() {
        callback = null
    }

    override fun changeJokeStatus(resultCallback: ResultCallback) {
        jokeCloudTemporary?.let {
            resultCallback.provideSuccess(it.toFavoriteUi())
            it.change(cacheDataSource)
        }
    }

    override fun chooseFavorite(favorite: Boolean) {
        getJokeFromCache = favorite
    }

    override fun init(callback: ResultCallback) {
        this.callback = callback
    }


}

class FakeRepository(manageResources: ManageResources) : Repository {

    private var callback: ResultCallback? = null
    private var noConnection = Error.NoConnection(manageResources)
    private var serviceUnavailable = Error.ServiceUnavailable(manageResources)

    private var count = 1

    override fun getJoke() {
        Thread {
            Thread.sleep(2000)
            if (count % 2 == 1) {
                callback?.provideSuccess(BaseJoke("NEW JOKE text AHAHAHAHHAHAH!", "punchline"))
            } else if (count % 3 == 0) {
                callback?.provideSuccess(FailedJoke(noConnection.message()))
            } else {
                callback?.provideSuccess(FailedJoke(serviceUnavailable.message()))
            }

            count++
        }.start()

    }

    override fun init(callback: ResultCallback) {
        this.callback = callback
    }

    override fun clear() {
        callback = null
    }

    override fun changeJokeStatus(resultCallback: ResultCallback) {
        TODO("Not yet implemented")
    }

    override fun chooseFavorite(favorite: Boolean) {
        TODO("Not yet implemented")
    }
}

interface Repository {
    fun getJoke()
    fun init(callback: ResultCallback)
    fun clear()
    fun changeJokeStatus(resultCallback: ResultCallback)
    fun chooseFavorite(favorite: Boolean)
}

