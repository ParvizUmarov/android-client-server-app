package com.example.easycodeclientserverapp.data.dto

import com.example.easycodeclientserverapp.data.cache.CacheDataSource
import com.example.easycodeclientserverapp.data.entity.BaseJoke
import com.example.easycodeclientserverapp.data.entity.Joke
import com.google.gson.annotations.SerializedName

data class JokeDTO(
    @SerializedName("id")
    private val id: Int,
    @SerializedName("type")
    private val type: String,
    @SerializedName("setup")
    private val text: String,
    @SerializedName("punchline")
    private val punchline: String
) {
    fun toJoke() = BaseJoke(text, punchline)

    fun change(changeDataSource: CacheDataSource) : BaseJoke =
        changeDataSource.addOrRemove(id, this)
}
