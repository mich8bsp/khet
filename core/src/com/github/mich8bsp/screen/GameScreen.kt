package com.github.mich8bsp.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector3
import com.github.mich8bsp.Game
import com.github.mich8bsp.logic.BoardCell
import com.github.mich8bsp.logic.EDirection
import com.github.mich8bsp.logic.ERotationDirection
import com.github.mich8bsp.logic.GameplayManager
import ktx.app.KtxScreen
import ktx.graphics.copy
import ktx.graphics.use
import ktx.log.logger

private val log = logger<GameScreen>()

class GameScreen(private val game: Game, private val gameplayManager: GameplayManager) : KtxScreen {

    private val textureManager = TextureManager()
    // The camera ensures we can render using our target resolution of 800x480
    //    pixels no matter what the screen resolution is.
    private val camera = OrthographicCamera().apply { setToOrtho(false, 850f, 680f) }
    // create the touchPos to store mouse click position
    private val touchPos = Vector3()
    private val cellSize = 85
    private val laserDurationInSec = 2f

    override fun render(delta: Float) {
        // generally good practice to update the camera's matrices once per frame
        camera.update()

        // tell the SpriteBatch to render in the coordinate system specified by the camera.
        game.batch.projectionMatrix = camera.combined

        game.batch.enableBlending()
        // begin a new batch and draw the bucket and all drops
        game.batch.use {
             gameplayManager.board.getCells().forEach { cell ->
                 renderCell(cell)
                 if(cell.laser!=null){
                     renderLaser(cell)
                     cell.reduceLaserIntensity( delta * 100f/laserDurationInSec)
                 }
             }
        }

        // process user input
        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.x.toFloat(),
                    Gdx.input.y.toFloat(),
                    0f)
            camera.unproject(touchPos)
            val cellClicked = gameplayManager.board.getCell((touchPos.y / cellSize).toInt(), (touchPos.x / cellSize).toInt())
            if(cellClicked!=null){
                gameplayManager.onCellSelected(cellClicked)
            }
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
           gameplayManager.onRotate(ERotationDirection.COUNTER_CLOCKWISE)
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            gameplayManager.onRotate(ERotationDirection.CLOCKWISE)
        }
    }

    private fun renderCell(cell: BoardCell){
        val cellX: Float = cell.pos.j.toFloat() * cellSize
        val cellY: Float = cell.pos.i.toFloat() * cellSize
        val texture: Texture? = textureManager.getTexture(cell.cellColor, cell.piece)
        val rotationDegrees: Float = when(cell.piece?.direction) {
            EDirection.UP -> 0f
            EDirection.RIGHT -> 270f
            EDirection.DOWN -> 180f
            EDirection.LEFT -> 90f
            else -> 0f
        }
        val textureRegion = TextureRegion(texture)
        val w = textureRegion.regionWidth.toFloat()
        val h = textureRegion.regionHeight.toFloat()
        game.batch.draw(textureRegion, cellX, cellY, w/2, h/2, w, h, 1f, 1f, rotationDegrees)
    }

    private fun renderLaser(cell: BoardCell){
        val cellX: Float = cell.pos.j.toFloat() * cellSize
        val cellY: Float = cell.pos.i.toFloat() * cellSize
        val texture = textureManager.laserTexture
        val textureRegion = TextureRegion(texture)
        val laserIntensity: Float = cell.laser?.intensity ?: 0f
        val preDrawColor = game.batch.color.copy()
        game.batch.color = preDrawColor.copy(alpha = laserIntensity / 100f)
        game.batch.draw(textureRegion, cellX, cellY)
        game.batch.color = preDrawColor
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