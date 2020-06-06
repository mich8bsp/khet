package com.github.mich8bsp.logic

import java.lang.Exception

interface Piece {
    var health: Int
    fun isDead(): Boolean = health <= 0
    fun hitWithRay(hitFromDirection: EDirection): EDirection?
    fun rotate(rotationDir: ERotationDirection)
}

class PharaohPiece : Piece {
    override var health: Int = 1
    var directionFront: EDirection = EDirection.UP

    override fun hitWithRay(hitFromDirection: EDirection): EDirection? {
        health--
        return null
    }

    override fun rotate(rotationDir: ERotationDirection) {
        directionFront = directionFront.rotate(rotationDir)
    }
}

class ScarabPiece : Piece {
    override var health: Int = 1
    var orientationLR: Boolean = false

    override fun hitWithRay(hitFromDirection: EDirection): EDirection? =
        Mirror.reflect(hitFromDirection, orientationLR)


    override fun rotate(rotationDir: ERotationDirection) {
        orientationLR = !orientationLR
    }
}

class PyramidPiece : Piece {
    override var health: Int = 1

    var orientationRightReflection: EDirection = EDirection.RIGHT

    override fun hitWithRay(hitFromDirection: EDirection): EDirection? {
        val orientationLR: Boolean = when(orientationRightReflection){
            EDirection.UP -> false
            EDirection.RIGHT -> true
            EDirection.LEFT -> true
            EDirection.DOWN -> false
        }
        val reflected: EDirection = Mirror.reflect(hitFromDirection, orientationLR)
        if(hitFromDirection == orientationRightReflection || reflected == orientationRightReflection){
            return reflected
        }else{
            health--
            return null
        }
    }

    override fun rotate(rotationDir: ERotationDirection) {
        orientationRightReflection = orientationRightReflection.rotate(rotationDir)
    }

}

class AnubisPiece : Piece {
    override var health: Int = 1
    var directionFront: EDirection = EDirection.UP

    override fun hitWithRay(hitFromDirection: EDirection): EDirection? {
        if(hitFromDirection == directionFront){
            health--
        }
        return null;
    }

    override fun rotate(rotationDir: ERotationDirection) {
        directionFront = directionFront.rotate(rotationDir)
    }

}

class SphinxPiece : Piece {
    var facingDirection: EDirection = EDirection.UP

    override var health: Int = 1
    override fun hitWithRay(hitFromDirection: EDirection): EDirection? = null

    override fun rotate(rotationDir: ERotationDirection) {
        val rotated = facingDirection.rotate(rotationDir)
        if(rotated == EDirection.DOWN || rotated == EDirection.RIGHT){
            throw Exception("A Sphinx can't be rotated away from the board")
        }
        facingDirection = rotated
    }

}

class Mirror {
    companion object {
        fun reflect(rayDir: EDirection, orientationLR: Boolean): EDirection {
            val reflected = when (rayDir) {
                EDirection.DOWN -> EDirection.LEFT
                EDirection.UP -> EDirection.RIGHT
                EDirection.LEFT -> EDirection.DOWN
                EDirection.RIGHT -> EDirection.UP
            }
            return if (orientationLR) reflected else reflected.reverse()
        }

    }
}