package com.example.easycodeclientserverapp.data.callback

import androidx.annotation.DrawableRes

interface JokeUiCallback {

    fun provideText(text: String)

    fun provideIconRes(@DrawableRes id: Int)

    class Empty : JokeUiCallback {
        override fun provideText(text: String) = Unit
        override fun provideIconRes(id: Int) = Unit
    }

}