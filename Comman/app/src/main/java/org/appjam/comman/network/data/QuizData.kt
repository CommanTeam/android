package org.appjam.comman.network.data

/**
 * Created by yeahen on 2018-01-06.
 */
object QuizData {
    data class QuizResponse(val result : List<QuizInfo>)

    data class QuizInfo(val quizID: Int,//퀴즈 아이디 값
                         val quizTitle: String, //퀴즈 제목
                         val quizPriority : Int,//퀴즈 순서(퀴즈 제목 옆 인덱스 값)
                         val quizImage : String, //이미지 포함된 퀴즈에서 불러올 이미지 경로
                         val explanation: String, //해당 퀴즈의 정답 해설정보
                         val questionArr: List<QuestionArr>
    )

    data class QuestionArr(val questionID: Int,
                           val questionContent : String,
                           val answerFlag : Int)

    data class AnswerArr(
            var answerArr : MutableList<Int>
    )

}