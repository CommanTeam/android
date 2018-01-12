package org.appjam.comman.network.data

/**
 * Created by junhoe on 2018. 1. 4..
 */
object CoursesData {

    const val COURSE_ID_KEY = "courseID"
    data class CoursesResponse(val result : List<CourseInfo>)

    data class CourseInfo(
            val courseID : Int,
            val imagePath: String,
            val courseTitle : String,
            val chapterCnt: Int,
            val progressPercentage: Int)

    data class CourseMetaResponse(val result: CourseMetadata)

    data class CourseMetadata(val id: Int,
                              val supplier_id: Int,
                              val opened_chapter: Int,
                              val image_path: String,
                              val name: String,
                              val supplier_thumbnail: String,
                              val title: String,
                              val info: String,
                              val price: Int,
                              val category_id: Int)

    data class CheckRegistered(
            val result : Int
    )

    data class CheckPurchased(
            val result : Int
    )

    data class PurchaseCourse(
            val message : String
    )

    data class RegisterPost(
            val courseID : Int
    )

    data class RegisterCourse(
            val message : String
    )


}