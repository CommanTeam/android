package org.appjam.comman.network.data

/**
 * Created by KSY on 2018-01-07.
 */
object CardData {
    data class CardResponse (
            val lectureImageUrlArr : List<String>,
            val nextLectureID : Int
    )
}