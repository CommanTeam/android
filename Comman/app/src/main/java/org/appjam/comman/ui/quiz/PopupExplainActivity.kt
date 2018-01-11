package org.appjam.comman.ui.quiz

/**
 * Created by yeahen on 2018-01-01.
 */


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.popup_quiz_explain.*
import org.appjam.comman.R
import org.appjam.comman.network.data.QuizData


class PopupExplainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_quiz_explain)

        val gson = Gson()
        val quizInfoList = gson.fromJson(intent.getStringExtra("quizInfo"), QuizData.QuizInfo::class.java)
        val quizCount = intent.getIntExtra("quizCount", 0)

        if(quizInfoList.quizPriority < 10) {
            popup_num_tv.text = "Q. 0${quizInfoList.quizPriority}"
        } else {
            popup_num_tv.text = "Q. ${quizInfoList.quizPriority}"
        }
        popup_quiz_tv.text = quizInfoList.quizTitle
        if(quizInfoList.quizImage == "") {
            popup_img.visibility = View.GONE
        } else {
            popup_img.visibility = View.VISIBLE
//            val aQuery = AQuery(this)
//            aQuery.id(popup_img).image(quizInfoList.quizImage)
            Glide.with(this)
                 .load(quizInfoList.quizImage)
                 .centerCrop()
                 .into(popup_img)
        }

        var i = 0
        for(question in quizInfoList.questionArr) {
            i++
            if(question.answerFlag == 1)
                popup_answerNum_tv.text = "정답: ${i}번"
        }
        popup_answer_tv.text = "해설: ${quizInfoList.explanation}"
        popup_count_btn.text = "${quizInfoList.quizPriority} / ${quizCount}"

        //닫기 버튼 클릭시 팝업 종료
        popup_close_btn.setOnClickListener{
            finish()
        }

    }
}
