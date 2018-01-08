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
import kotlinx.android.synthetic.main.lecture_popup_items.view.*
import org.appjam.comman.R

class LectureSubPopupActivity : AppCompatActivity() {

    private var lecturePopupList : RecyclerView? = null
    private var lecturePopupDatas: ArrayList<LecturePopupElemData>? = arrayListOf()
    private var lecturePopupAdapter : LecturePopupAdapter? = null
    private var lecturePopupHeaderData :LecturePopupElemData? = null

    data class LecturePopupHeaderData (
            var headerImge : Int,
            var headerTitle : String,
            var headerName : String
    )
    data class LecturePopupElemData(
            var elemTitle : String,
            var elemContent : String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_popup)

        lecturePopupList = findViewById<View>(R.id.lecture_popup_list) as RecyclerView
        lecturePopupList!!.layoutManager = LinearLayoutManager(this)

//        lecturePopupDatas = ArrayList<LecturePopupElemData>()
        LecturePopupHeaderData(R.drawable.kakao_default_profile_image,"[Rhino] 반지 모델링하기", "강사명")
        lecturePopupDatas!!.add(LecturePopupElemData("1장. 라이노 툴 다루기","가장 기본적인 라이노의 기능과 활룔바법하하하하하하하하하 ㅏ하하하하하하하하하 하하하하"))
        lecturePopupDatas!!.add(LecturePopupElemData("2장. 필렛 넣기","cocedakdskfbsdkjanknvksdnasdk nkjdsfn ksadnfsadkfnsdkfnsakjfndskjaf ndskfn sdkfnsadkfnadksfnsadkfnsdfk"))
        lecturePopupDatas!!.add(LecturePopupElemData("3장. 아주안차라러아","ㅇㅁㅇㅇㄴㄻㄴㅇ럼ㄴ오륨ㅇㄴ륜ㅇ란우란우란ㅇ루나운앛ㄴ앛날ㄴ알ㄴ마루라ㅜㅏㄹㄴ란ㄹ"))
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
        fun bind(position: Int){
            itemView.lecturePopup_lecTitle_tv.text = lecturePopupDatas[position].elemTitle
        }
    }

    /***
     * 어댑터는 데이터와 화면 출력을 이어주는 객체입니다 여기서는 QuizResultData에 넣은 데이터들을 ViewHolder로 연결하기 위해 쓰였습니다 **/
    private inner class LecturePopupAdapter(var dataList : ArrayList<LecturePopupData>?) : RecyclerView.Adapter<LecturePopupViewHolder>() {

        override fun onBindViewHolder(holder: LecturePopupViewHolder?, position: Int) {
            holder!!.img.setImageResource(dataList!!.get(position).img)
            holder!!.title.text = dataList!!.get(position).title
            holder!!.content.text = dataList!!.get(position).content
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LecturePopupViewHolder {
            val mainView : View = LayoutInflater.from(parent!!.context).inflate(R.layout.lecture_popup_items, parent, false)

            return LecturePopupViewHolder(mainView)
        }

        override fun getItemCount(): Int = dataList!!.size

    }
}
