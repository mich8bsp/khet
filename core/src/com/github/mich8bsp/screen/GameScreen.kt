package com.github.mich8bsp.screen


import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.TimeUtils
import com.github.mich8bsp.Game
import com.github.mich8bsp.logic.*
import ktx.app.KtxScreen
import ktx.collections.iterate
import ktx.graphics.use
import ktx.log.logger

private val log = logger<GameScreen>()

class GameScreen(private val game: Game) : KtxScreen {

    private val textureManager = TextureManager()
    // The camera ensures we can render using our target resolution of 800x480
    //    pixels no matter what the screen resolution is.
    private val camera = OrthographicCamera().apply { setToOrtho(false, 850f, 680f) }
    // create the touchPos to store mouse click position
    private val touchPos = Vector3()
    private val cellSize = 85

    private val playerColor: EPieceColor = EPieceColor.GREY
    private val piecesConfiguration: Map<BoardPos, Piece> = when(playerColor){
        EPieceColor.GREY -> EBoardConfigurations.CLASSIC_GREY.configuration
        EPieceColor.RED -> EBoardConfigurations.CLASSIC_RED.configuration
    }
    private val board: Board = Board(8, 10, piecesConfiguration, playerColor)

    override fun render(delta: Float) {
        // generally good practice to update the camera's matrices once per frame
        camera.update()

        // tell the SpriteBatch to render in the coordinate system specified by the camera.
        game.batch.projectionMatrix = camera.combined

        // begin a new batch and draw the bucket and all drops
        game.batch.use {
             board.getCells().forEach { cell ->
                 game.batch.draw(textureManager.getTexture(cell.cellColor, cell.piece), cell.pos.j.toFloat() * cellSize, cell.pos.i.toFloat() * cellSize)
             }
        }

        // process user input
        if (Gdx.input.isTouched) {
//            touchPos.set(Gdx.input.x.toFloat(),
//                    Gdx.input.y.toFloat(),
//                    0f)
//            camera.unproject(touchPos)
//            bucket.x = touchPos.x - 64f / 2f
        }


        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            // getDeltaTime returns the time passed between the last and the current frame in seconds
//            bucket.x -= 200 * delta
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            bucket.x += 200 * delta
        }
    }

    override fun show() {
        // start the playback of the background music when the screen is shown
//        rainMusic.play()
    }

    override fun dispose() {
        log.debug { "Disposing ${this.javaClass.simpleName}" }
        textureManager.dispose()
//        dropSound.dispose()
//        rainMusic.dispose()
    }
}