package com.example.easycodeclientserverapp.data.repository

import com.example.easycodeclientserverapp.data.cache.CacheDataSource
import com.example.easycodeclientserverapp.data.cache.JokeCacheCallback
import com.example.easycodeclientserverapp.data.callback.ResultCallback
import com.example.easycodeclientserverapp.data.cloud.CloudDataSource
import com.example.easycodeclientserverapp.data.cloud.Joke
import com.example.easycodeclientserverapp.data.cloud.JokeCloudCallback
import com.example.easycodeclientserverapp.data.dto.JokeServerModel
import com.example.easycodeclientserverapp.data.entity.BaseModelJoke
import com.example.easycodeclientserverapp.data.entity.FailedModelJoke
import com.example.easycodeclientserverapp.data.error.Error
import com.example.easycodeclientserverapp.view.ManageResources

class BaseRepository(
    private val cloudDataSource: CloudDataSource,
    private val cacheDataSource: CacheDataSource
) :
    Repository {

    private var callback: ResultCallback? = null
    private var getJokeFromCache = false

    private var jokeServerModelTemporary: Joke? = null

    override fun getJoke() {

        if (getJokeFromCache) {
            cacheDataSource.fetch(object : JokeCacheCallback {
                override fun provideJoke(joke: Joke) {
                    jokeServerModelTemporary = joke
                    callback?.provideSuccess(joke.map(Joke.Mapper.ToFavoriteUi()))
                }

                override fun provideError(error: Error) {
                    callback?.provideError(error)
                }

            })

        } else {
            cloudDataSource.fetch(object : JokeCloudCallback {
                override fun provideJokeCloud(joke: Joke) {
                    jokeServerModelTemporary = joke
                    callback?.provideSuccess(joke.map(Joke.Mapper.ToBaseUi()))
                }

                override fun provideError(error: Error) {
                    jokeServerModelTemporary = null
                    callback?.provideSuccess(FailedModelJoke(error.message()))
                }
            })
        }
    }

    override fun clear() {
        callback = null
    }

    override fun changeJokeStatus(resultCallback: ResultCallback) {
        jokeServerModelTemporary?.let {
            resultCallback.provideSuccess(it.map(Joke.Mapper.Change(cacheDataSource)))
            it.map(Joke.Mapper.Change(cacheDataSource))
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
                callback?.provideSuccess(BaseModelJoke("NEW JOKE text AHAHAHAHHAHAH!", "punchline"))
            } else if (count % 3 == 0) {
                callback?.provideSuccess(FailedModelJoke(noConnection.message()))
            } else {
                callback?.provideSuccess(FailedModelJoke(serviceUnavailable.message()))
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

