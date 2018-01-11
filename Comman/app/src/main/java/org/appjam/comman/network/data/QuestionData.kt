package org.appjam.comman.network.data

/**
 * Created by RyuDongIl on 2018-01-11.
 */
object QuestionData {
    data class QuestionResponse (
            val result : List<QuestionInfo>
    )

    data class QuestionInfo (
            val lq_id : Int,
            val l_question_user_nickname : String,
            val l_question_lecture_id : Int,
            val question_text : String,
            val l_question_date : String,
            val l_question_flag : Int,
            val l_answer_id : Int,
            val l_answer_question_id : Int,
            val l_answer_text : String,
            val l_answer_date : String,
            val supplier_name : String
    )

    data class QuestionPost (
            val lectureID : Int,
            val question_text : String
    )

    data class RegisterQuestionResponse (
            val result : RegisterInfo
    )

    data class RegisterInfo (
            val id : Int,
            val user_id : String,
            val lecture_id : Int,
            val question_text : String,
            val question_date : String,
            val flag : Int
    )

}