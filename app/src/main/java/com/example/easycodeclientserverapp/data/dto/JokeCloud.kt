package com.example.easycodeclientserverapp.data.dto

import com.example.easycodeclientserverapp.data.cache.CacheDataSource
import com.example.easycodeclientserverapp.data.cache.JokeCache
import com.example.easycodeclientserverapp.data.entity.BaseJoke
import com.example.easycodeclientserverapp.data.entity.FavoriteJoke
import com.example.easycodeclientserverapp.data.entity.JokeUi
import com.google.gson.annotations.SerializedName

data class JokeCloud(
    @SerializedName("id")
    private val id: Int,
    @SerializedName("type")
    private val type: String,
    @SerializedName("setup")
    private val text: String,
    @SerializedName("punchline")
    private val punchline: String
) {
    fun toUi() = BaseJoke(text, punchline)
    fun toFavoriteUi() : JokeUi = FavoriteJoke(text, punchline)

    fun change(changeDataSource: CacheDataSource) : JokeUi =
        changeDataSource.addOrRemove(id, this)

    fun toJokeRealm(): JokeCache {
        return JokeCache().apply {
            id = this@JokeCloud.id
            text = this@JokeCloud.text
            punchline = this@JokeCloud.punchline
            type = this@JokeCloud.type

        }
    }
}
