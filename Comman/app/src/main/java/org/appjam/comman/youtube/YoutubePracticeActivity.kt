package org.appjam.comman.youtube

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_youtube_practice.*
import kotlinx.android.synthetic.main.etc_lecvideo_list_items.view.*
import kotlinx.android.synthetic.main.first_lecvideo_list_items.view.*
import kotlinx.android.synthetic.main.second_lecvideo_list_items.view.*
import org.appjam.comman.R
import org.appjam.comman.custom.CustomSeekBar
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.ChapterData
import org.appjam.comman.network.data.NextLectureData
import org.appjam.comman.network.data.VideoData
import org.appjam.comman.ui.card.CardActivity
import org.appjam.comman.ui.quiz.QuizActivity
import org.appjam.comman.util.ListUtils
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.YoutubeTimeUtils
import org.appjam.comman.util.setDefaultThreads
import java.util.*


class YoutubePracticeActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    companion object {
        const val TAG = "YoutubePracticeActivity"
    }

    private val disposables = CompositeDisposable()
    private var videoId = "Rs8zSWSR5Ys"
    private var lectureID: Int = 27        //실험중
    private var chapterID: Int = 1
    private var courseID: Int = 1
    private var isComplete: Int = 0
    private var videoLectureInfo: VideoData.VideoLectureInfo? = null
    private var lectureList: List<ChapterData.LectureListInChapterData> = listOf()
    private var nextLectureResponse: NextLectureData.NextLectureResponse? = null
    private var mPlayer: YouTubePlayer? = null
    private val timer = Timer()
    private var timeOfPref : Int = 0
    private lateinit var timerTask : TimerTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_practice)

