package com.example.easycodeclientserverapp.data.dto

import com.example.easycodeclientserverapp.data.cache.CacheDataSource
import com.example.easycodeclientserverapp.data.cache.JokeCache
import com.example.easycodeclientserverapp.data.cloud.Joke
import com.example.easycodeclientserverapp.data.entity.BaseModelJoke
import com.example.easycodeclientserverapp.data.entity.FavoriteModelJoke
import com.example.easycodeclientserverapp.data.entity.JokeUiModel
import com.google.gson.annotations.SerializedName

data class JokeServerModel(
    @SerializedName("id")
    private val id: Int,
    @SerializedName("type")
    private val type: String,
    @SerializedName("setup")
    private val text: String,
    @SerializedName("punchline")
    private val punchline: String
) {

    fun toJoke() = Joke(id, type, text, punchline)

}
