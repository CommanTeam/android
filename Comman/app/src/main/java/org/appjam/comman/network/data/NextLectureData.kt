package org.appjam.comman.network.data

/**
 * Created by KSY on 2018-01-08.
 */
object NextLectureData {
    data class NextLectureResponse(
            val nextLectureOfCourse : NextLectureInfo,
            val nextLectureOfChapter : Int
    )

    data class NextLectureInfo(
            val lectureID : Int,
            val lectureType : Int,
            val purchaseFlag : Int
    )
}