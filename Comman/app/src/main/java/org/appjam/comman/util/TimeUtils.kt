package org.appjam.comman.util

/**
 * Created by RyuDongIl on 2018-01-08.
 */
object TimeUtils {
    fun formatTime(millis: Int): String {
        val seconds = millis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        return (if (hours != 0) hours.toString() + " : " else "") + String.format("%02d : %02d", minutes % 60, seconds % 60)
    }
}