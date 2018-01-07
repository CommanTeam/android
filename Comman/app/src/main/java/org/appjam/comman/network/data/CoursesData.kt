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

}