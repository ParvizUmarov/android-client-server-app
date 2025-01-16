package com.example.easycodeclientserverapp.data.cloud

import com.example.easycodeclientserverapp.data.cache.CacheDataSource
import com.example.easycodeclientserverapp.data.cache.JokeCache
import com.example.easycodeclientserverapp.data.entity.BaseModelJoke
import com.example.easycodeclientserverapp.data.entity.FavoriteModelJoke
import com.example.easycodeclientserverapp.data.entity.JokeUiModel
import com.google.gson.annotations.SerializedName

data class Joke(
    private val id: Int,
    private val type: String,
    private val text: String,
    private val punchline: String
) {

    fun <T> map(mapper: Mapper<T>): T = mapper.map(type, text, punchline, id)

    interface Mapper<T> {
        fun map(type: String, mainText: String, punchline: String, id: Int): T

        class ToCache : Mapper<JokeCache> {
            override fun map(
                type: String,
                mainText: String,
                punchline: String,
                id: Int
            ): JokeCache {
                val jokeCache = JokeCache()
                jokeCache.id = id
                jokeCache.text = mainText
                jokeCache.punchline = punchline
                jokeCache.type = type
                return jokeCache
            }
        }

        class ToBaseUi : Mapper<JokeUiModel> {
            override fun map(
                type: String,
                mainText: String,
                punchline: String,
                id: Int
            ): JokeUiModel {
                return BaseModelJoke(mainText, punchline)
            }
        }

        class ToFavoriteUi : Mapper<JokeUiModel> {
            override fun map(
                type: String,
                mainText: String,
                punchline: String,
                id: Int
            ): JokeUiModel {
                return FavoriteModelJoke(mainText, punchline);
            }
        }

        class Change(private val cacheDataSource: CacheDataSource) : Mapper<JokeUiModel> {
            override fun map(
                type: String,
                mainText: String,
                punchline: String,
                id: Int
            ): JokeUiModel {
                return cacheDataSource.addOrRemove(id, Joke(id, type, mainText, punchline))
            }

        }

    }

    fun toFavoriteUi(): JokeUiModel = FavoriteModelJoke(text, punchline)
}



