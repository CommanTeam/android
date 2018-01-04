package org.appjam.comman

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_enroll_popup.*

class EnrollPopupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enroll_popup)

        //닫기 버튼 클릭시 팝업 종료
        enroll_close_btn.setOnClickListener{
            finish()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //바깥 레이어 클릭시 안닫히게 한다.
        if (MotionEvent.ACTION_OUTSIDE == event!!.action) {
            return false
        }
        return true
    }
}
