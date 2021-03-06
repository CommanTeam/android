package org.appjam.comman.ui.CourseSubsection

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
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
import org.appjam.comman.ui.card.CardActivity
import org.appjam.comman.ui.courseNonRegist.ChargePopupActivity
import org.appjam.comman.ui.lecture.LectureListActivity
import org.appjam.comman.ui.quiz.QuizActivity
import org.appjam.comman.util.ListUtils
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.YoutubeTimeUtils
import org.appjam.comman.util.setDefaultThreads
import org.appjam.comman.youtube.YoutubePracticeActivity

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
        sub_back_btn.setOnClickListener {
            finish()

        }

        val recycler_view = lecture_subsection_list_view
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = LectureSubAadapter()

        disposables.add(APIClient.apiService.getCourseMetaInfo(
                PrefUtils.getUserToken(this@CourseSubActivity), intent.getIntExtra("courseID", 0))  //defaultValue 0넣는게 맞을까?
                .setDefaultThreads()
                .subscribe({ response ->
                    courseMetaData = response.result
                    sub_lecture_name_tv.text = courseMetaData?.title
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

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
//            val aQuery = AQuery(this@CourseSubActivity)
//            aQuery.id(itemView.lecture_subsection_course_profile_iv).image(courseMetaData?.supplier_thumbnail)

            Glide.with(this@CourseSubActivity)
                    .load(courseMetaData?.supplier_thumbnail)
                    .centerCrop()
                    .into(itemView.lecture_subsection_course_profile_iv)
            itemView.lecture_subsection_course_name_tv.text = courseMetaData?.title
            itemView.lecture_subsection_instructor_name_tv.text = courseMetaData?.name
            itemView.lecture_subsection_course_exp_tv.text = courseMetaData?.info
            itemView.lecture_subsection_popup_layout.setOnClickListener {
                val intent = Intent(applicationContext, CourseSubPopupActivity::class.java)
                intent.putExtra("courseID", courseMetaData?.id)
                startActivity(intent)
            }
        }
    }

    inner class SecondHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            if (recentLectureInfo != null) {
                itemView.course_lecture_wathing_layout.visibility = View.VISIBLE
                itemView.course_lecture_wathing_tv.visibility = View.VISIBLE

                itemView.course_course_wathing_chapter_tv.text = "${recentLectureInfo!!.course_title} > ${recentLectureInfo!!.chapter_priority}장"

                if ((recentLectureInfo!!.lecture_priority) / 10 == 0) {
                    itemView.course_course_wathing_title_tv.text = "0${recentLectureInfo!!.lecture_priority}. ${recentLectureInfo!!.lecture_title}"
                } else {
                    itemView.course_course_wathing_title_tv.text = "${recentLectureInfo!!.lecture_priority}. ${recentLectureInfo!!.lecture_title}"
                }

                if (recentLectureInfo!!.lecture_type == 0) {
                    itemView.course_subsection_video_thumb.visibility = View.GONE
                    itemView.course_lecture_watching_progress_tv.text =
                            "${PrefUtils.getRecentLectureOfCoursePosition(this@CourseSubActivity, recentLectureInfo!!.course_ID)+1} / ${recentLectureInfo!!.cnt_lecture_quiz+1}"
                    itemView.course_lecture_wathing_img.setBackgroundResource(R.drawable.home_quiz_icon)
                    Glide.with(this@CourseSubActivity)
                            .load(R.drawable.home_quiz_default_image)
                            .centerCrop()
                            .into(itemView.course_watching_full_background_img)
                    itemView.course_lecture_watching_progress_bar.visibility = View.GONE
                    itemView.course_lecture_wathing_layout.setOnClickListener {
                        val intent = Intent(this@CourseSubActivity, QuizActivity::class.java)
                        intent.putExtra("courseID", recentLectureInfo!!.course_ID)
                        intent.putExtra("lectureID", PrefUtils.getRecentLectureOfCourseID(this@CourseSubActivity, recentLectureInfo!!.course_ID))
                        startActivity(intent)
                    }
                } else if (recentLectureInfo!!.lecture_type == 1) {
                    itemView.course_subsection_video_thumb.visibility = View.GONE
                    itemView.course_lecture_watching_progress_tv.text =
                            "${PrefUtils.getRecentLectureOfCoursePosition(this@CourseSubActivity, recentLectureInfo!!.course_ID)+1} / ${recentLectureInfo!!.cnt_lecture_picture+1}"
                    itemView.course_lecture_wathing_img.setBackgroundResource(R.drawable.home_picture_icon)
                    Glide.with(this@CourseSubActivity)
                            .load(R.drawable.home_picture_default_image)
                            .centerCrop()
                            .into(itemView.course_watching_full_background_img)
                    itemView.course_lecture_watching_progress_bar.visibility = View.GONE
                    itemView.course_lecture_wathing_layout.setOnClickListener {
                        val intent = Intent(this@CourseSubActivity, CardActivity::class.java)
                        intent.putExtra("courseID", recentLectureInfo!!.course_ID)
                        intent.putExtra("lectureID", PrefUtils.getRecentLectureOfCourseID(this@CourseSubActivity, recentLectureInfo!!.course_ID))
                        startActivity(intent)
                    }
                } else {
                    itemView.course_subsection_video_thumb.visibility = View.VISIBLE
                    itemView.course_lecture_watching_progress_tv.text =
                            "${YoutubeTimeUtils.formatTime(recentLectureInfo!!.playTime)}"
                    itemView.course_lecture_wathing_img.setBackgroundResource(R.drawable.home_video_icon)

                    val thumbURL = "https://img.youtube.com/vi/${recentLectureInfo!!.lecture_video_id}/sddefault.jpg"

                    Glide.with(this@CourseSubActivity)
                            .load(thumbURL)
                            .placeholder(R.mipmap.ic_launcher)
                            .fitCenter()
                            .centerCrop()
                            .error(R.mipmap.ic_launcher)
                            .into(itemView.course_watching_full_background_img)
                    itemView.course_lecture_watching_progress_bar.visibility = View.VISIBLE
                    itemView.course_lecture_watching_progress_bar.progress =
                            (PrefUtils.getYoutubeCurrentTimeInCourse(this@CourseSubActivity, recentLectureInfo!!.course_ID) * 100 / recentLectureInfo!!.playTime)
                    itemView.course_lecture_wathing_layout.setOnClickListener {
                        val intent = Intent(this@CourseSubActivity, YoutubePracticeActivity::class.java)
                        intent.putExtra("courseID", recentLectureInfo!!.course_ID)
                        intent.putExtra("lectureID", PrefUtils.getRecentLectureOfCourseID(this@CourseSubActivity, recentLectureInfo!!.course_ID))
                        intent.putExtra("chapterID", recentLectureInfo!!.chapter_ID)
                        startActivity(intent)
                    }
                }

            } else {        //시청하던 강의 없을 경우 숨기기
                itemView.course_lecture_wathing_layout.visibility = View.GONE
                itemView.course_lecture_wathing_tv.visibility = View.GONE
            }

            if(isPurchased == 0) {
                itemView.course_subsection_purchase_btn.visibility = View.VISIBLE
                itemView.course_subsection_purchase_btn.setOnClickListener {
                    val intent = Intent(this@CourseSubActivity, ChargePopupActivity::class.java)
                    intent.putExtra("courseID", recentLectureInfo?.course_ID)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    startActivity(intent)
                    finish()
                }
            } else {
                itemView.course_subsection_purchase_btn.visibility = View.GONE
            }

        }

    }


    inner class ElemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO: 일단 기본적인 것은 완성 혹시나 안되면 데이터 클래스나 통신 쪽을 확인해봐야 함
        fun bind(position: Int) {
            itemView.lecture_subsection_chapterlist_chapnum_tv.text = "${chaptersInfoList[position].priority}장"
            itemView.lecture_subsection_chapterlist_totalnum_tv.text = "총 ${chaptersInfoList[position].lectureCnt}강"
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
            return when (viewType) {
                ListUtils.TYPE_HEADER -> {
                    val view: View = layoutInflater.inflate(R.layout.lecture_subsection_course_item, parent, false)
                    HeaderViewHolder(view)
                }
                ListUtils.TYPE_SECOND_HEADER -> {
                    val view: View = layoutInflater.inflate(R.layout.lecture_subsection_video_item, parent, false)
                    SecondHeaderViewHolder(view)
                }

                ListUtils.TYPE_ELEM -> {
                    val view: View = layoutInflater.inflate(R.layout.lecture_subsection_chapterlist_item, parent, false)
                    ElemViewHolder(view)
                }
                else -> {
                    val view: View = layoutInflater.inflate(R.layout.lecture_list_footer, parent, false)
                    FooterViewHolder(view)
                }
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