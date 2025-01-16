package com.example.easycodeclientserverapp.view

import com.example.easycodeclientserverapp.data.callback.ResultCallback
import com.example.easycodeclientserverapp.data.callback.JokeUiCallback
import com.example.easycodeclientserverapp.data.entity.FailedModelJoke
import com.example.easycodeclientserverapp.data.entity.JokeUiModel
import com.example.easycodeclientserverapp.data.error.Error
import com.example.easycodeclientserverapp.data.repository.Repository

class ViewModel(private val repository: Repository) {

    private var jokeUiCallback: JokeUiCallback = JokeUiCallback.Empty()

    private val resultCallback = object : ResultCallback {
        override fun provideSuccess(jokeUiModel: JokeUiModel) {
            jokeUiModel.map(jokeUiCallback)
        }

        override fun provideError(error: Error) {
            FailedModelJoke(error.message()).map(jokeUiCallback)
        }
    }

    fun init(callback: JokeUiCallback) {
        this.jokeUiCallback = callback
        repository.init(resultCallback)
    }

    fun getJoke() {
        repository.getJoke()
    }

    fun clear() {
        jokeUiCallback = JokeUiCallback.Empty()
        repository.clear()
    }

    fun chooseFavorite(favorite: Boolean) {
        repository.chooseFavorite(favorite)
    }

    fun changeJokeStatus() {
        repository.changeJokeStatus(resultCallback)
    }


}