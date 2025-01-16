package com.example.easycodeclientserverapp.data.entity

import androidx.annotation.DrawableRes
import com.example.easycodeclientserverapp.R
import com.example.easycodeclientserverapp.data.callback.JokeUiCallback

class BaseModelJoke(text: String, punchline: String) :
    JokeUiModel(text, punchline,  R.drawable.ic_favorite_24px)

class FavoriteModelJoke(text: String, punchline: String) :
    JokeUiModel(text, punchline, R.drawable.ic_favorite_filled_24px)

class FailedModelJoke(text: String) : JokeUiModel(text, "", 0)

abstract class JokeUiModel(
    private val text: String,
    private val punchline: String,
    @DrawableRes
    private val iconResId : Int) {

    fun map(callback: JokeUiCallback) = callback.run {
        provideText("$text\n$punchline")
        provideIconRes(iconResId)
    }

}