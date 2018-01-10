package org.appjam.comman.network.data

/**
 * Created by KSY on 2018-01-08.
 */
object LectureData {
    data class LectureResponse (
            val data : LectureInfo
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

    data class RecentLectureResponse (
            val result : RecentLectureInfo
    )

    data class RecentLectureInfo (
            val course_ID : Int,
            val course_title : String,
            val chapter_priority : Int,
            val chapter_ID : Int,
            val lecture_title : String,
            val lecture_priority : Int,
            val lecture_type: Int,
            val cnt_lecture_quiz : Int,
            val cnt_lecture_picture : Int
    )

    data class FinishLecture(
            val message : String
    )
}


