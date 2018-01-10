package org.appjam.comman.ui.courseNonRegist

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_charge_popup.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.PopupData
import org.appjam.comman.ui.CourseSubsection.CourseSubActivity
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.setDefaultThreads


class ChargePopupActivity : AppCompatActivity() {

    companion object {
        private val TAG = "ChargePopupActivity"
    }

    private val disposables = CompositeDisposable()
    private var courseInfo : List<PopupData.PopupTitleInfo> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charge_popup)

        var choice : Int ?=null

        disposables.add(APIClient.apiService.getPopupTitleInfos(
                PrefUtils.getUserToken(this), intent.getIntExtra("courseID", 1))
                .setDefaultThreads()
                .subscribe({ response ->
                    courseInfo = response.result
                    charge_title_tv.text = courseInfo[0].title
                    charge_price_tv.text = "${courseInfo[0].price}원"        // TODO 만 단위 이상에서는 , 붙여줘야 하나?
                } ,{
                    failure -> Log.i(TAG, "on Failure ${failure.message}")
                }))

        //닫기 버튼 클릭시 팝업 종료
        charge_close_btn.setOnClickListener{
            finish()

        }
        charge_ok_btn.setOnClickListener {
            disposables.add(APIClient.apiService.purchaseCourse(
                    PrefUtils.getUserToken(this), intent.getIntExtra("courseID", 1))
                    .setDefaultThreads()
                    .subscribe({ response ->
                        if(response.message == "구매 성공") {
                            val intent = Intent(this, CourseSubActivity::class.java)
                            intent.putExtra("courseID", intent.getIntExtra("courseID", 1))
                            startActivity(intent)       // TODO 토스트 띄울 건지 어떠 액션을 취할 건지 고민
                        }
                    }, {
                        failure -> Log.i(TAG, "on Failure ${failure.message}")
                    }))
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //바깥 레이어 클릭시 안닫히게 한다.
        if (MotionEvent.ACTION_OUTSIDE == event!!.action) {
            return false
        }
        return true
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
