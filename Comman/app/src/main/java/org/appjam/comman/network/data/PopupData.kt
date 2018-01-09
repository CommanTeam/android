package org.appjam.comman.network.data

/**
 * Created by ChoGyuJin on 2018-01-08.
 */
object PopupData {
    data class PopupTitleResponse(
            val result : List<PopupTitleInfo>
    )

    data class PopupTitleInfo(
            val id : Int,
            val supplier_id: Int,
            val opened_chapter : Int,
            val image_path : String,
            val name : String,
            val supplier_thumbnail : String,
            val title : String,
            val info : String,
            val price : Int,
            val category_id : Int
    )

    data class PopupContentResponse (val result : List<PopupContentInfo>)

    data class PopupContentInfo(
            val chapterID : Int,
            val title : String,
            val info : String,
            val priority : Int,
            val open : Boolean
    )
}