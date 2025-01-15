package com.example.easycodeclientserverapp.data.cache

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class JokeCache : RealmObject {

    @PrimaryKey
    var id : Int = -1
    var text: String = ""
    var punchline: String = ""
    var type: String = ""

}