package org.appjam.comman.network.data

/**
 * Created by RyuDongIl on 2018-01-07.
 */
object CategoryData {
    data class CategoryResponse (
            val result : List<CategoryInfo>
    )

    data class CategoryInfo (
            val categoryID : Int,
            val categoryName : String,
            val categoryImg : String,
            val title : List<String>
    )

    data class CoursesOfCategoryResponse(
            val result : List<CoursesOfCategory>
    )

    data class CoursesOfCategory(
            val id : Int,
            val title : String,
            val info : String,
            val image_path : String,
            val hit : Int
    )

}
