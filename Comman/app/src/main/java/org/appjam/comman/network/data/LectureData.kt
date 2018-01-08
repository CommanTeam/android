package org.appjam.comman.network.data

/**
 * Created by KSY on 2018-01-08.
 */
object LectureData {
    data class LectureResponse (
            val data : List<LectureInfo>
    )

    data class LectureInfo (
            val id : Int,
            val chapter_id : Int,
            val title : String,
            val lecture_type : Int,
            val file_path : String,
            val priority : Int,
            val info : String,
            val video_id : String,
            val pass_value : Int
    )
}

