package org.appjam.comman.ui.CourseSubsection

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_charge_popup.*
import kotlinx.android.synthetic.main.activity_lecture_popup.*
import kotlinx.android.synthetic.main.lecture_popup_items.view.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.PopupData
import org.appjam.comman.util.PopupItemDecoration
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.setDefaultThreads

class CourseSubPopupActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    companion object {
        const val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f
        const val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
    }

    private val disposables = CompositeDisposable()
    private var popupContentInfoList: List<PopupData.PopupContentInfo> = listOf()
    private var isTitleVisible = false
    private var isContainerVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_popup)
        startAlphaAnimation(toolbarTextView, 0, View.INVISIBLE)

        lecture_popup_list.layoutManager = LinearLayoutManager(this)
        lecture_popup_list.adapter = LecturePopupAdapter()
        lecture_popup_list.addItemDecoration(PopupItemDecoration(this))


        appBarLayout.addOnOffsetChangedListener(this)

        disposables.add(APIClient.apiService.getPopupTitleInfos(PrefUtils.getUserToken(this), intent.getIntExtra("courseID",0)).setDefaultThreads()
                .subscribe({response ->
                    val popupTitleInfo = response.result
                    lecturePopup_title_tv.text = popupTitleInfo.title
                    lecturePopup_name_tv.text = popupTitleInfo.name
                },{failure ->
                    Log.i("CourseSubPopupActivity","on Failure, Message : ${failure.message}")
                }))
        disposables.add(APIClient.apiService.getPopupContentInfos(PrefUtils.getUserToken(this),intent.getIntExtra("courseID",0)).setDefaultThreads()
                .subscribe({
                    response ->
                    popupContentInfoList = response.result
                    lecture_popup_list.adapter.notifyDataSetChanged()
                },{failure ->
                    Log.i("CourseSubPopupActivity","on Failure, Message : ${failure.message}")
                }))
        charge_ok_btn.setOnClickListener {
            finish()
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        val maxScroll = appBarLayout.totalScrollRange
        val percentage = Math.abs(verticalOffset) / maxScroll.toFloat()
        handleToolbarTitleVisibility(percentage)
        handleAlphaOnContainer(percentage)
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!isTitleVisible) {
                startAlphaAnimation(toolbarTextView, 200, View.VISIBLE)
                isTitleVisible = true
            }
        } else {
            if (isTitleVisible) {
                startAlphaAnimation(toolbarTextView, 200, View.INVISIBLE)
                isTitleVisible = false
            }
        }
    }

    private fun handleAlphaOnContainer(percentage: Float) {
                if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (isContainerVisible) {
                startAlphaAnimation(upperLayout, 200, View.INVISIBLE)
                isContainerVisible = false
            }
        } else {
            if (!isContainerVisible) {
                startAlphaAnimation(upperLayout, 200, View.VISIBLE)
                isContainerVisible = true
            }
        }
    }

    private fun startAlphaAnimation(v: View, duration: Long, visibility: Int) {
        val alphaAnimation = if (visibility == View.VISIBLE) AlphaAnimation(0f, 1f) else AlphaAnimation(1f, 0f)
        alphaAnimation.duration = duration
        alphaAnimation.fillAfter = true
        v.startAnimation(alphaAnimation)
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

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int):RecyclerView.ViewHolder {
            return LecturePopupElemViewHolder(layoutInflater.inflate(R.layout.lecture_popup_items,parent,false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            (holder as LecturePopupElemViewHolder).bind(popupContentInfoList[position] )
        }

        override fun getItemCount() = popupContentInfoList.size
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}