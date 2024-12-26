package com.example.easycodeclientserverapp.data.repository

import com.example.easycodeclientserverapp.data.callback.ResultCallback
import com.example.easycodeclientserverapp.data.cloud.CloudDataSource
import com.example.easycodeclientserverapp.data.cloud.JokeCloudCallback
import com.example.easycodeclientserverapp.data.dto.JokeDTO
import com.example.easycodeclientserverapp.data.entity.BaseJoke
import com.example.easycodeclientserverapp.data.entity.FailedJoke
import com.example.easycodeclientserverapp.data.error.Error
import com.example.easycodeclientserverapp.view.ManageResources
import com.example.easycodeclientserverapp.data.cloud.JokeService
import retrofit2.Call
import retrofit2.Response
import java.net.UnknownHostException

class BaseRepository(
    private val cloudDataSource: CloudDataSource) :
    Repository{

    private var callback: ResultCallback? = null

    private var jokeCloudCached: JokeDTO? = null

    override fun getJoke() {
        cloudDataSource.fetch(object : JokeCloudCallback {
            override fun provideJokeCloud(jokeDto: JokeDTO) {
                jokeCloudCached = jokeDto
                callback?.provideJoke(jokeDto.toJoke())
            }

            override fun provideError(error: Error) {
                jokeCloudCached = null
                callback?.provideJoke(FailedJoke(error.message()))
            }
        })
    }

    override fun clear() {
        callback = null
    }

    override fun init(callback: ResultCallback) {
        this.callback = callback
    }

}

class TestModel(manageResources: ManageResources) : Repository {

    private var callback: ResultCallback? = null
    private var noConnection = Error.NoConnection(manageResources)
    private var serviceUnavailable = Error.ServiceUnavailable(manageResources)

    private var count = 1

    override fun getJoke() {
        Thread {
            Thread.sleep(2000)
            if (count % 2 == 1) {
                callback?.provideJoke(BaseJoke("NEW JOKE text AHAHAHAHHAHAH!", "punchline"))
            } else if (count % 3 == 0) {
                callback?.provideJoke(FailedJoke(noConnection.message()))
            } else {
                callback?.provideJoke(FailedJoke(serviceUnavailable.message()))
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
}

interface Repository {
    fun getJoke()

    fun init(callback: ResultCallback)

    fun clear()
}

