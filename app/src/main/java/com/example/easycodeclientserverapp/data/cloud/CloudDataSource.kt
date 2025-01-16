package com.example.easycodeclientserverapp.data.cloud

import com.example.easycodeclientserverapp.data.dto.JokeServerModel
import com.example.easycodeclientserverapp.view.ManageResources
import retrofit2.Call
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException

interface CloudDataSource {

    fun fetch(cloudCallback: JokeCloudCallback)

    class Base(
        private val jokeService: JokeService,
        private val manageResources: ManageResources
    ) : CloudDataSource {


        private var noConnection =
            com.example.easycodeclientserverapp.data.error.Error.NoConnection(manageResources)
        private var serviceUnavailable =
            com.example.easycodeclientserverapp.data.error.Error.ServiceUnavailable(manageResources)

        override fun fetch(cloudCallback: JokeCloudCallback) {
            jokeService.getJoke().enqueue(object : retrofit2.Callback<JokeServerModel> {

                override fun onResponse(call: Call<JokeServerModel>, response: Response<JokeServerModel>) {

                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null)
                            cloudCallback.provideJokeCloud(body.toJoke())
                         else
                            cloudCallback.provideError(serviceUnavailable)
                    } else {
                        cloudCallback.provideError(serviceUnavailable)
                    }
                }

                override fun onFailure(p0: Call<JokeServerModel>, t: Throwable) {
                    cloudCallback.provideError(
                        if (t is UnknownHostException || t is ConnectException)
                            noConnection
                        else
                            serviceUnavailable
                    )
                }
            })
        }
    }
}

interface JokeCloudCallback {

    fun provideJokeCloud(joke: Joke)

    fun provideError(error: com.example.easycodeclientserverapp.data.error.Error)

}