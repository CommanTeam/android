package org.appjam.comman.ui.CourseSubsection

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.androidquery.AQuery
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_lecture_list.*
import kotlinx.android.synthetic.main.activity_lecture_subsection.*
import kotlinx.android.synthetic.main.lecture_subsection_chapterlist_item.view.*
import kotlinx.android.synthetic.main.lecture_subsection_course_item.view.*
import kotlinx.android.synthetic.main.lecture_subsection_video_item.view.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.CoursesData
import org.appjam.comman.network.data.LectureData
import org.appjam.comman.network.data.PopupData
import org.appjam.comman.ui.courseNonRegist.ChargePopupActivity
import org.appjam.comman.ui.lecture.LectureListActivity
import org.appjam.comman.util.ListUtils
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.TimeUtils
import org.appjam.comman.util.setDefaultThreads

/**
 * Created by KSY on 2018-01-03.
 */
class CourseSubActivity : AppCompatActivity() {

    companion object {
        private val TAG = "CourseSubActivity"
    }

    private var courseMetaData: CoursesData.CourseMetadata? = null
    private val disposables = CompositeDisposable()
    private var chaptersInfoList: List<PopupData.PopupContentInfo> = listOf()
    private var recentLectureInfo: LectureData.RecentLectureInfo? = null
    private var isPurchased: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_subsection)

        val recycler_view = lecture_subsection_list_view
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = LectureSubAadapter()

        disposables.add(APIClient.apiService.getCourseMetaInfo(
                PrefUtils.getUserToken(this@CourseSubActivity), intent.getIntExtra("courseID", 0))  //defaultValue 0넣는게 맞을까?
                .setDefaultThreads()
                .subscribe({ response ->
                    courseMetaData = response.result[0]
                    recycler_view.adapter.notifyDataSetChanged()
                }, { failure ->
                    Log.i(TAG, "on Failure ${failure.message}")
                }))

        disposables.add(APIClient.apiService.getPopupContentInfos(
                PrefUtils.getUserToken(this@CourseSubActivity), intent.getIntExtra("courseID", 0))
                .setDefaultThreads()
                .subscribe({ response ->
                    chaptersInfoList = response.result
                    recycler_view.adapter.notifyDataSetChanged()
                }, { failure ->
                    Log.i(TAG, "on Failure ${failure.message}")
                }))

        disposables.add(APIClient.apiService.checkPurchaseCourse(
                PrefUtils.getUserToken(this@CourseSubActivity), intent.getIntExtra("courseID", 0))
                .setDefaultThreads()
                .subscribe({ response ->
                    isPurchased = response.result
                    recycler_view.adapter.notifyDataSetChanged()
                }, { failure ->
                    Log.i(TAG, "on Failure ${failure.message}")
                }))
        if (PrefUtils.getInt(this, PrefUtils.LECTURE_ID) != 0) {
            disposables.add(APIClient.apiService.getRecentLecture(
                    PrefUtils.getUserToken(this), PrefUtils.getInt(this, PrefUtils.LECTURE_ID))
                    .setDefaultThreads()
                    .subscribe({ response ->
                        recentLectureInfo = response.result
                        lecture_list_rv.adapter.notifyDataSetChanged()
                    }, { failure ->
                        Log.i(LectureListActivity.TAG, "on Failure ${failure.message}")
                    }))
        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            if (courseMetaData != null) {
                val aQuery = AQuery(this@CourseSubActivity)
                aQuery.id(itemView.lecture_subsection_course_profile_iv).image(courseMetaData?.supplier_thumbnail)
                itemView.lecture_subsection_course_name_tv.text = courseMetaData?.title
                itemView.lecture_subsection_instructor_name_tv.text = courseMetaData?.name
                itemView.lecture_subsection_course_exp_tv.text = courseMetaData?.info
            }
            itemView.lecture_subsection_popup_layout.setOnClickListener {
                val intent = Intent(applicationContext, CourseSubPopupActivity::class.java)
                intent.putExtra("courseID", courseMetaData?.id)
                startActivity(intent)
            }
        }
    }

    inner class SecondHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO : 비디오일 경우 백그라운드 설정 및 몇 가지 설정 좀 더 필요, intent 연결도 필요
        fun bind() {
            if (isPurchased == 0) {
                itemView.lecture_subsection_purchase_btn.visibility = View.VISIBLE
                itemView.lecture_subsection_purchase_btn.setOnClickListener {
                    val intent = Intent(this@CourseSubActivity, ChargePopupActivity::class.java)
                    intent.putExtra("courseID", courseMetaData?.id)
                    startActivity(intent)
                }
            } else {
                itemView.lecture_subsection_purchase_btn.visibility = View.GONE
            }

            if (recentLectureInfo != null) {
                itemView.lecture_subsection_video_chaporder_tv.text = "${recentLectureInfo!!.course_title} > ${recentLectureInfo!!.chapter_priority}장"

                if ((recentLectureInfo!!.lecture_priority) / 10 == 0) {
                    itemView.lecture_subsection_video_lecturename_tv.text = "0${recentLectureInfo!!.lecture_priority}. ${recentLectureInfo!!.lecture_title}"
                } else {
                    itemView.lecture_subsection_video_lecturename_tv.text = "${recentLectureInfo!!.lecture_priority}. ${recentLectureInfo!!.lecture_title}"
                }

                if (recentLectureInfo!!.lecture_type == 0) {
                    itemView.lecture_subsection_video_time_tv.text =
                            "${PrefUtils.getInt(this@CourseSubActivity, PrefUtils.POSITION)} / ${recentLectureInfo!!.cnt_lecture_quiz}"
                    itemView.lecture_subsection_video_play_btn.setBackgroundResource(R.drawable.home_quiz_icon)
                    itemView.lecture_subsection_watching_progress_bar.visibility = View.GONE
                } else if (recentLectureInfo!!.lecture_type == 1) {
                    itemView.lecture_subsection_video_time_tv.text =
                            "${PrefUtils.getInt(this@CourseSubActivity, PrefUtils.POSITION)} / ${recentLectureInfo!!.cnt_lecture_picture}"
                    itemView.lecture_subsection_video_play_btn.setBackgroundResource(R.drawable.home_picture_icon)
                    itemView.lecture_subsection_watching_progress_bar.visibility = View.GONE
                } else {
                    itemView.lecture_subsection_video_time_tv.text =
                            "${TimeUtils.formatTime(PrefUtils.getInt(this@CourseSubActivity, PrefUtils.DURATION_TIME))}"
                    itemView.lecture_subsection_video_play_btn.setBackgroundResource(R.drawable.home_video_icon)
                    itemView.lecture_subsection_watching_progress_bar.visibility = View.VISIBLE
                }

            } else {
                itemView.lecture_subsection_video_title_tv.visibility = View.GONE
                itemView.lecture_subsection_video_play_layout.visibility = View.GONE
            }
        }
    }


    inner class ElemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO: 일단 기본적인 것은 완성 혹시나 안되면 데이터 클래스나 통신 쪽을 확인해봐야 함
        fun bind(position: Int) {
            itemView.lecture_subsection_chapterlist_chapnum_tv.text = "${chaptersInfoList[position].priority}장"
            //itemView.lecture_subsection_chapterlist_totalnum_tv.text = chaptersInfoList[position].lectu   api에 단원의 강좌수 추가되면 수정
            itemView.lecture_subsection_chapterlist_chapname_tv.text = chaptersInfoList[position].title
            if (((isPurchased == 0) and chaptersInfoList[position].open) or (isPurchased == 1)) {
                itemView.lecture_subsection_lock_layout.visibility = View.GONE
                itemView.setOnClickListener {
                    val intent = Intent(this@CourseSubActivity, LectureListActivity::class.java)
                    intent.putExtra("chapterID", chaptersInfoList[position].chapterID)
                    startActivity(intent)
                }
            }
        }
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class LectureSubAadapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == ListUtils.TYPE_HEADER) {
                val view: View = layoutInflater.inflate(R.layout.lecture_subsection_course_item, parent, false)
                HeaderViewHolder(view)
            } else if (viewType == ListUtils.TYPE_SECOND_HEADER) {
                val view: View = layoutInflater.inflate(R.layout.lecture_subsection_video_item, parent, false)
                SecondHeaderViewHolder(view)
            } else if (viewType == ListUtils.TYPE_ELEM) {
                val view: View = layoutInflater.inflate(R.layout.lecture_subsection_chapterlist_item, parent, false)
                ElemViewHolder(view)
            } else {
                val view: View = layoutInflater.inflate(R.layout.lecture_list_footer, parent, false)
                FooterViewHolder(view)
            }
        }

        override fun getItemCount() = chaptersInfoList.size + 3

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if (holder?.itemViewType == ListUtils.TYPE_ELEM) {
                (holder as ElemViewHolder).bind(position - 2)
            } else if (holder?.itemViewType == ListUtils.TYPE_HEADER) {
                (holder as HeaderViewHolder).bind()
            } else if (holder?.itemViewType == ListUtils.TYPE_SECOND_HEADER) {
                (holder as SecondHeaderViewHolder).bind()
            } else
                holder as FooterViewHolder
        }

        override fun getItemViewType(position: Int): Int
                = when (position) {
            0 -> ListUtils.TYPE_HEADER
            1 -> ListUtils.TYPE_SECOND_HEADER
            (itemCount - 1) -> ListUtils.TYPE_FOOTER
            else -> ListUtils.TYPE_ELEM
        }
    }
}

