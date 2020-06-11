package com.github.mich8bsp.multiplayer

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.mich8bsp.logic.*
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.utils.EmptyContent
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*

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

    fun sendMove(move: Move, playerId: UUID) {
        val body = objectMapper.writeValueAsString(MoveRequest(move, playerId))
        GlobalScope.async {
            val path = when(move){
                is RotationMove -> "make_rot_move"
                is PositionMove -> "make_pos_move"
                is SwitchMove -> "make_switch_move"
                else -> throw Exception("unrecognized move type $move")
            }
            postRequest("/game/$path", body)
        }
    }

    fun getLatestMove(playerId: UUID): Deferred<MoveRecord<out Move>?> {
        return GlobalScope.async {
            val response = getRequest("/game/get_latest_move?player_id=$playerId")
            val responseAsTypedMove = objectMapper.readValue<MoveTypeResponse>(response)
            when(responseAsTypedMove.moveType){
                EMoveType.NONE -> null
                EMoveType.POSITION -> objectMapper.readValue<MoveRecord<PositionMove>>(response)
                EMoveType.ROTATE -> objectMapper.readValue<MoveRecord<RotationMove>>(response)
                EMoveType.SWITCH -> objectMapper.readValue<MoveRecord<SwitchMove>>(response)
            }
        }
    }

    private suspend fun postRequest(url: String, body: Any = EmptyContent): String {
        return client.post<String>(scheme = "https", host = serverURL, path = url, body = body)
    }

    private suspend fun getRequest(url: String): String {
        return client.get<String>(scheme = "https", host = serverURL, path = url)
    }
}

data class MoveRequest(val move: Move, val playerId: UUID)
data class MoveTypeResponse(val moveType: EMoveType)