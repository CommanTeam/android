package org.appjam.comman.ui.lecture

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
import kotlinx.android.synthetic.main.chapter_explanation_item.view.*
import kotlinx.android.synthetic.main.lecture_list_item.view.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.ChapterData
import org.appjam.comman.ui.card.CardActivity
import org.appjam.comman.ui.quiz.QuizActivity
import org.appjam.comman.util.ListUtils
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.YoutubeTimeUtils
import org.appjam.comman.util.setDefaultThreads
import org.appjam.comman.youtube.YoutubePracticeActivity

/**
 * Created by ChoGyuJin on 2017-12-31.
 */
class LectureListActivity : AppCompatActivity() {

    companion object {
        const val TAG = "LectureListActivity"
    }

    private var chapterInfo: ChapterData.ChapterInfo? = null
    private var lectureInChapterDataList: List<ChapterData.LectureListInChapterData> = listOf()
    private val disposables = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_list)

        lecture_list_rv.layoutManager = LinearLayoutManager(this)
        lecture_list_rv.adapter = LectureAdapter()
        lectureList_back_btn.setOnClickListener{
            finish()
        }
        disposables.add(APIClient.apiService.getChapterInfo(
                PrefUtils.getUserToken(this), intent.getIntExtra(ChapterData.CHAPTER_ID_KEY, 0))
                .setDefaultThreads()
                .subscribe({ response ->
                    chapterInfo = response.data
                    lectureList_lecture_name_tv.text = chapterInfo?.title
                    lecture_list_rv.adapter.notifyDataSetChanged()
                }, { failure ->
                    Log.i(TAG, "on Failure, Message: ${failure.message}")
                }))
        disposables.add(APIClient.apiService.getLectureListInChapter(
                PrefUtils.getUserToken(this), intent.getIntExtra(ChapterData.CHAPTER_ID_KEY, 0))
                .setDefaultThreads()
                .subscribe({ response ->
                    lectureInChapterDataList = response.result
                    lecture_list_rv.adapter?.notifyDataSetChanged()
                }, { failure ->
                    Log.i(TAG, "on Failure, Message: ${failure.message}")
                }))
    }

    inner class ChapterExpViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.chapter_title_tv.text = "${chapterInfo?.priority}장. ${chapterInfo?.title}"
            itemView.chapter_content_tv.text = chapterInfo?.info
        }
    }

    inner class LectureViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ChapterData.LectureListInChapterData) {
            if (data.watchedFlag == 0) {
                when {
//                    data.lectureType == 0 -> itemView.lecture_list_img.setImageResource(R.drawable.quiz_icon)
                    data.lectureType == 0 ->
                                            Glide.with(this@LectureListActivity)
                                                    .load(R.drawable.quiz_icon)
                                                    .into(itemView.lecture_list_img)

//                    data.lectureType == 1 -> itemView.lecture_list_img.setImageResource(R.drawable.picture_icon)
                    data.lectureType == 1 ->
                                            Glide.with(this@LectureListActivity)
                                                     .load(R.drawable.picture_icon)
                                                     .into(itemView.lecture_list_img)

//                    else -> itemView.lecture_list_img.setImageResource(R.drawable.video_icon)
                    else ->
                            Glide.with(this@LectureListActivity)
                                    .load(R.drawable.video_icon)
                                    .into(itemView.lecture_list_img)
                }
            } else {
                when {
//                    data.lectureType == 0 -> itemView.lecture_list_img.setImageResource(R.drawable.completed_quiz_icon)
                    data.lectureType == 0 ->
                                            Glide.with(this@LectureListActivity)
                                                     .load(R.drawable.completed_quiz_icon)
                                                     .into(itemView.lecture_list_img)

//                    data.lectureType == 1 -> itemView.lecture_list_img.setImageResource(R.drawable.completed_pictures_icon)
                    data.lectureType == 1 ->
                                            Glide.with(this@LectureListActivity)
                                                      .load(R.drawable.completed_pictures_icon)
                                                      .into(itemView.lecture_list_img)

//                    else -> itemView.lecture_list_img.setImageResource(R.drawable.completed_video_icon)
                    else ->
                            Glide.with(this@LectureListActivity)
                                    .load(R.drawable.completed_video_icon)
                                    .into(itemView.lecture_list_img)
                }
            }
            when {
                data.lectureType == 2 -> {
                    itemView.setOnClickListener {
                        val intent = Intent(this@LectureListActivity, YoutubePracticeActivity::class.java)
                        intent.putExtra("courseID", chapterInfo?.course_id)
                        intent.putExtra("lectureID", data.lectureID)
                        intent.putExtra("chapterID", chapterInfo?.id)
                        startActivity(intent)
                    }
                    //TODO video 시간 서버한테 받을 수 있으면 그걸로 text에 넣기
                    itemView.lecture_list_num_tv.text = "${YoutubeTimeUtils.formatTime(data.playTime)}"
                }

                data.lectureType == 0 -> {
                    itemView.setOnClickListener {
                        val intent = Intent(this@LectureListActivity, QuizActivity::class.java)
                        intent.putExtra("courseID", chapterInfo?.course_id)
                        intent.putExtra("lectureID", data.lectureID)
                        startActivity(intent)
                    }
                    itemView.lecture_list_num_tv.text = "${data.lectureCnt} 페이지"
                }
                data.lectureType == 1 -> {
                    itemView.setOnClickListener {
                        val intent = Intent(this@LectureListActivity, CardActivity::class.java)
                        intent.putExtra("courseID", chapterInfo?.course_id)
                        intent.putExtra("lectureID", data.lectureID)
                        startActivity(intent)
                    }
                    itemView.lecture_list_num_tv.text = "${data.lectureCnt} 문제"
                }
            }
            if(data.lecturePriority < 10) {
                itemView.lecture_list_name_tv.text = "0${data.lecturePriority} ${data.lectureTitle}"
            } else {
                itemView.lecture_list_name_tv.text = "${data.lecturePriority} ${data.lectureTitle}"
            }
        }
    }

    inner class FootViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    inner class LectureAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

            return when (viewType) {
                ListUtils.TYPE_HEADER -> {
                    val mainView: View = layoutInflater.inflate(R.layout.chapter_explanation_item, parent, false)
                    ChapterExpViewHolder(mainView)
                }
                ListUtils.TYPE_FOOTER -> {
                    val footView: View = layoutInflater.inflate(R.layout.course_item_footer, parent, false)
                    FootViewHolder(footView)
                }
                else -> {
                    val elemView: View = layoutInflater.inflate(R.layout.lecture_list_item, parent, false)
                    LectureViewHolder(elemView)
                }
            }
        }
        override fun getItemCount() = lectureInChapterDataList.size + 2

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            when {
                holder?.itemViewType == ListUtils.TYPE_ELEM ->
                    (holder as LectureViewHolder).bind(lectureInChapterDataList[position - 1])
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