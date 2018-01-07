package org.appjam.comman.network.data

/**
 * Created by junhoe on 2018. 1. 4..
 */
object CoursesData {

    data class CoursesResponse(val result : List<CourseInfo>)

    data class CourseInfo(val courseTitle: String,
                          val courseImage: String,
                          val chapterCnt: Int,
                          val progressPercentage: Int)
    data class MyCourseAlarmItem(
            val myCourseProfile : Int,
            val myCourseAlarm : String
    )

    data class CourseMetaResponse(val result: List<CourseMetadata>)

    data class CourseMetadata(val id: Int,
                              val supplier_id: Int,
                              val opened_chapter: Int,
                              val image_path: String,
                              val name: String,
                              val title: String,
                              val info: String,
                              val price: Int,
                              val category_id: Int)


}