package com.github.mich8bsp.desktop

import com.badlogic.gdx.Application
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.github.mich8bsp.Game

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration().apply {
            title = "Khet"
            width = 800
            height = 480
        }
        LwjglApplication(Game(), config).logLevel =  Application.LOG_DEBUG
    }
}