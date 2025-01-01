package com.example.easycodeclientserverapp.data.callback

import com.example.easycodeclientserverapp.data.entity.JokeUi
import com.example.easycodeclientserverapp.data.error.Error

interface ResultCallback {
    fun provideSuccess(jokeUi: JokeUi)
    fun provideError(error: Error)
}