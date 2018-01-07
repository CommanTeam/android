package org.appjam.comman.network.data

/**
 * Created by KSY on 2018-01-07.
 */
object CardData {
    data class CardResponse (
//            var lectureImageUrlArr : List<String>,
//            var nextLectureID : String
            var result : ArrayList<CardInfo>
    )
    data class CardInfo(
            val lecture_id : Int,
            val title : String,
            val image_path : String,
            val image_priority : Int
    )
}