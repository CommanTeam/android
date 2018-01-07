package org.appjam.comman.network.data

/**
 * Created by RyuDongIl on 2018-01-06.
 */
object SearchedCoursesData {

    data class SearchedcoursesPost(val search : String)

    data class SearchedCoursesResponse(val result : List<SearchedCourseInfo>)

    data class SearchedCourseInfo(val id: Int,
                                  val title: String,
                                  val info: String,
                                  val image_path: String,
                                  val hit: Int)

}