package org.appjam.comman.ui.CourseSubsection

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_lecture_popup.*
import kotlinx.android.synthetic.main.lecture_popup_items.view.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.PopupData
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.SpaceItemDecoration
import org.appjam.comman.util.setDefaultThreads

class CourseSubPopupActivity : AppCompatActivity() {

    private var lecturePopupDatas: ArrayList<LecturePopupElemData>? = arrayListOf()
    private val disposables = CompositeDisposable()
    private var popupcontentinfoList : List<PopupData.PopupContentInfo> = listOf()

    data class LecturePopupElemData(
            var elemTitle : String,
            var elemContent : String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_popup)

        lecture_popup_list.layoutManager = LinearLayoutManager(this)
        lecture_popup_list.adapter = LecturePopupAdapter()
        lecture_popup_list.addItemDecoration(SpaceItemDecoration(this, 19, 19, 0, 0))

        lecturePopupDatas!!.add(LecturePopupElemData("1장. 라이노 툴 다루기","가장 기본적인 라이노의 기능과 활룔바법하하하하하하하하하 ㅏ하하하하하하하하하 하하하하"))
        lecturePopupDatas!!.add(LecturePopupElemData("2장. 필렛 넣기","cocedakdskfbsdkjanknvksdnasdk nkjdsfn ksadnfsadkfnsdkfnsakjfndskjaf ndskfn sdkfnsadkfnadksfnsadkfnsdfk"))
        lecturePopupDatas!!.add(LecturePopupElemData("3장. 아주안차라러아","ㅇㅁㅇㅇㄴㄻㄴㅇ럼ㄴ오륨ㅇㄴ륜ㅇ란우란우란ㅇ루나운앛ㄴ앛날ㄴ알ㄴ마루라ㅜㅏㄹㄴ란ㄹ"))

        disposables.add(APIClient.apiService.getPopupTitleInfos(PrefUtils.getUserToken(this), intent.getIntExtra("courseID",0)).setDefaultThreads()
                .subscribe({response ->
                    val popupTitleInfo = response.result[0]
                    lecturePopup_title_tv.text = popupTitleInfo.title
                    lecturePopup_name_tv.text = popupTitleInfo.name
                },{failure ->
                    Log.i("CourseSubPopupActivity","on Failure, Message : ${failure.message}")
                }))
        disposables.add(APIClient.apiService.getPopupContentInfos(PrefUtils.getUserToken(this),intent.getIntExtra("courseID",0)).setDefaultThreads()
                .subscribe({
                    response ->
                    popupcontentinfoList = response.result
                    lecture_popup_list.adapter.notifyDataSetChanged()
                },{failure ->
                    Log.i("CourseSubPopupActivity","on Failure, Message : ${failure.message}")
                }))
        //닫기 버튼 클릭시 팝업 종료
        lecture_popup_btn.setOnClickListener {
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

    private inner class LecturePopupElemViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(data : PopupData.PopupContentInfo){
            itemView.lecturePopup_lecTitle_tv.text = data.title
            itemView.lecturePopup_lecContent_tv.text = data.info
        }
    }
    /***
     * 어댑터는 데이터와 화면 출력을 이어주는 객체입니다 여기서는 QuizResultData에 넣은 데이터들을 ViewHolder로 연결하기 위해 쓰였습니다 **/
    private inner class LecturePopupAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            (holder as LecturePopupElemViewHolder).bind(popupcontentinfoList [position] )
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int):RecyclerView.ViewHolder {
            return LecturePopupElemViewHolder(layoutInflater.inflate(R.layout.lecture_popup_items,parent,false))
        }

        override fun getItemCount() = popupcontentinfoList.size
    }
    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}