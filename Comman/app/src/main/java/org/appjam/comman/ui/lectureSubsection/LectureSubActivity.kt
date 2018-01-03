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
import java.util.*

/**
 * Created by KSY on 2018-01-03.
 */
class LectureSubActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {

    }

    private val chapterContentsList = mutableListOf<LectureSubActivity.ChapterListData>()

    private var chapterListDatas : ArrayList<ChapterListData>?=null

    data class ChapterListData(
            val number: String,
            val content: String,
            val totalNumber: String
    )

    private var adapter : LectureSubAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_subsection)

        val recyclerView = lecture_subsection_list_view
        adapter = LectureSubAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

    }


    inner class LectureSubAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var onItemClick : View.OnClickListener? = null

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            if(viewType == ListUtils.TYPE_HEADER) {
                return HeaderViewHolder(layoutInflater.inflate(R.layout.lecture_subsection_course_item, parent, false))
            } else if (viewType == ListUtils.TYPE_SECOND_HEADER) {
                val view : View = layoutInflater.inflate(R.layout.lecture_subsection_video_item, parent, false)
                view.setOnClickListener(onItemClick)
                return SecondHeaderViewHolder(view)
            } else {
                val view : View = layoutInflater.inflate(R.layout.lecture_subsection_chapterlist_item, parent, false)
                view.setOnClickListener(onItemClick)
                return ElemViewHolder(view)
            }
        }

        override fun getItemCount() = chapterContentsList.size + 2

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if (holder?.itemViewType == ListUtils.TYPE_ELEM) {
                (holder as LectureSubActivity.ElemViewHolder).bind(position - 2)
            }
        }

        override fun getItemViewType(position: Int): Int
                = when (position) {
            0 -> ListUtils.TYPE_HEADER
            1 -> ListUtils.TYPE_SECOND_HEADER
            else -> ListUtils.TYPE_ELEM
        }


        fun setOnItemClickListener(l: View.OnClickListener) {
            onItemClick = l
        }
    }


    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    inner class SecondHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ElemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        // TODO: Implement more detail view binding
        fun bind(position: Int) {
            itemView.lecture_subsection_chapterlist_chapnum_tv.text = chapterContentsList[position].number
            itemView.lecture_subsection_chapterlist_totalnum_tv.text = chapterContentsList[position].content
            itemView.lecture_subsection_chapterlist_chapname_tv.text = chapterContentsList[position].content

        }

}
}