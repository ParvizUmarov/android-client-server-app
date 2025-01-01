package com.example.easycodeclientserverapp.data.cloud

import com.example.easycodeclientserverapp.data.dto.JokeCloud
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
            jokeService.getJoke().enqueue(object : retrofit2.Callback<JokeCloud> {

                override fun onResponse(call: Call<JokeCloud>, response: Response<JokeCloud>) {

                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null)
                            cloudCallback.provideJokeCloud(body)
                         else
                            cloudCallback.provideError(serviceUnavailable)
                    } else {
                        cloudCallback.provideError(serviceUnavailable)
                    }
                }

                override fun onFailure(p0: Call<JokeCloud>, t: Throwable) {
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

    fun provideJokeCloud(jokeCloud: JokeCloud)

    fun provideError(error: com.example.easycodeclientserverapp.data.error.Error)

}