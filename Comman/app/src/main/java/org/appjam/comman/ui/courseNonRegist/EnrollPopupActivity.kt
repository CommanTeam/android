package org.appjam.comman.ui.courseNonRegist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_enroll_popup.*
import org.appjam.comman.R

class EnrollPopupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enroll_popup)
        var choice : Int ?=null
        //닫기 버튼 클릭시 팝업 종료
        enroll_close_btn.setOnClickListener{
            finish()
        }
        //등록하기 버튼 클릭시 팝업 종료
        enroll_ok_btn.setOnClickListener{
            finish()
        }
    }


}
