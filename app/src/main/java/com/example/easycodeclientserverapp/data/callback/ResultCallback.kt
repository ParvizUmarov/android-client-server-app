package com.example.easycodeclientserverapp.data.callback

import com.example.easycodeclientserverapp.data.entity.Joke

interface ResultCallback {

    fun provideJoke(joke: Joke)
}