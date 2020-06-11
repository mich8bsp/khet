package com.github.mich8bsp.multiplayer

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.mich8bsp.logic.Player
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.utils.EmptyContent
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

object GameServerClient {
    private val client = HttpClient()
    private const val serverURL = "floating-temple-17106.herokuapp.com"
    private val objectMapper = jacksonObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)

    fun joinGameAsync(): Deferred<Player> {
        return GlobalScope.async {
            objectMapper.readValue<Player>(postRequest("/game/join"))
        }
    }

    private suspend fun postRequest(url: String, body: Any = EmptyContent): String {
        return client.post<String>(scheme = "https", host = serverURL, path = url, body = body)
    }
}