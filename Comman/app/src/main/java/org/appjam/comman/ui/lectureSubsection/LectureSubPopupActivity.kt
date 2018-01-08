package org.appjam.comman.ui.lectureSubsection

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_lecture_popup.*
import org.appjam.comman.R

class LectureSubPopupActivity : AppCompatActivity() {

    private var lecturePopupList : RecyclerView? = null
    private var lecturePopupDatas: ArrayList<LecturePopupData>? = null
    private var lecturePopupAdapter : LecturePopupAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_popup)

        lecturePopupList = findViewById<View>(R.id.lecture_popup_list) as RecyclerView
        lecturePopupList!!.layoutManager = LinearLayoutManager(this)

        lecturePopupDatas = ArrayList<LecturePopupData>()
        lecturePopupDatas!!.add(LecturePopupData(R.drawable.kakao_default_profile_image,"1장. 라이노 툴 다루기", "가장 기본적인 라이노의 기능과 활용방법을 배워볼 수 있다. 점 선, 면, 덩어리의 개념을 이해하면서 기초적인 모델링을 해볼 수 있다."))
        lecturePopupDatas!!.add(LecturePopupData(R.drawable.kakao_default_profile_image,"2장. 필렛 넣기", "가장 기본적인 라이노의 기능과 활용방법을 배워볼 수 있다. 점 선, 면, 덩어리의 개념을 이해하면서 기초적인 모델링을 해볼 수 있다."))
        lecturePopupDatas!!.add(LecturePopupData(R.drawable.kakao_default_profile_image,"2장. 필렛 넣기", "가장 기본적인 라이노의 기능과 활용방법을 배워볼 수 있다. 점 선, 면, 덩어리의 개념을 이해하면서 기초적인 모델링을 해볼 수 있다."))
        lecturePopupAdapter = LecturePopupAdapter(lecturePopupDatas)


        lecturePopupList!!.adapter = lecturePopupAdapter

        //닫기 버튼 클릭시 팝업 종료
        lecture_popup_btn.setOnClickListener{
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


    /**ViewHolder는 각 리스트에 어떤 뷰가 들어가는지 설정해주는 부분입니다. 한 번 설정해주면 몇번이고 재사용이 가능합니다
     * */
    private inner class LecturePopupViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {
        var img : ImageView = itemView!!.findViewById(R.id.lecturePopup_img)
        var title : TextView = itemView!!.findViewById(R.id.lecturePopup_lecName_tv)
        var content : TextView = itemView!!.findViewById(R.id.lecturePopup_lecContent_tv)
    }

    data class LecturePopupData (
            var img : Int,
            var title : String,
            var content : String
    )

    /***
     * 어댑터는 데이터와 화면 출력을 이어주는 객체입니다 여기서는 QuizResultData에 넣은 데이터들을 ViewHolder로 연결하기 위해 쓰였습니다 **/
    private inner class LecturePopupAdapter(var dataList : ArrayList<LecturePopupData>?) : RecyclerView.Adapter<LecturePopupViewHolder>() {

        override fun onBindViewHolder(holder: LecturePopupViewHolder?, position: Int) {
            holder!!.img.setImageResource(dataList!!.get(position).img)
            holder!!.title.text = dataList!!.get(position).title
            holder!!.content.text = dataList!!.get(position).content
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LecturePopupViewHolder {
            val mainView : View = LayoutInflater.from(parent!!.context).inflate(R.layout.activity_lecture_popup, parent, false)

            return LecturePopupViewHolder(mainView)
        }

        override fun getItemCount(): Int = dataList!!.size

    }
}