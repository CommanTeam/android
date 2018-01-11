package org.appjam.comman.ui.courseNonRegist

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_enroll_popup.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.CoursesData
import org.appjam.comman.ui.main.MainActivity
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.setDefaultThreads


class EnrollPopupActivity : AppCompatActivity() {

    companion object {
        private val TAG = "EnrollPopupActivity"
    }

    private val disposables = CompositeDisposable()

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

        //등록하기 버튼 클릭시 등록된 페이지(CourseSubAcitivity)로 이동
        enroll_ok_btn.setOnClickListener{
            val courseID = intent.getIntExtra("courseID", 0)

            disposables.add(APIClient.apiService.registerCourse(
                    PrefUtils.getUserToken(this@EnrollPopupActivity), CoursesData.RegisterPost(courseID))
                    .setDefaultThreads()
                    .subscribe({ response ->
                            val intent = Intent(this@EnrollPopupActivity, MainActivity::class.java)
                            intent.putExtra("courseID", courseID)
                            startActivity(intent)

                    }, { failure ->
                        Log.i(TAG, "on Failure ${failure.message}")
                    }))
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
