package org.appjam.comman.network.data

/**
 * Created by KSY on 2018-01-08.
 */
object NextLectureData {
    data class NextLectureResponse(
            val resultOfCourse : Int,
            val resultOfChapter : Int
    )
}