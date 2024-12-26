package com.example.easycodeclientserverapp.data.cache

import com.example.easycodeclientserverapp.data.dto.JokeDTO
import com.example.easycodeclientserverapp.data.entity.BaseJoke
import com.example.easycodeclientserverapp.data.repository.Repository

interface CacheDataSource  {

    fun addOrRemove(id: Int, joke: JokeDTO) : BaseJoke

}