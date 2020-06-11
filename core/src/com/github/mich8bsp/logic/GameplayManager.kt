package com.github.mich8bsp.logic

import com.github.mich8bsp.multiplayer.GameServerClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch

class GameplayManager(val player: Player) {

    private var gameOver: Boolean = false
    private val playerColor: EPlayerColor = player.color
    private val piecesConfiguration: Map<BoardPos, Piece> = when (playerColor) {
        EPlayerColor.GREY -> EBoardConfigurations.CLASSIC_GREY.configuration
        EPlayerColor.RED -> EBoardConfigurations.CLASSIC_RED.configuration
    }

    private var currPlayerToMove: EPlayerColor = EPlayerColor.GREY

    val board: Board = Board(8, 10, piecesConfiguration, playerColor)
    private val moveValidator = MoveValidator(board)
    private val latestMoveLock = Object()
    private var latestMoveFromServer: MoveRecord<out Move>? = null
    private var lastMovePlayedOutId: Long = 0


    private var cellSelected: BoardCell? = null

    fun connect(): GameplayManager {
        val tickerChannel = ticker(delayMillis = 5_000, initialDelayMillis = 0)

        GlobalScope.launch {
            for (event in tickerChannel) {
                println("checking latest move from server")
                val latestMoveRecord = GameServerClient.getLatestMove(player.playerId).await()
                if (latestMoveRecord != null) {
                    synchronized(latestMoveLock){
                        val isNewMove: Boolean = latestMoveFromServer == null || latestMoveFromServer!!.moveId < latestMoveRecord.moveId
                        if (isNewMove) {
                            latestMoveFromServer = latestMoveRecord
                        }
                    }
                }
            }
        }
        return this
    }

    fun playOutOpponentMove() {
        synchronized(latestMoveLock) {
            val isOpponentMove: Boolean = latestMoveFromServer?.playerColor == playerColor.other()
            val isNewMove: Boolean = latestMoveFromServer != null && latestMoveFromServer!!.moveId > lastMovePlayedOutId
            val shouldPlayMove: Boolean = isOpponentMove && isNewMove
            if (shouldPlayMove) {
                board.makeMove(latestMoveFromServer!!.move)
                lastMovePlayedOutId = latestMoveFromServer!!.moveId
                onMoveFinished(latestMoveFromServer!!.playerColor)
            }
        }
    }

    fun onCellSelected(cell: BoardCell) {
        if (gameOver || (currPlayerToMove != playerColor)) {
            return
        }
        cellSelected = when (cellSelected) {
            cell -> {
                null
            }
            null -> {
                if (cell.piece != null && cell.piece?.color == playerColor) {
                    cell
                } else {
                    null
                }
            }
            else -> {
                val move = if (cellSelected?.piece is ScarabPiece && !cell.isEmpty()) {
                    SwitchMove(cellSelected!!.pos, cell.pos)
                } else {
                    PositionMove(cellSelected!!.pos, cell.pos)
                }
                if (moveValidator.validateMove(move)) {
                    board.makeMove(move)
                    GameServerClient.sendMove(move, player.playerId)
                    onMoveFinished(playerColor)
                } else {
                    println("Invalid move")
                }
                null
            }
        }

    }

    fun onRotate(direction: ERotationDirection) {
        if (gameOver || (currPlayerToMove != playerColor)) {
            return
        }
        val isRotationAllowed = cellSelected != null && cellSelected?.piece != null && cellSelected?.piece?.color == playerColor
        if (isRotationAllowed) {
            val move = RotationMove(cellSelected!!.pos, direction)
            if (moveValidator.validateMove(move)) {
                board.makeMove(move)
                GameServerClient.sendMove(move, player.playerId)
                onMoveFinished(playerColor)
            } else {
                println("Invalid move")
            }
        }
    }

    fun onMoveFinished(moveOfPlayer: EPlayerColor) {
        cellSelected = null
        board.fireLaser(playerColor)
        if (board.isPharaohDead()) {
            val winner = board.getWinner()
            println("Game Over! $winner won!")
            gameOver = true
        }
        currPlayerToMove = moveOfPlayer.other()
    }

}