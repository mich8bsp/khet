package com.github.mich8bsp.logic

import kotlin.math.abs

class Board(rows: Int, cols: Int, piecesConfiguration: Map<BoardPos, Piece>, playerColor: EPieceColor){
    private val cells: Array<Array<BoardCell>> = Array(rows) { i -> Array(cols) { j ->
       BoardCell.create(BoardPos.get(i, j), piecesConfiguration[BoardPos.get(i, j)], rows, cols, playerColor)
    } }

    fun makeMove(move: Move) {
        when(move){
            is RotationMove -> {
                val cell: BoardCell = cells[move.pos.i][move.pos.j]
                cell.piece?.rotate(move.direction)
            }
            is PositionMove -> {
                val fromCell: BoardCell = cells[move.from.i][move.from.j]
                val toCell: BoardCell = cells[move.to.i][move.to.j]
                toCell.piece = fromCell.piece
                fromCell.piece = null
            }
            is SwitchMove -> {
                val cell1: BoardCell = cells[move.pos1.i][move.pos1.j]
                val cell2: BoardCell = cells[move.pos2.i][move.pos2.j]
                val tmp: Piece? = cell1.piece
                cell1.piece = cell2.piece
                cell2.piece = tmp
            }
        }
    }

    fun getCells(): List<BoardCell> {
        return cells.flatten()
    }

    fun getCell(x: Int, y: Int): BoardCell {
        return cells[x][y]
    }

    fun getCell(pos: BoardPos): BoardCell {
        return getCell(pos.i, pos.j)
    }
}

class BoardCell(val pos: BoardPos, val cellColor: EPieceColor?){
    var piece: Piece? = null

    fun isEmpty(): Boolean {
        return piece == null
    }

    companion object {
        private fun getEmptyCellColor(pos: BoardPos, boardRows: Int, boardCols: Int, playerColor: EPieceColor): EPieceColor? {
            if(pos.j == boardCols-1){
                return playerColor
            }
            if(pos.j == 0){
                return playerColor.other()
            }
            if(pos.j == 1 && (pos.i == 0 || pos.i == boardRows-1)){
                return playerColor
            }
            if(pos.j == boardCols-2 && (pos.i == 0 || pos.i == boardRows-1)){
                return playerColor.other()
            }
            return null;
        }

        fun create(pos: BoardPos, piece: Piece?, boardRows: Int, boardCols: Int, playerColor: EPieceColor): BoardCell {
            val cellColor: EPieceColor? = if(piece==null){
               getEmptyCellColor(pos, boardRows, boardCols, playerColor)
            }else{
                null
            }

            val cell = BoardCell(pos, cellColor)
            if(piece!=null){
                cell.piece = piece
            }
            return cell
        }
    }
}

data class BoardPos(val i: Int, val j: Int){
    companion object {
        fun get(i: Int, j: Int): BoardPos = BoardPos(i, j) //object pooling would be a good idea here
        fun areNeighbors(pos1: BoardPos, pos2: BoardPos): Boolean {
            val diffX = abs(pos1.i - pos2.i)
            val diffY = abs(pos1.j - pos2.j)
            return (diffX + diffY > 0) && diffX<=1 && diffY<=1
        }
    }
}