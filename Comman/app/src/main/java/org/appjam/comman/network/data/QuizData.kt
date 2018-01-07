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
                         val questionID : Int, //해당 보기의 id 값
                         val questionContent : String, //보기 텍스트
                         val answerFlag : Int,
                         val array : Array<Any> = arrayOf(questionID, questionContent, answerFlag)) //오답 = 0, 1 = 정답


}