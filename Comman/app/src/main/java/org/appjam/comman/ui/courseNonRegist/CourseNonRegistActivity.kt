package org.appjam.comman.ui.courseNonRegist

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_lecture_preview_item.*
import kotlinx.android.synthetic.main.activity_lecture_preview_item.view.*
import kotlinx.android.synthetic.main.activity_lecture_subsection.*
import kotlinx.android.synthetic.main.lecture_subsection_chapterlist_item.view.*
import kotlinx.android.synthetic.main.lecture_subsection_regist_item.view.*
import org.appjam.comman.R
import org.appjam.comman.custom.CustomSeekBar
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.CoursesData
import org.appjam.comman.network.data.LectureData
import org.appjam.comman.network.data.PopupData
import org.appjam.comman.ui.CourseSubsection.CourseSubPopupActivity
import org.appjam.comman.util.ListUtils
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.YoutubeTimeUtils
import org.appjam.comman.util.setDefaultThreads
import org.appjam.comman.youtube.YouTubeConfigs
import java.util.*


class CourseNonRegistActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private var mPlayer: YouTubePlayer? = null
    private var videoId = "Rs8zSWSR5Ys"
    private val timer = Timer()
    private lateinit var timerTask: TimerTask
    private var courseID: Int = 0
    private var lecturePreviewInfo: LectureData.LecturePreviewInfo? = null
    private var playOrpause : Int = 0


    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
        this.mPlayer = player
        player!!.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS)
        preview_youtube_playing_btn.setOnClickListener {
            if(playOrpause == 0) {
                this.mPlayer?.pause()
                preview_youtube_playing_btn.setBackgroundResource(R.drawable.video_new_pause_btn)
                playOrpause = 1
            } else {
                this.mPlayer?.play()
                preview_youtube_playing_btn.setBackgroundResource(R.drawable.video_new_play_btn)
                playOrpause = 0
            }
        }

        this.mPlayer!!.setPlayerStateChangeListener(object : YouTubePlayer.PlayerStateChangeListener {
            override fun onAdStarted() {}

            override fun onLoading() {}

            override fun onVideoStarted() {
                preview_youtube_progress_bar.max = mPlayer!!.durationMillis
                preview_youtube_progress_bar.progress = mPlayer!!.currentTimeMillis

            }


            override fun onLoaded(p0: String?) {}

            override fun onVideoEnded() {}

            override fun onError(p0: YouTubePlayer.ErrorReason?) {}

        })

        if (!wasRestored) {
            mPlayer!!.loadVideo(videoId)
            timer.schedule(timerTask, 0, 100)
        } else {
            this.mPlayer?.play()
            timer.schedule(timerTask, 0, 100)
        }
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?){

    }

    companion object {
        private val TAG = "CourseNonRegistActivity"
    }

    private var courseMetaData: CoursesData.CourseMetadata? = null
    private var chaptersInfoList: List<PopupData.PopupContentInfo> = listOf()
    private val disposables = CompositeDisposable()
    private var isPurchased: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_subsection)


        sub_back_btn_ex.setOnClickListener{
            finish()
        }
        sub_back_btn.setOnClickListener {
            finish()
        }

        val recycler_view = lecture_subsection_list_view
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = LectureSubAdapter()


        disposables.add(APIClient.apiService.getCourseMetaInfo(
                PrefUtils.getUserToken(this@CourseNonRegistActivity), intent.getIntExtra("courseID", 0))  //defaultValue 0넣는게 맞을까?
                .setDefaultThreads()
                .subscribe({ response ->
                    courseMetaData = response.result
                    sub_lecture_name_tv.text = courseMetaData?.title
                    recycler_view.adapter.notifyDataSetChanged()
                }, { failure ->
                    Log.i(TAG, "on Failure ${failure.message}")
                }))

        disposables.add(APIClient.apiService.getPopupContentInfos(
                PrefUtils.getUserToken(this@CourseNonRegistActivity), intent.getIntExtra("courseID", 0))
                .setDefaultThreads()
                .subscribe({ response ->
                    chaptersInfoList = response.result
                    recycler_view.adapter.notifyDataSetChanged()
                }, { failure ->
                    Log.i(TAG, "on Failure ${failure.message}")
                }))

        disposables.add(APIClient.apiService.getLecrturePreview(
                PrefUtils.getUserToken(this@CourseNonRegistActivity), intent.getIntExtra("courseID", 0))
                .setDefaultThreads()
                .subscribe({ response ->
                    lecturePreviewInfo = response.result
                    recycler_view.adapter.notifyDataSetChanged()
                }, { failure ->
                    Log.i(TAG, "on Failure ${failure.message}")
                }))

        timerTask = object : TimerTask() {
            override fun run() {
                if (mPlayer?.isPlaying == true) {
                    runOnUiThread {
                        val current_time = YoutubeTimeUtils.formatTime(mPlayer!!.currentTimeMillis)
                        val duration_time = YoutubeTimeUtils.formatTime(mPlayer!!.durationMillis)
                        preview_youtube_current_time_tv.text = "$current_time / $duration_time"
                        preview_youtube_progress_bar.progress = mPlayer!!.currentTimeMillis
                        preview_youtube_progress_bar.setSeekBarListener(object : CustomSeekBar.CustomSeekBarListener {
                            override fun onThumbDragged(progress: Int) {
                                mPlayer!!.seekToMillis(progress)
                            }

                            override fun onProgressBarTouched(progress: Int) {
                                mPlayer!!.seekToMillis(progress)
                            }
                        })
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        mPlayer?.release()
        super.onBackPressed()
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            Log.i(TAG, "meta data : ${courseMetaData?.supplier_thumbnail}")
            Log.i(TAG, "itemview : ${itemView.regist_lecture_subsection_course_profile_iv}")
            Glide.with(this@CourseNonRegistActivity)
                    .load(courseMetaData?.supplier_thumbnail)
                    .into(itemView.regist_lecture_subsection_course_profile_iv)

            itemView.regist_lecture_subsection_course_name_tv.text = courseMetaData?.title
            itemView.regist_lecture_subsection_instructor_name_tv.text = courseMetaData?.name
            itemView.regist_lecture_subsection_course_exp_tv.text = courseMetaData?.info
            itemView.regist_lecture_subsection_popup_layout.setOnClickListener {
                mPlayer?.release()
                val intent = Intent(this@CourseNonRegistActivity, CourseSubPopupActivity::class.java)
                intent.putExtra("courseID", courseMetaData?.id)
                startActivity(intent)
            }
            itemView.regist_lecture_regist_btn.setOnClickListener {
                mPlayer?.release()
                val intent = Intent(this@CourseNonRegistActivity, EnrollPopupActivity::class.java)
                intent.putExtra("courseID", courseMetaData?.id)
                startActivity(intent)
            }
        }
    }

    inner class SecondHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            videoId = lecturePreviewInfo?.video_id?:""
            if ((lecturePreviewInfo?.lecture_priority ?: 0) < 10) {
                itemView.preview_youtube_lecture_name_tv.text = "0${lecturePreviewInfo?.lecture_priority}. ${lecturePreviewInfo?.lecture_title}"
            } else {
                itemView.preview_youtube_lecture_name_tv.text = "${lecturePreviewInfo?.lecture_priority}. ${lecturePreviewInfo?.lecture_title}"
            }
            itemView.preview_lectureVideo_youtube_playerView.initialize(YouTubeConfigs.API_KEY, this@CourseNonRegistActivity)

            itemView.lecture_subsection_preview_purchase_btn.setOnClickListener {
                mPlayer?.release()
                val intent = Intent(this@CourseNonRegistActivity, ChargePopupActivity::class.java)
                intent.putExtra("courseID", courseMetaData?.id)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }
        }
    }

    inner class ElemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO: Implement more detail view binding
        fun bind(position: Int) {
            itemView.lecture_subsection_chapterlist_chapnum_tv.text = "${chaptersInfoList[position].priority}장"
            itemView.lecture_subsection_chapterlist_totalnum_tv.text = "총 ${chaptersInfoList[position].lectureCnt}강"
            itemView.lecture_subsection_chapterlist_chapname_tv.text = chaptersInfoList[position].title
        }
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class LectureSubAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                ListUtils.TYPE_HEADER -> {
                    val view: View = layoutInflater.inflate(R.layout.lecture_subsection_regist_item, parent, false)
                    HeaderViewHolder(view)
                }
                ListUtils.TYPE_SECOND_HEADER -> {
                    val view: View = layoutInflater.inflate(R.layout.activity_lecture_preview_item, parent, false)
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