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
import kotlinx.android.synthetic.main.activity_lecture_subsection.*
import kotlinx.android.synthetic.main.lecture_subsection_chapterlist_item.view.*
import kotlinx.android.synthetic.main.lecture_subsection_course_item.view.*
import kotlinx.android.synthetic.main.main_notice_item.view.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.CoursesData
import org.appjam.comman.ui.lecture.LectureListActivity
import org.appjam.comman.ui.main.MyCourseFragment
import org.appjam.comman.util.ListUtils
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.setDefaultThreads

/**
 * Created by KSY on 2018-01-03.
 */
class CourseSubActivity : AppCompatActivity() {

    companion object {
        private val TAG = "CourseSubActivity"
    }

    private var courseMetaData : List<CoursesData.CourseMetadata> = listOf()
    private var lectureSubAdapter : LectureSubAadapter? = null
    private val disposables = CompositeDisposable()

    data class ChapterListData(
            val id: Int,
            val number: String,
            val content: String,
            val totalNumber: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_subsection)

        val recycler_view = lecture_subsection_list_view
        recycler_view.layoutManager=LinearLayoutManager(this)

        lectureSubAdapter = LectureSubAadapter()
        recycler_view.adapter = lectureSubAdapter

        disposables.add(APIClient.apiService.getCourseMetaInfo(
                PrefUtils.getUserToken(this@CourseSubActivity), intent.getIntExtra("courseID"))
                .setDefaultThreads()
                .subscribe({
                    response -> courseMetaData = response.result
                                recycler_view.adapter.notifyDataSetChanged()
                }, {
                    failure -> Log.i(TAG, "on Failure ${failure.message}")
                }))


    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            if(courseMetaData.isNotEmpty()) {
                val aQuery = AQuery(this@CourseSubActivity)
                aQuery.id(itemView.lecture_subsection_course_profile_iv).image(courseMetaData[0].supplier_thumbnail)
                itemView.lecture_subsection_course_name_tv.text = courseMetaData[0].title
                itemView.lecture_subsection_instructor_name_tv.text = courseMetaData[0].name
                itemView.lecture_subsection_course_exp_tv.text = courseMetaData[0].info
            }
            itemView.lecture_subsection_popup_layout.setOnClickListener {
                val intent = Intent(applicationContext, CourseSubPopupActivity::class.java)
                startActivity(intent)
            }
        }
    }

    inner class SecondHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    inner class ElemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO: Implement more detail view binding
        fun bind(position: Int) {
            itemView.lecture_subsection_chapterlist_chapnum_tv.text = chapterListData!![position].number
            itemView.lecture_subsection_chapterlist_totalnum_tv.text = chapterListData!![position].totalNumber
            itemView.lecture_subsection_chapterlist_chapname_tv.text = chapterListData!![position].content
            itemView.setOnClickListener {
                val intent = Intent(this@CourseSubActivity, LectureListActivity::class.java)
                intent.putExtra("chapterID", chapterListData!![position].id)
                //intent.putExtra("chapterID", chapterListData!![position].id)
                startActivity(intent)
            }

            if(position==0)
                itemView.lecture_subsection_lock_layout.visibility=View.GONE
        }
    }
    inner class FooterViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

    inner class LectureSubAadapter(var datalist: ArrayList<ChapterListData>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            } else if  (viewType == ListUtils.TYPE_ELEM)  {
                val view : View = layoutInflater.inflate(R.layout.lecture_subsection_chapterlist_item, parent, false)
                view.setOnClickListener(onItemClick)
                ElemViewHolder(view)
            }
            else {
                val view : View = layoutInflater.inflate(R.layout.lecture_list_footer, parent, false)
                view.setOnClickListener(onItemClick)
                FooterViewHolder(view)
            }
        }

        override fun getItemCount() = chapterListData!!.size + 3

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if (holder?.itemViewType == ListUtils.TYPE_ELEM) {
                (holder as ElemViewHolder).bind(position - 2 )
            }else if(holder?.itemViewType == ListUtils.TYPE_HEADER) {
                (holder as HeaderViewHolder).bind()
            }
            else if(holder?.itemViewType == ListUtils.TYPE_SECOND_HEADER){
                holder as SecondHeaderViewHolder
            }
            else
                holder as FooterViewHolder
        }

        override fun getItemViewType(position: Int): Int
                = when (position) {
            0 -> ListUtils.TYPE_HEADER
            1 -> ListUtils.TYPE_SECOND_HEADER
            (itemCount - 1)->ListUtils.TYPE_FOOTER
            else -> ListUtils.TYPE_ELEM

        }
    }
}
