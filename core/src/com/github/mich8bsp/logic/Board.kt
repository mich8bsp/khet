package com.github.mich8bsp.logic

class Board(rows: Int, cols: Int, piecesConfiguration: Map<BoardPos, Piece>){
    val board: Array<Array<BoardCell>> = Array(rows) { i -> Array(cols) { j ->
       BoardCell.create(BoardPos.get(i, j), piecesConfiguration[BoardPos.get(i, j)])
    } }

    fun makeMove(move: Move) {
        when(move){
            is RotationMove -> {
                val cell: BoardCell = board[move.pos.i][move.pos.j]
                cell.piece?.rotate(move.direction)
            }
            is PositionMove -> {
                val fromCell: BoardCell = board[move.from.i][move.from.j]
                val toCell: BoardCell = board[move.to.i][move.to.j]
                toCell.piece = fromCell.piece
                fromCell.piece = null
            }
            is SwitchMove -> {
                val cell1: BoardCell = board[move.pos1.i][move.pos1.j]
                val cell2: BoardCell = board[move.pos2.i][move.pos2.j]
                val tmp: Piece? = cell1.piece
                cell1.piece = cell2.piece
                cell2.piece = tmp
            }
        }
    }

}

class BoardCell(val pos: BoardPos){
    var piece: Piece? = null

    fun isEmpty(): Boolean {
        return piece == null
    }

    companion object {
        fun create(pos: BoardPos, piece: Piece?): BoardCell {
            val cell = BoardCell(pos)
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
    }
}