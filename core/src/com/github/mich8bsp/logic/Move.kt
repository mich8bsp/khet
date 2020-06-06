package com.github.mich8bsp.logic

interface Move

data class RotationMove(val pos: BoardPos, val direction: ERotationDirection) : Move
data class PositionMove(val from: BoardPos, val to: BoardPos): Move
data class SwitchMove(val pos1: BoardPos, val pos2: BoardPos): Move