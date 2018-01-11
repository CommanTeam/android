package org.appjam.comman.util

import android.content.Context
import android.content.Intent
import org.appjam.comman.network.data.NextLectureData
import org.appjam.comman.ui.CourseSubsection.CourseSubActivity
import org.appjam.comman.ui.card.CardActivity
import org.appjam.comman.ui.courseNonRegist.ChargePopupActivity
import org.appjam.comman.ui.quiz.QuizActivity
import org.appjam.comman.youtube.YoutubePracticeActivity

/**
 * Created by junhoe on 2017. 12. 31..
 */
object ListUtils {

    const val TYPE_TOP = 4
    const val TYPE_ELEM = 0
    const val TYPE_HEADER = 1
    const val TYPE_FOOTER = 2
    const val TYPE_SECOND_HEADER = 3

    fun linkToNextLecture(context: Context, nextLectureResponse: NextLectureData.NextLectureResponse?,
                          courseID: Int) : Intent {
        if((nextLectureResponse?.nextLectureOfCourse?.lectureID == -1) and (nextLectureResponse?.nextLectureOfCourse?.purchaseFlag == 0)) {
            val intent = Intent(context, ChargePopupActivity::class.java)
            intent.putExtra("courseID", courseID)
            return intent
        }
        if(nextLectureResponse?.nextLectureOfCourse?.lectureID == -1) {
            val intent = Intent(context, CourseSubActivity::class.java)
            intent.putExtra("courseID", courseID)
            return intent
        }
        if(nextLectureResponse?.nextLectureOfCourse?.lectureType == 0) {
            val intent = Intent(context, QuizActivity::class.java)
            intent.putExtra("lectureID", nextLectureResponse.nextLectureOfCourse.lectureID)
            intent.putExtra("courseID", courseID)
            return intent
        } else if(nextLectureResponse?.nextLectureOfCourse?.lectureType == 1) {
            val intent = Intent(context, CardActivity::class.java)
            intent.putExtra("lectureID", nextLectureResponse.nextLectureOfCourse.lectureID)
            intent.putExtra("courseID", courseID)
            return intent
        } else {
            val intent = Intent(context, YoutubePracticeActivity::class.java)
            intent.putExtra("lectureID", nextLectureResponse?.nextLectureOfCourse?.lectureID)
            intent.putExtra("courseID", courseID)       //TODO nextLectureResponse에 챕터id추가되면 수정 필요
            return intent
        }
    }


}
