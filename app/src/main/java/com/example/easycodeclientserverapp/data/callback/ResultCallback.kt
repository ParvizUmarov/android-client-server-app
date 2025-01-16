package com.example.easycodeclientserverapp.data.callback

import com.example.easycodeclientserverapp.data.entity.JokeUiModel
import com.example.easycodeclientserverapp.data.error.Error

interface ResultCallback {
    fun provideSuccess(jokeUiModel: JokeUiModel)
    fun provideError(error: Error)
}