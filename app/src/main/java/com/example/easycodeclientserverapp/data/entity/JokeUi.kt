package com.example.easycodeclientserverapp.data.entity

import androidx.annotation.DrawableRes
import com.example.easycodeclientserverapp.R
import com.example.easycodeclientserverapp.data.callback.JokeUiCallback

class BaseJoke(text: String, punchline: String) :
    JokeUi(text, punchline,  R.drawable.ic_favorite_24px)

class FavoriteJoke(text: String, punchline: String) :
    JokeUi(text, punchline, R.drawable.ic_favorite_filled_24px)

class FailedJoke(text: String) : JokeUi(text, "", 0)

abstract class JokeUi(
    private val text: String,
    private val punchline: String,
    @DrawableRes
    private val iconResId : Int) {

    fun map(callback: JokeUiCallback) = callback.run {
        provideText("$text\n$punchline")
        provideIconRes(iconResId)
    }

}