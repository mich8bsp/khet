package com.github.mich8bsp.screen


import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.github.mich8bsp.Game
import ktx.app.KtxScreen
import ktx.graphics.use

class MainMenuScreen(private val game: Game) : KtxScreen {
    private val camera: OrthographicCamera = OrthographicCamera().apply { setToOrtho(false, 800f, 400f) }

    override fun render(delta: Float) {
        camera.update();
        game.batch.projectionMatrix = camera.combined;

        game.batch.use {
            game.font.draw(game.batch, "Welcome to Khet!!! ", 100f, 150f);
            game.font.draw(game.batch, "Tap anywhere to begin!", 100f, 100f);
        }

        if (Gdx.input.isTouched) {
            game.addScreen(GameScreen(game))
            game.setScreen<GameScreen>();
            game.removeScreen<MainMenuScreen>()
            dispose();
        }
    }
}