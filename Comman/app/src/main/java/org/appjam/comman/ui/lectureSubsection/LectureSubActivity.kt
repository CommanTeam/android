package org.appjam.comman.ui.lectureSubsection

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_lecture_subsection.*
import kotlinx.android.synthetic.main.lecture_subsection_chapterlist_item.view.*
import org.appjam.comman.R
import org.appjam.comman.util.ListUtils

/**
 * Created by KSY on 2018-01-03.
 */
class LectureSubActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {

    }

    private var chapterList : RecyclerView?=null
    private var chapterListData : ArrayList<ChapterListData>?=arrayListOf()
    private var lectureSubAdapter : LectureSubAdapter? = null


    data class ChapterListData(
            val number: String,
            val content: String,
            val totalNumber: String
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_subsection)

        chapterList=lecture_subsection_list_view
        chapterList!!.layoutManager=LinearLayoutManager(this)

        chapterListData?.add(ChapterListData("1장","반지 모델링 하기","총 16강"))
        chapterListData?.add(ChapterListData("2장","반지 모델링 하기","총 16강"))
        chapterListData?.add(ChapterListData("3장","반지 모델링 하기","총 16강"))

        lectureSubAdapter=LectureSubAdapter(chapterListData)

        chapterList!!.adapter=lectureSubAdapter
    }
    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    inner class SecondHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ElemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO: Implement more detail view binding
        fun bind(position: Int) {
            itemView.lecture_subsection_chapterlist_chapnum_tv.text = chapterListData!![position].number
            itemView.lecture_subsection_chapterlist_totalnum_tv.text = chapterListData!![position].content
            itemView.lecture_subsection_chapterlist_chapname_tv.text = chapterListData!![position].totalNumber

        }
    }
    inner class LectureSubAdapter(var datalist: ArrayList<ChapterListData>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var onItemClick : View.OnClickListener? = null

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            return if(viewType == ListUtils.TYPE_HEADER) {
                val view : View = layoutInflater.inflate(R.layout.lecture_subsection_course_item, parent, false)
                view.setOnClickListener(onItemClick)
                HeaderViewHolder(view)
            } else if (viewType == ListUtils.TYPE_SECOND_HEADER) {
                val view : View = layoutInflater.inflate(R.layout.lecture_subsection_video_item, parent, false)
                view.setOnClickListener(onItemClick)
                SecondHeaderViewHolder(view)
            } else {
                val view : View = layoutInflater.inflate(R.layout.lecture_subsection_chapterlist_item, parent, false)
                view.setOnClickListener(onItemClick)
                ElemViewHolder(view)
            }
        }

        override fun getItemCount() = chapterListData!!.size + 2

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if (holder?.itemViewType == ListUtils.TYPE_ELEM) {
                (holder as ElemViewHolder).bind(position - 1)
            }else if(holder?.itemViewType == ListUtils.TYPE_HEADER) {
                (holder as HeaderViewHolder)
            }
            else if(holder?.itemViewType == ListUtils.TYPE_SECOND_HEADER){
                holder as SecondHeaderViewHolder
            }
        }

        override fun getItemViewType(position: Int): Int
                = when (position) {
            0 -> ListUtils.TYPE_HEADER
            1 -> ListUtils.TYPE_SECOND_HEADER
            else -> ListUtils.TYPE_ELEM
        }


    }
}
