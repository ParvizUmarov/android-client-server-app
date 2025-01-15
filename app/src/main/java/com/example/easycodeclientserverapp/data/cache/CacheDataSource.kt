package com.example.easycodeclientserverapp.data.cache

import android.util.Log
import com.example.easycodeclientserverapp.data.dto.JokeCloud
import com.example.easycodeclientserverapp.data.entity.JokeUi
import com.example.easycodeclientserverapp.data.error.Error
import com.example.easycodeclientserverapp.view.ManageResources
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query

interface CacheDataSource {
    fun addOrRemove(id: Int, joke: JokeCloud): JokeUi
    fun fetch(jokeCacheCallback: JokeCacheCallback)

    class Base(private val realm: Realm, manageResources: ManageResources) : CacheDataSource {

        private val error: Error = Error.NoFavoriteJoke(manageResources)
        override fun addOrRemove(id: Int, joke: JokeCloud): JokeUi {
            realm.writeBlocking {
                val jokeRealm = this.query<JokeCache>("id == $0", id).first().find()

                if (jokeRealm == null) {
                    val newJoke = joke.toJokeRealm()
                    this.copyToRealm(newJoke)
                    joke.toFavoriteUi()
                } else {
                    delete(jokeRealm)
                    joke.toUi()
                }
            }
            return joke.toUi()
        }

        override fun fetch(jokeCacheCallback: JokeCacheCallback) {
            var jokes = realm.query(JokeCache::class).find()

            if (jokes.isEmpty())
                jokeCacheCallback.provideError(error)
            else
                jokes.random().let { joke ->
                    jokeCacheCallback.provideJoke(
                        JokeCloud(
                            joke.id,
                            joke.type,
                            joke.text,
                            joke.punchline
                        )
                    )
                }
        }

    }

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

                map.forEach {
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