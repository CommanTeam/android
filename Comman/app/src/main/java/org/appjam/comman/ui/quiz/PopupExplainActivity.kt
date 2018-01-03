package org.appjam.comman.ui.quiz

/**
 * Created by yeahen on 2018-01-01.
 */


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.popup_quiz_explain.*
import org.appjam.comman.R

class PopupExplainActivity : AppCompatActivity(){

    var quiz_num : String? = null
    var quiz_text : String? = null
    var quiz_answer_num : String? = null
    var quiz_answer_text : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_quiz_explain)

        val intent = getIntent()
        var quiz_num = intent.getStringExtra("quiz_num")
        var quiz_text = intent.getStringExtra("quiz_text")
        var quiz_answer_num = intent.getStringExtra("quiz_answer_num")
        var quiz_answer_text = intent.getStringExtra("quiz_answer_text")
        popup_num_tv.text = quiz_num
        popup_quiz_tv.text = quiz_text
        popup_answerNum_tv.text = quiz_answer_num
        popup_answer_tv.text =  quiz_answer_text
        //닫기 버튼 클릭시 팝업 종료
        popup_close_btn.setOnClickListener{
            finish()
        }

    }
}