//        lectureID = intent.getIntExtra("lectureID", 0)
//        courseID = intent.getIntExtra("courseID", 0)
//        chapterID = intent.getIntExtra("chapterID", 0)
        if (lectureID != PrefUtils.getRecentLectureOfCourseID(this, courseID)) {
            PrefUtils.putYoutubeCurrentTimeInCourse(this, 0, courseID)
        }
        PrefUtils.putLectureOfCourseID(this, lectureID, courseID)
        PrefUtils.putCurrentLectureID(this, lectureID)
        timeOfPref = PrefUtils.getYoutubeCurrentTimeInCourse(this, courseID)

        video_lecture_list_rv?.layoutManager = LinearLayoutManager(this)
        video_lecture_list_rv?.adapter = LectureVideoAdapter()

        disposables.add(APIClient.apiService.getVideoLectureInfo(       //비디오강의 정보 가져오기
                PrefUtils.getUserToken(this), lectureID)
                .setDefaultThreads()
                .subscribe({ response ->
                    videoLectureInfo = response.result
                    if (videoLectureInfo!!.priority < 10) {
                        video_lecture_name_tv?.text = "0${videoLectureInfo!!.priority}. ${videoLectureInfo!!.title}"
                        video_lecture_name2_tv?.text = "0${videoLectureInfo!!.priority}. ${videoLectureInfo!!.title}"
                    } else {
                        video_lecture_name_tv?.text = "${videoLectureInfo!!.priority}. ${videoLectureInfo!!.title}"
                        video_lecture_name2_tv?.text = "${videoLectureInfo!!.priority}. ${videoLectureInfo!!.title}"
                    }
                    lectureVideo_content_tv?.text = videoLectureInfo!!.info
//                    videoId = videoLectureInfo[0].video_id
                    practice_lectureVideo_youtube_playerView?.initialize(YouTubeConfigs.API_KEY, this)
                    land_practice_lectureVideo_youtube_playerView?.initialize(YouTubeConfigs.API_KEY, this)
                }, { failure ->
                    Log.i(QuizActivity.TAG, "on Failure ${failure.message}")
                }))

        disposables.add(APIClient.apiService.getLectureListInChapter(       //챕터의 강의리스트 정보
                PrefUtils.getUserToken(this), chapterID)
                .setDefaultThreads()
                .subscribe({ response ->
                    lectureList = response.result
                    video_lecture_list_rv?.adapter?.notifyDataSetChanged()
                }, { failure ->
                    Log.i(QuizActivity.TAG, "on Failure ${failure.message}")
                }))

        disposables.add(APIClient.apiService.getNextLectureInfo(        //다음 강의 정보 얻어오기
                PrefUtils.getUserToken(this@YoutubePracticeActivity), lectureID)
                .setDefaultThreads()
                .subscribe({ response ->
                    nextLectureResponse = response
                }, { failure ->
                    Log.i(QuizActivity.TAG, "on Failure ${failure.message}")
                }))

        video_full_screen_btn?.setOnClickListener {
            timer.cancel()
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            land_practice_lectureVideo_youtube_playerView?.initialize(YouTubeConfigs.API_KEY, this)
        }

        youtube_small_view_btn?.setOnClickListener {
            timer.cancel()
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            practice_lectureVideo_youtube_playerView?.initialize(YouTubeConfigs.API_KEY, this)
        }

        timerTask = object : TimerTask() {
            override fun run() {
                if (mPlayer?.isPlaying == true) {
                    runOnUiThread {
                        val current_time = YoutubeTimeUtils.formatTime(mPlayer!!.currentTimeMillis)
                        val duration_time = YoutubeTimeUtils.formatTime(mPlayer!!.durationMillis)
                        youtube_current_time_tv.text = "$current_time / $duration_time"
                        youtube_progress_bar.progress = mPlayer!!.currentTimeMillis
                        PrefUtils.putYoutubeCurrentTimeInCourse(this@YoutubePracticeActivity, mPlayer!!.currentTimeMillis, courseID)
                        Log.i(TAG, PrefUtils.getInt(this@YoutubePracticeActivity, PrefUtils.CURRENT_TIME).toString())
                        youtube_progress_bar.setSeekBarListener(object : CustomSeekBar.CustomSeekBarListener {
                            override fun onThumbDragged(progress: Int) {
                                mPlayer!!.seekToMillis(progress)
                            }

                            override fun onProgressBarTouched(progress: Int) {
                                mPlayer!!.seekToMillis(progress)
                            }
                        })

                        if (((mPlayer!!.currentTimeMillis / mPlayer!!.durationMillis) > 0.9) and (isComplete == 0)) {
                            disposables.add(APIClient.apiService.registerFinishLecture(
                                    PrefUtils.getUserToken(this@YoutubePracticeActivity), lectureID)
                                    .setDefaultThreads()
                                    .subscribe({
                                        isComplete = 1
                                    }, { failure ->
                                        Log.i(QuizActivity.TAG, "on Failure ${failure.message}")
                                    }))
                        }
                    }
                }
            }
        }

    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
        Toast.makeText(this, "동영상을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
        this.mPlayer = player
        youtube_playing_btn.setOnClickListener {
            this.mPlayer?.play()
        }
        youtube_pause_btn.setOnClickListener {
            this.mPlayer?.pause()
        }
        this.mPlayer!!.setPlayerStateChangeListener(object : YouTubePlayer.PlayerStateChangeListener {
            override fun onAdStarted() {}

            override fun onLoading() {}

            override fun onVideoStarted() {
                youtube_progress_bar.max = this@YoutubePracticeActivity.mPlayer!!.durationMillis
                youtube_progress_bar.progress = this@YoutubePracticeActivity.mPlayer!!.currentTimeMillis

            }


            override fun onLoaded(p0: String?) {}

            override fun onVideoEnded() {
                if (video_lecture_list_rv?.getChildAt(0)?.fstLecVideo_switch?.isChecked == true) {
                    val intent = ListUtils.linkToNextLecture(this@YoutubePracticeActivity, nextLectureResponse, courseID)
                    startActivity(intent)
                }
            }

            override fun onError(p0: YouTubePlayer.ErrorReason?) {}

        })

        if (!wasRestored) {
            mPlayer!!.loadVideo(videoId, timeOfPref)
            timer.schedule(timerTask, 0, 100)
        } else {
            this.mPlayer?.play()
            timer.schedule(timerTask, 0, 100)
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    private inner class LectureVideoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                ListUtils.TYPE_HEADER -> HeaderViewHolder(layoutInflater.inflate(R.layout.first_lecvideo_list_items, parent, false))
                ListUtils.TYPE_SECOND_HEADER -> {
                    return if (nextLectureResponse?.nextLectureOfChapter == -1) {
                        SecondHeaderViewHolder(layoutInflater.inflate(R.layout.lastlecvideo_list_items, parent, false))
                    } else {
                        SecondHeaderViewHolder(layoutInflater.inflate(R.layout.second_lecvideo_list_items, parent, false))
                    }
                }
                ListUtils.TYPE_FOOTER -> FooterViewHolder(layoutInflater.inflate(R.layout.quiz_result_footer, parent, false))
                else -> ElemViewHolder(layoutInflater.inflate(R.layout.etc_lecvideo_list_items, parent, false))
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            when {
                holder?.itemViewType == ListUtils.TYPE_SECOND_HEADER -> // 리스트에서 가장 1번 값이다. 하지만 인덱스 상에서는 0번째 값이 되어야 1번째에 올 수 있기에 -1
                    (holder as SecondHeaderViewHolder).bind()
                holder?.itemViewType == ListUtils.TYPE_ELEM -> {//리스트에서 2번째 값이다. 하지만 인덱스 상에서는 1번째 값이 되어야 2번째에 올 수 있기에 -1
                    val elem_position = ((videoLectureInfo?.priority?: 0) + position - 2) % lectureList.size
                    (holder as ElemViewHolder).bind(elem_position)
                }
            }

        }


        override fun getItemCount(): Int = lectureList.size + 2

        override fun getItemViewType(position: Int): Int
                = when (position) {
            0 -> ListUtils.TYPE_HEADER // 가장 첫번째
            1 -> ListUtils.TYPE_SECOND_HEADER // 두번째
            (itemCount - 1) -> ListUtils.TYPE_FOOTER    //마지막, 마진을 주기 위해
            else -> ListUtils.TYPE_ELEM // 여러개 사용할 때
        }


    }

    private inner class HeaderViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    private inner class FooterViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    private inner class SecondHeaderViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            if (nextLectureResponse?.nextLectureOfChapter != -1) {
                itemView.secLecVideo_time_tv.text = YoutubeTimeUtils.formatTime(mPlayer?.durationMillis ?: 0)
                if ((videoLectureInfo?.priority?: 0) < 10)
                    itemView.secLecVideo_title_tv.text = "0${videoLectureInfo?.priority} ${videoLectureInfo?.title}"
                else
                    itemView.secLecVideo_title_tv.text = "${videoLectureInfo?.priority} ${videoLectureInfo?.title}"
            } else {
                itemView.setOnClickListener {
                    val intent = ListUtils.linkToNextLecture(this@YoutubePracticeActivity, nextLectureResponse, courseID)
                    startActivity(intent)
                }
            }
        }
    }

    private inner class ElemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            if (lectureList[position].lecturePriority < 10)
                itemView.etcLecVideo_title_tv.text = "0${lectureList[position].lecturePriority} ${lectureList[position].lectureTitle}"
            else
                itemView.etcLecVideo_title_tv.text = "${lectureList[position].lecturePriority} ${lectureList[position].lectureTitle}"

            val data = lectureList[position]
            if (data.watchedFlag == 0) {
                when {
                    data.lectureType == 0 -> itemView.etcLecVideo_img.setImageResource(R.drawable.quiz_icon)
                    data.lectureType == 1 -> itemView.etcLecVideo_img.setImageResource(R.drawable.picture_icon)
                    else -> itemView.etcLecVideo_img.setImageResource(R.drawable.video_icon)
                }
            } else {
                when {
                    data.lectureType == 0 -> itemView.etcLecVideo_img.setImageResource(R.drawable.completed_quiz_icon)
                    data.lectureType == 1 -> itemView.etcLecVideo_img.setImageResource(R.drawable.completed_pictures_icon)
                    else -> itemView.etcLecVideo_img.setImageResource(R.drawable.completed_video_icon)
                }
            }
            when {
                data.lectureType == 2 -> {
                    itemView.setOnClickListener {
                        val intent = Intent(this@YoutubePracticeActivity, YoutubePracticeActivity::class.java)
                        intent.putExtra("courseID", courseID)
                        intent.putExtra("lectureID", data.lectureID)
                        intent.putExtra("chapterID", chapterID)
                        startActivity(intent)
                    }
                    //TODO video 시간 서버한테 받을 수 있으면 그걸로 text에 넣기
                    itemView.etcLecVideo_time_tv.text = "${YoutubeTimeUtils.formatTime(data.playTime)}"
                }
                data.lectureType == 0 -> {
                    itemView.setOnClickListener {
                        val intent = Intent(this@YoutubePracticeActivity, QuizActivity::class.java)
                        intent.putExtra("courseID", courseID)
                        intent.putExtra("lectureID", data.lectureID)
                        startActivity(intent)
                    }
                    itemView.etcLecVideo_time_tv.text = "${data.lectureCnt} 페이지"
                }
                data.lectureType == 1 -> {
                    itemView.setOnClickListener {
                        val intent = Intent(this@YoutubePracticeActivity, CardActivity::class.java)
                        intent.putExtra("courseID", courseID)
                        intent.putExtra("lectureID", data.lectureID)
                        startActivity(intent)
                    }
                    itemView.etcLecVideo_time_tv.text = "${data.lectureCnt} 문제"
                }
            }
        }
    }

}
