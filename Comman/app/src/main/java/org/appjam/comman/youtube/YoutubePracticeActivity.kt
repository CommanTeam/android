package org.appjam.comman.youtube

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_youtube_practice.*
import kotlinx.android.synthetic.main.etc_lecvideo_list_items.view.*
import kotlinx.android.synthetic.main.second_lecvideo_list_items.view.*
import org.appjam.comman.LectureVideo1Activity
import org.appjam.comman.R
import org.appjam.comman.custom.CustomSeekBar
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.VideoData
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
    private val videoId = "wKJ9KzGQq0w"
    private var lectureID : Int = 0
    private var courseID : Int = 0
    private var videoLectureInfo : List<VideoData.VideoLectureInfo> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_practice)

        lectureID = intent.getIntExtra("lectureID", 0)
        courseID = intent.getIntExtra("courseID", 0)

        disposables.add(APIClient.apiService.getVideoLectureInfo(
                PrefUtils.getUserToken(this), lectureID)
                .setDefaultThreads()
                .subscribe({ response ->
                    videoLectureInfo = response.result
                    if(videoLectureInfo[0].lecture_priority < 10) {
                        video_lecture_name_tv.text = "0${videoLectureInfo[0].lecture_priority}. ${videoLectureInfo[0].title}"
                        video_lecture_name2_tv.text = "0${videoLectureInfo[0].lecture_priority}. ${videoLectureInfo[0].title}"
                    } else {
                        video_lecture_name_tv.text = "${videoLectureInfo[0].lecture_priority}. ${videoLectureInfo[0].title}"
                        video_lecture_name2_tv.text = "${videoLectureInfo[0].lecture_priority}. ${videoLectureInfo[0].title}"
                    }
                    lectureVideo_content_tv.text = videoLectureInfo[0].info
                }, { failure ->
                    Log.i(QuizActivity.TAG, "on Failure ${failure.message}")
                }))

        disposables.add(APIClient.apiService.getLectureListInChapter())
        practice_lectureVideo_youtube_playerView?.initialize(YouTubeConfigs.API_KEY, this)

    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
        if (!wasRestored) {
            player!!.loadVideo(videoId)
            youtube_playing_btn.setOnClickListener {
                player.play()
            }
            youtube_pause_btn.setOnClickListener {
                player.pause()
            }
            Toast.makeText(this@YoutubePracticeActivity, "onInitializationSuccess() 호출", Toast.LENGTH_SHORT).show()
            player.setPlayerStateChangeListener(object : YouTubePlayer.PlayerStateChangeListener {
                override fun onAdStarted() {}

                override fun onLoading() {}

                override fun onVideoStarted() {
                    youtube_progress_bar.max = player.durationMillis
                    youtube_progress_bar.progress = player.currentTimeMillis
                    val timerTask = object: TimerTask() {
                        override fun run() {
                            if (player.isPlaying) {
                                runOnUiThread {
                                    val current_time = YoutubeTimeUtils.formatTime(player.currentTimeMillis)
                                    val duration_time = YoutubeTimeUtils.formatTime(player.durationMillis)
                                    youtube_current_time_tv.text = "$current_time / $duration_time"
                                    youtube_progress_bar.progress = player.currentTimeMillis
                                    youtube_progress_bar.setSeekBarListener(object: CustomSeekBar.CustomSeekBarListener {
                                        override fun onThumbDragged(progress: Int) {
                                            player.seekToMillis(progress)
                                        }

                                        override fun onProgressBarTouched(progress: Int) {
                                            player.seekToMillis(progress)
                                        }
                                    })
                                }
                            }
                        }
                    }
                    val timer = Timer()
                    timer.schedule(timerTask, 0, 100)
                }

                override fun onLoaded(p0: String?) {}

                override fun onVideoEnded() {}

                override fun onError(p0: YouTubePlayer.ErrorReason?) {}

            })
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    private inner class LecVideoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if (holder?.itemViewType == ListUtils.TYPE_HEADER) { //이건 고정으로 넣는 것이기 때문에 상관하지 않는다.

            }
            else if (holder?.itemViewType == ListUtils.TYPE_SECOND_HEADER) { // 리스트에서 가장 1번 값이다. 하지만 인덱스 상에서는 0번째 값이 되어야 1번째에 올 수 있기에 -1
                (holder as LectureVideo1Activity.SecondViewHolder).bind(position - 1)
            }
            else if(holder?.itemViewType == ListUtils.TYPE_ELEM) { //리스트에서 2번째 값이다. 하지만 인덱스 상에서는 1번째 값이 되어야 2번째에 올 수 있기에 -1
                (holder as LectureVideo1Activity.EtcViewHolder).bind(position - 1)
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            if(viewType == ListUtils.TYPE_HEADER) {
                return FirstViewHolder(layoutInflater.inflate(R.layout.first_lecvideo_list_items, parent, false))
            }

            else if(viewType == ListUtils.TYPE_SECOND_HEADER) {
                return SecondViewHolder(layoutInflater.inflate(R.layout.second_lecvideo_list_items, parent, false))
            }
            else {
                val mainView : View = LayoutInflater.from(parent!!.context).inflate(R.layout.etc_lecvideo_list_items, parent, false)
                return EtcViewHolder(mainView)
            }

        }

        //
        override fun getItemCount(): Int = lecList.size + 1

        override fun getItemViewType(position: Int): Int
                = when (position) {
            0 -> ListUtils.TYPE_HEADER // 가장 첫번째
            1 -> ListUtils.TYPE_SECOND_HEADER // 두번째
            else -> ListUtils.TYPE_ELEM // 여러개 사용할 때
        }


    }

    private inner class FirstViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView)

    private inner class LastViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) //서버 통신할 때, result 값이 -1이면 마지막 강의라는 뜻

    private inner class SecondViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            itemView.secLecVideo_img.setImageResource(lecList[position].Img)
            itemView.secLecVideo_time_tv.text = lecList[position].time
            itemView.secLecVideo_title_tv.text = lecList[position].title
        }
    }

    private inner class EtcViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            itemView.etcLecVideo_img.setImageResource(lecList[position].Img)
            itemView.etcLecVideo_time_tv.text = lecList[position].time
            itemView.etcLecVideo_title_tv.text = lecList[position].title
        }
    }

}
