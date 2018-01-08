package org.appjam.comman.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidquery.AQuery
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.course_active_item.view.*
import kotlinx.android.synthetic.main.course_watching_item.view.*
import kotlinx.android.synthetic.main.fragment_main_my_lecture.view.*
import kotlinx.android.synthetic.main.main_notice_item.view.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.CoursesData
import org.appjam.comman.network.data.GreetingData
import org.appjam.comman.util.ListUtils
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.setDefaultThreads

/**
 * Created by RyuDongIl on 2018-01-02.
 */
class MyCourseFragment : Fragment(), View.OnClickListener {
    override fun onClick(p0: View?) {

    }

    companion object {
        const val TAG = "MyCourseFragment"
    }

    private var lectureWatchingData : LectureWatchingItem? = null
    private var greetingInfo : GreetingData.GreetingResult? = null
    private var courseInfoList: List<CoursesData.CourseInfo> = listOf()

    data class LectureWatchingItem (
            val chapterName: String,
            val lectureTitle: String
    )

    private val disposables = CompositeDisposable()

    init {
        // TODO: Implement network data class
        lectureWatchingData = MyCourseFragment.LectureWatchingItem("무슨 강좌 01 챕터", "[Rhino] 반지 모델링")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_main_my_lecture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.main_my_lecture_rv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = MyLectureAdapter()

        disposables.add(APIClient.apiService.getRegisteredCourses(PrefUtils.getUserToken(context))
                .setDefaultThreads()
                .subscribe ({
                    response -> courseInfoList = response.result
                                recyclerView.adapter.notifyDataSetChanged()

                }, {
                    failure -> Log.i(TAG, "on Failure ${failure.message}")
                })
        )
        disposables.add(APIClient.apiService.getGreetingInfo(PrefUtils.getUserToken(context))
                .setDefaultThreads()
                .subscribe({
                    response -> greetingInfo = response.result
                                recyclerView.adapter.notifyDataSetChanged()
                }, {
                    failure -> Log.i(TAG, "on Failure ${failure.message}")
                })
        )
    }

    inner class MyLectureAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                ListUtils.TYPE_HEADER -> HeaderViewHolder(layoutInflater.inflate(R.layout.main_notice_item, parent, false))
                ListUtils.TYPE_SECOND_HEADER -> SecondHeaderViewHolder(layoutInflater.inflate(R.layout.course_watching_item, parent, false))
                ListUtils.TYPE_FOOTER -> FooterViewHolder(layoutInflater.inflate(R.layout.course_item_footer, parent, false))
                else -> ElemViewHolder(layoutInflater.inflate(R.layout.course_active_item, parent, false))
            }
        }

        override fun getItemCount() = courseInfoList.size + 3

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if (holder?.itemViewType == ListUtils.TYPE_ELEM) {
                (holder as MyCourseFragment.ElemViewHolder).bind(courseInfoList[position - 2])
            } else if (holder?.itemViewType == ListUtils.TYPE_SECOND_HEADER) {
                (holder as MyCourseFragment.SecondHeaderViewHolder).bind()
            } else if (holder?.itemViewType == ListUtils.TYPE_HEADER) {
                (holder as MyCourseFragment.HeaderViewHolder).bind()
            }
        }

        override fun getItemViewType(position: Int): Int
                = when (position) {
                    0 -> ListUtils.TYPE_HEADER
                    1 -> ListUtils.TYPE_SECOND_HEADER
                    itemCount - 1 -> ListUtils.TYPE_FOOTER
                    else -> ListUtils.TYPE_ELEM
                }
    }

    inner class ElemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(courseInfo: CoursesData.CourseInfo) {
            itemView.main_course_active_img.setImageResource(R.drawable.additional_explanation_btn)
            itemView.main_course_active_course_tv.text = courseInfo.courseTitle
            itemView.main_course_active_chapters_tv.text = resources.getString(R.string.msg_format_chapter_count, courseInfo.chapterCnt)

            val progressPercentage = courseInfo.progressPercentage
            itemView.main_course_active_progress_bar.progress = progressPercentage
            itemView.main_course_active_progress_tv.text = resources.getString(R.string.msg_format_progress_percentage, progressPercentage)


        }
    }

    inner class SecondHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO: Implement more detail view binding
        fun bind() {
            itemView.main_course_wathing_chapter_tv.text = lectureWatchingData!!.chapterName
            itemView.main_course_wathing_title_tv.text = lectureWatchingData!!.lectureTitle

        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            var aQuery = AQuery(context)
            val thumbnailUrl = greetingInfo?.userImg
            aQuery.id(itemView.main_profile_img).image(thumbnailUrl)
            itemView.main_notice_tv.text = greetingInfo?.ment
        }
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }

}