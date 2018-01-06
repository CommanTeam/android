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

    data class SearchedCoursesResponse(val result : List<SearchedCourseInfo>)

    data class SearchedCourseInfo(val id: Int,
                                  val title: String,
                                  val info: String,
                                  val image_path: String,
                                  val hit: Int)

}