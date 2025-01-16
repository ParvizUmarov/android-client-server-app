package com.example.easycodeclientserverapp.data.cache

import android.util.Log
import com.example.easycodeclientserverapp.data.cloud.Joke
import com.example.easycodeclientserverapp.data.dto.JokeServerModel
import com.example.easycodeclientserverapp.data.entity.JokeUiModel
import com.example.easycodeclientserverapp.data.error.Error
import com.example.easycodeclientserverapp.view.ManageResources
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query

interface CacheDataSource {
    fun addOrRemove(id: Int, joke: Joke): JokeUiModel
    fun fetch(jokeCacheCallback: JokeCacheCallback)

    class Base(private val realm: Realm, manageResources: ManageResources) : CacheDataSource {

        private val error: Error = Error.NoFavoriteJoke(manageResources)
        override fun addOrRemove(id: Int, joke: Joke): JokeUiModel {
            return realm.writeBlocking {
                val jokeRealm = this.query<JokeCache>("id == $0", id).first().find()

                if (jokeRealm == null) {
                    val newJoke = joke.map(Joke.Mapper.ToCache())
                    this.copyToRealm(newJoke)
                    return@writeBlocking joke.map(Joke.Mapper.ToFavoriteUi())
                } else {
                    delete(jokeRealm)
                    return@writeBlocking joke.map(Joke.Mapper.ToBaseUi())
                }
            }
        }

        override fun fetch(jokeCacheCallback: JokeCacheCallback) {
            var jokes = realm.query(JokeCache::class).find()

            if (jokes.isEmpty())
                jokeCacheCallback.provideError(error)
            else
                jokes.random().let { joke ->
                    jokeCacheCallback.provideJoke(
                        JokeServerModel(
                            joke.id,
                            joke.type,
                            joke.text,
                            joke.punchline
                        ).toJoke()
                    )
                }
        }

    }

    class Fake(manageResources: ManageResources) : CacheDataSource {

        private val error: Error = Error.NoFavoriteJoke(manageResources)

        private val map = mutableMapOf<Int, Joke>()
        override fun addOrRemove(id: Int, joke: Joke): JokeUiModel {
            if (map.containsKey(id)) {
                map.remove(id)
                return joke.map(Joke.Mapper.ToBaseUi())
            } else {
                map[id] = joke
                return joke.map(Joke.Mapper.ToFavoriteUi())

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
    fun provideJoke(joke: Joke)
    fun provideError(error: Error)

}