package com.github.mich8bsp.logic

class GameplayManager {

    private val playerColor: EPieceColor = EPieceColor.GREY
    private val piecesConfiguration: Map<BoardPos, Piece> = when (playerColor) {
        EPieceColor.GREY -> EBoardConfigurations.CLASSIC_GREY.configuration
        EPieceColor.RED -> EBoardConfigurations.CLASSIC_RED.configuration
    }

    val board: Board = Board(8, 10, piecesConfiguration, playerColor)
    private val moveValidator = MoveValidator(board)

    private var cellSelected: BoardCell? = null

    fun onCellSelected(cell: BoardCell) {
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
                }else{
                    println("Invalid move")
                }
                null
            }
        }

    }

    fun onRotate(direction: ERotationDirection) {
        val isRotationAllowed = cellSelected!=null && cellSelected?.piece!=null && cellSelected?.piece?.color == playerColor
        if(isRotationAllowed){
            val move = RotationMove(cellSelected!!.pos, direction)
            if(moveValidator.validateMove(move)){
                board.makeMove(move)
                cellSelected = null
            }else{
                println("Invalid move")
            }
        }
    }

}