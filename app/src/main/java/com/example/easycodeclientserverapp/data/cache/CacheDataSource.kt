package com.example.easycodeclientserverapp.data.cache

import android.util.Log
import com.example.easycodeclientserverapp.data.dto.JokeCloud
import com.example.easycodeclientserverapp.data.entity.JokeUi
import com.example.easycodeclientserverapp.data.error.Error
import com.example.easycodeclientserverapp.view.ManageResources
import kotlin.random.Random

interface CacheDataSource {
    fun addOrRemove(id: Int, joke: JokeCloud): JokeUi
    fun fetch(jokeCacheCallback: JokeCacheCallback)

    class Fake(manageResources: ManageResources) : CacheDataSource {

        private val error: Error = Error.NoFavoriteJoke(manageResources)

        private val map = mutableMapOf<Int, JokeCloud>()
        override fun addOrRemove(id: Int, joke: JokeCloud): JokeUi {
            if (map.containsKey(id)) {
                map.remove(id)
                return joke.toUi()
            } else {
                map[id] = joke
                return joke.toFavoriteUi()
            }
        }

        override fun fetch(jokeCacheCallback: JokeCacheCallback) {
            if (map.isEmpty())
                jokeCacheCallback.provideError(error)
            else {

                map.forEach{
                    Log.i("CACHEDATASOURCE KEY", "${it.key}")
                    Log.i("CACHEDATASOURCE VALUE", "${it.value}")
                }

                Log.i("CACHEDATASOURCE", "${map.toList()}")
                jokeCacheCallback.provideJoke(map.values.first())
            }

        }
    }
}

interface JokeCacheCallback {
    fun provideJoke(joke: JokeCloud)
    fun provideError(error: Error)

}