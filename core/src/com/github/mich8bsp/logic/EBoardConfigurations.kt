package com.github.mich8bsp.logic

import com.github.mich8bsp.logic.BoardConfigurationFactor.Companion.buildClassic

enum class EBoardConfigurations(val configuration: Map<BoardPos, Piece>) {
    CLASSIC_GREY(buildClassic(EPlayerColor.GREY)),
    CLASSIC_RED(buildClassic(EPlayerColor.RED)),
    DYNASTY_GREY(mapOf()),
    DYNASTY_RED(mapOf()),
    IMHOTEP_GREY(mapOf()),
    IMHOTEP_RED(mapOf());

}

class BoardConfigurationFactor{
    companion object{
        fun buildClassic(playerColor: EPlayerColor): Map<BoardPos, Piece> {
            val opponentColor: EPlayerColor = playerColor.other()
            return mapOf(
                    BoardPos.get(0,2) to PyramidPiece(playerColor, EDirection.UP),
                    BoardPos.get(0,3) to AnubisPiece(playerColor),
                    BoardPos.get(0, 4) to PharaohPiece(playerColor),
                    BoardPos.get(0, 5) to AnubisPiece(playerColor),
                    BoardPos.get(0, 9) to SphinxPiece(playerColor),
                    BoardPos.get(1, 7) to PyramidPiece(playerColor, EDirection.RIGHT),
                    BoardPos.get(2, 6) to PyramidPiece(opponentColor, EDirection.DOWN),
                    BoardPos.get(3, 0) to PyramidPiece(opponentColor, EDirection.DOWN),
                    BoardPos.get(3, 2) to PyramidPiece(playerColor, EDirection.UP),
                    BoardPos.get(3, 4) to ScarabPiece(playerColor, EDirection.LEFT),
                    BoardPos.get(3, 5) to ScarabPiece(playerColor, EDirection.DOWN),
                    BoardPos.get(3, 7) to PyramidPiece(opponentColor, EDirection.RIGHT),
                    BoardPos.get(3, 9) to PyramidPiece(playerColor, EDirection.LEFT),

                    BoardPos.get(7,7) to PyramidPiece(opponentColor, EDirection.RIGHT),
                    BoardPos.get(7,6) to AnubisPiece(opponentColor, EDirection.DOWN),
                    BoardPos.get(7, 5) to PharaohPiece(opponentColor, EDirection.DOWN),
                    BoardPos.get(7, 4) to AnubisPiece(opponentColor, EDirection.DOWN),
                    BoardPos.get(7, 0) to SphinxPiece(opponentColor, EDirection.DOWN),
                    BoardPos.get(6, 2) to PyramidPiece(opponentColor, EDirection.LEFT),
                    BoardPos.get(5, 3) to PyramidPiece(playerColor, EDirection.UP),
                    BoardPos.get(4, 9) to PyramidPiece(playerColor, EDirection.UP),
                    BoardPos.get(4, 7) to PyramidPiece(opponentColor, EDirection.DOWN),
                    BoardPos.get(4, 5) to ScarabPiece(opponentColor, EDirection.RIGHT),
                    BoardPos.get(4, 4) to ScarabPiece(opponentColor, EDirection.UP),
                    BoardPos.get(4, 2) to PyramidPiece(playerColor, EDirection.LEFT),
                    BoardPos.get(4, 0) to PyramidPiece(opponentColor, EDirection.RIGHT)
            )
        }
    }
}