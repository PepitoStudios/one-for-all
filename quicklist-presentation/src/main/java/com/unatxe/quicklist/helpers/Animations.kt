package com.unatxe.quicklist.helpers

object Animations {

    fun processNewRotation(lastRotation: Int, rotation: Int): Int {
        val modLast = if (lastRotation > 0) lastRotation % 360 else 360 - (-lastRotation % 360)

        if (modLast != rotation) {
            val backward = if (rotation > modLast) {
                modLast + 360 - rotation
            } else {
                modLast - rotation
            }
            val forward = if (rotation > modLast) {
                rotation - modLast
            } else {
                360 - modLast + rotation
            }
            return if (backward < forward) {
                lastRotation - backward
            } else {
                lastRotation + forward
            }
        } else {
            return lastRotation
        }
    }
}
