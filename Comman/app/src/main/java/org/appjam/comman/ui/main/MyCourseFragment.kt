package org.appjam.comman.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main_my_lecture.view.*
import kotlinx.android.synthetic.main.lecture_active_item.view.*
import kotlinx.android.synthetic.main.lecture_watching_item.view.*
import org.appjam.comman.R
import org.appjam.comman.util.ListUtils

/**
 * Created by RyuDongIl on 2018-01-02.
 */
class MyCourseFragment : Fragment() {
    private val lectureItemList = arrayListOf<LectureActiveItem>()
    private var lectureWatchingData : LectureWatchingItem? = null
    data class LectureWatchingItem (
            val chapterName: String,
            val lectureTitle: String
    )
    data class LectureActiveItem(
            val lectureImg: Int,
            val lectureName: String,
            val courseCount: Int,
            val lectureProgress: Int
    )

    init {
        // TODO: Implement network data class
        lectureWatchingData = MyCourseFragment.LectureWatchingItem("무슨 강좌 01 챕터", "")
        lectureItemList.add(MyCourseFragment.LectureActiveItem
        (R.drawable.quiz_icon, "[Rhino] 지우형", 15, 43))
        lectureItemList.add(MyCourseFragment.LectureActiveItem
        (R.drawable.home_video_icon, "[Rhino] 김준회", 17, 43))
        lectureItemList.add(MyCourseFragment.LectureActiveItem
        (R.drawable.quiz_icon, "[Rhino] 서연이", 20, 43))
        lectureItemList.add(MyCourseFragment.LectureActiveItem
        (R.drawable.picture_icon, "[Rhino] 규진이형", 8, 43))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_main_my_lecture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.main_my_lecture_rv
        recyclerView.adapter = MyLectureAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    inner class MyLectureAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == ListUtils.TYPE_HEADER) {
                HeaderViewHolder(layoutInflater.inflate(R.layout.main_notice_item, parent, false))
            } else if (viewType == ListUtils.TYPE_SECOND_HEADER) {
                SecondHeaderViewHolder(layoutInflater.inflate(R.layout.lecture_watching_item, parent, false))
            } else {
                ElemViewHolder(layoutInflater.inflate(R.layout.lecture_active_item, parent, false))
            }
        }

        override fun getItemCount() = lectureItemList.size + 2

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if (holder?.itemViewType == ListUtils.TYPE_ELEM) {
                (holder as MyCourseFragment.ElemViewHolder).bind(position - 2)
            } else if (holder?.itemViewType == ListUtils.TYPE_SECOND_HEADER) {
                (holder as MyCourseFragment.SecondHeaderViewHolder).bind()
            }
        }

        override fun getItemViewType(position: Int): Int
                = when (position) {
                    0 -> ListUtils.TYPE_HEADER
                    1 -> ListUtils.TYPE_SECOND_HEADER
                    else -> ListUtils.TYPE_ELEM
                }
    }

    inner class ElemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO: Implement more detail view binding
        fun bind(position: Int) {
            itemView.main_lecture_active_img.setBackgroundResource(lectureItemList[position].lectureImg)
            itemView.main_lecture_active_course_tv.text = lectureItemList[position].lectureName
            val courseCount : Int = lectureItemList[position].courseCount
            itemView.main_lecture_active_chapters_tv.text = "총 $courseCount 단원"
            val lectureProgress : Int = lectureItemList[position].lectureProgress
            itemView.main_lecture_active_progress_bar.progress = lectureProgress
            itemView.main_lecture_active_progress_tv.text = "$lectureProgress %"
        }
    }

    inner class SecondHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO: Implement more detail view binding
        fun bind() {
            itemView.main_lecture_wathing_chapter_tv.text = lectureWatchingData!!.chapterName
            itemView.main_lecture_wathing_title_tv.text = lectureWatchingData!!.lectureTitle

        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}