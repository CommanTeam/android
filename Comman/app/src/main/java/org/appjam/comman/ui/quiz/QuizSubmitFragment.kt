package org.appjam.comman.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import org.appjam.comman.R
import org.appjam.comman.network.data.QuizData
import org.appjam.comman.util.PrefUtils

/**
 * Created by RyuDongIl on 2017-12-31.
 */
class QuizSubmitFragment : Fragment() {

    companion object {
        const val TAG = "QuizSubmitFragment"
    }

    private var pagePosition: Int = 0
    private var pageCount : Int = 0
    private var quizInfoList: String = ""
    private var answerArray: QuizData.AnswerArr = QuizData.AnswerArr(mutableListOf())
    private var lecturePriority: Int = 0
    private var lectureTitle: String = ""
    private var passValue: Int = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.fragment_quiz_submit, container, false)
        if(arguments != null) {
            val gson = Gson()
            quizInfoList = arguments.getString("quizInfoList")
            pagePosition = arguments.getInt("position")
            pageCount = arguments.getInt("pageCount")
            answerArray = PrefUtils.getAnswerArr(context, pageCount)
            lectureTitle = arguments.getString("lectureTitle")
            lecturePriority = arguments.getInt("lecturePriority")
            passValue = arguments.getInt("passValue")
        }
        return v
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        view?.findViewById<TextView>(R.id.quiz_count_btn)?.text = "${pagePosition + 1} / $pageCount"
        view?.findViewById<TextView>(R.id.quiz_submit_lecture_tv)?.text = "${lecturePriority}장. ${lectureTitle}"
        view?.findViewById<TextView>(R.id.quiz_submit_limit_tv)?.text = "통과하려면 ${passValue}문제 이상 맞추셔야 합니다."
        view?.findViewById<Button>(R.id.quiz_submit_btn)?.setOnClickListener {
            val intent = Intent(context, QuizResultActivity::class.java)
            intent.putExtra("quizInfoList", quizInfoList)
            startActivity(intent)
        }
    }
}