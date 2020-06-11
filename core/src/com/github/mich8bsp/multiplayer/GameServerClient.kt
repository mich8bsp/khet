package com.github.mich8bsp.multiplayer

import com.github.mich8bsp.logic.EPlayerColor
import com.github.mich8bsp.logic.Player
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.function.Supplier


object GameServerClient {
    fun joinGame(): CompletableFuture<Player> {
        //TODO: connect to server
        return CompletableFuture.supplyAsync(Supplier{
            Player(UUID.randomUUID(), UUID.randomUUID(), EPlayerColor.GREY)
        },  Executors.newCachedThreadPool())
    }
}