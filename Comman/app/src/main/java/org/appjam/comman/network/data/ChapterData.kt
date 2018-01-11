package org.appjam.comman.network.data

/**
 * Created by ChoGyuJin on 2018-01-07.
 */
object ChapterData {
    const val CHAPTER_ID_KEY = "chapterID"
    data class InfoResponse(
            val data: ChapterInfo
    )

    data class ChapterInfo(val id: Int,
                           val course_id: Int,
                           val info: String,
                           val title: String,
                           val priority: Int)


    data class LectureListInChapterResponse(val result : List<LectureListInChapterData>)

    data class LectureListInChapterData(val lectureID : Int,
                                        val lecturePriority: Int,
                                        val lectureTitle: String,
                                        val lectureType : Int,
                                        val chapterID: Int,
                                        val playTime : Int,
                                        val watchedFlag: Int,
                                        val lectureCnt: Int

    )

}