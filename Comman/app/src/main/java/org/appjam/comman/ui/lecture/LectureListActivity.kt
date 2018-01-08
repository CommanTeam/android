package org.appjam.comman.ui.lecture

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_lecture_list.*
import kotlinx.android.synthetic.main.chapter_explanation_item.view.*
import kotlinx.android.synthetic.main.lecture_list_item.view.*
import org.appjam.comman.LectureVideo1Activity
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.ChapterData
import org.appjam.comman.ui.card.CardActivity
import org.appjam.comman.ui.quiz.QuizActivity
import org.appjam.comman.util.ListUtils
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.setDefaultThreads

/**
 * Created by ChoGyuJin on 2017-12-31.
 */
class LectureListActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val TAG = "LectureListActivity"
    }

    private var chapterInfo: ChapterData.ChapterInfo? = null
    private var lectureInChapterDataList: List<ChapterData.LectureListinChapterData> = listOf()
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_list)

        lecture_list.layoutManager = LinearLayoutManager(this)
        lecture_list.adapter = LectureAdapter()

        disposables.add(APIClient.apiService.getChapterInfo(PrefUtils.getUserToken(this), intent.getIntExtra(ChapterData.CHAPTER_ID_KEY, 0))
                .setDefaultThreads()
                .subscribe({ response ->
                    chapterInfo = response.data[0]
                    lecture_list.adapter?.notifyDataSetChanged()
                }, { failure ->
                    Log.i(TAG, failure.message)
                }))
        disposables.add(APIClient.apiService.getLectureListInChapter(PrefUtils.getUserToken(this), intent.getIntExtra(ChapterData.CHAPTER_ID_KEY, 0))
                .setDefaultThreads()
                .subscribe({ response ->
                    lectureInChapterDataList = response.result
                    Log.i(TAG, "result: ${response.result}")
                    lecture_list.adapter?.notifyDataSetChanged()
                }, { failure ->
                    Log.i(TAG, "on Failure, Message: ${failure.message}")
                }))
    }

    override fun onClick(v: View?) {
        val idx: Int = lecture_list.getChildAdapterPosition(v)
        val name: String = "hello"
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
    }

    inner class ChapterExpViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.chapter_title_tv.text = chapterInfo?.title
            itemView.chapter_content_tv.text = chapterInfo?.info
        }
    }

    inner class LectureViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ChapterData.LectureListinChapterData) {

            var lecture_title : String ?=null
            if (data.watchedFlag == 0) {
                when {
                    data.lectureType == 0 -> itemView.lecture_list_img.setImageResource(R.drawable.quiz_icon)
                    data.lectureType == 1 -> itemView.lecture_list_img.setImageResource(R.drawable.picture_icon)
                    else -> itemView.lecture_list_img.setImageResource(R.drawable.video_icon)
                }
            } else {
                when {
                    data.lectureType == 0 -> itemView.lecture_list_img.setImageResource(R.drawable.completed_quiz_icon)
                    data.lectureType == 1 -> itemView.lecture_list_img.setImageResource(R.drawable.completed_pictures_icon)
                    else -> itemView.lecture_list_img.setImageResource(R.drawable.completed_video_icon)
                }
            }
            if (data.lectureType == 2) {
                itemView.setOnClickListener {
                    val intent = Intent(this@LectureListActivity, LectureVideo1Activity::class.java)
//                intent.putExtra("chapterID", chapterListData!![position].id)
                    //intent.putExtra("chapterID", chapterListData!![position].id)
                    startActivity(intent)
                }
            } else if (data.lectureType == 0) {
                itemView.setOnClickListener {
                    val intent = Intent(this@LectureListActivity, QuizActivity::class.java)
                    startActivity(intent)
                }
            } else if (data.lectureType == 1) {

                itemView.setOnClickListener {
                    if((data.lecturePriority)/10==0) {
                        lecture_title="0"+data.lecturePriority.toString()+". "+data.lectureTitle
                    }
                    else
                        lecture_title = data.lecturePriority.toString() + ". " + data.lectureTitle

                    val intent = Intent(this@LectureListActivity, CardActivity::class.java)
                    intent.putExtra("card_lecture_name_tv",lecture_title)
                    startActivity(intent)
                }
            }
            itemView.lecture_list_name_tv.text = data.lectureTitle
            itemView.lecture_list_num_tv.text = data.size.toString()
        }
    }

    inner class FootViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    inner class LectureAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var onItemClick: View.OnClickListener? = null
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

            return when (viewType) {
                ListUtils.TYPE_HEADER -> {
                    val mainView: View = layoutInflater.inflate(R.layout.chapter_explanation_item, parent, false)
                    mainView.setOnClickListener(onItemClick)
                    ChapterExpViewHolder(mainView)
                }
                ListUtils.TYPE_FOOTER -> {
                    val footView: View = layoutInflater.inflate(R.layout.course_item_footer, parent, false)
                    footView.setOnClickListener(onItemClick)
                    FootViewHolder(footView)
                }
                else -> {
                    val elemView: View = layoutInflater.inflate(R.layout.lecture_list_item, parent, false)
                    elemView.setOnClickListener(onItemClick)
                    LectureViewHolder(elemView)
                }
            }
        }
        override fun getItemCount() = lectureInChapterDataList.size + 2

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            when {
                holder?.itemViewType == ListUtils.TYPE_ELEM -> (holder as LectureViewHolder).bind(lectureInChapterDataList[position - 1])
                holder?.itemViewType == ListUtils.TYPE_HEADER -> (holder as ChapterExpViewHolder).bind()
                holder?.itemViewType == ListUtils.TYPE_FOOTER -> holder as FootViewHolder
            }
        }

        override fun getItemViewType(position: Int): Int {
            return when (position) {
                0 -> ListUtils.TYPE_HEADER
                itemCount - 1 -> ListUtils.TYPE_FOOTER
                else -> ListUtils.TYPE_ELEM
            }
        }
    }

    //disposables은 불렀으면 반드시 파괴해주어야한다.
    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
