package com.example.easycodeclientserverapp.data.entity

import androidx.annotation.DrawableRes
import com.example.easycodeclientserverapp.R
import com.example.easycodeclientserverapp.data.callback.DataCallback
import com.example.easycodeclientserverapp.data.callback.ResultCallback

class BaseJoke(text: String, punchline: String) :
    Joke(text, punchline,  R.drawable.ic_favorite_24px)

class FavoriteJoke(text: String, punchline: String) :
    Joke(text, punchline, R.drawable.ic_favorite_filled_24px)

class FailedJoke(text: String) : Joke(text, "", 0)

abstract class Joke(
    private val text: String,
    private val punchline: String,
    @DrawableRes
    private val iconResId : Int) {

    fun map(callback: DataCallback) = callback.run {
        provideText("$text\n$punchline")
        provideIconRes(iconResId)
    }

}