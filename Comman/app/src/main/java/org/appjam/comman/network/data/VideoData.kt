package org.appjam.comman.network.data

/**
 * Created by RyuDongIl on 2018-01-10.
 */
object VideoData {
    data class VideoLectureResponse(
            val result : List<VideoLectureInfo>
    )

    data class VideoLectureInfo(
            val lecture_id : Int,
            val title : String,
            val info : String,
            val file_path : String,
            val video_id : String,
            val priority: Int
    )
}