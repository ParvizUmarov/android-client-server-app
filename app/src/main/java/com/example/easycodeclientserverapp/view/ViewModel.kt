package com.example.easycodeclientserverapp.view

import com.example.easycodeclientserverapp.data.callback.ResultCallback
import com.example.easycodeclientserverapp.data.callback.DataCallback
import com.example.easycodeclientserverapp.data.entity.Joke
import com.example.easycodeclientserverapp.data.error.Error
import com.example.easycodeclientserverapp.data.repository.Repository

class ViewModel(private val model: Repository) {

    private var callback: DataCallback = DataCallback.Empty()

    private val resultCallback = object : ResultCallback {
        override fun provideJoke(joke: Joke) {
            callback.let {
                joke.map(it)
            }
        }
    }

    fun init(callback: DataCallback) {
        this.callback = callback
        model.init(resultCallback)
    }

    fun getJoke() {
        model.getJoke()
    }

    fun clear() {
        callback = DataCallback.Empty()
        model.clear()
    }

    fun changeJokeStatus() {
    }


}