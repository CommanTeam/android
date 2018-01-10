package org.appjam.comman.youtube

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_youtube_practice.*
import org.appjam.comman.R
import org.appjam.comman.custom.CustomSeekBar
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.VideoData
import org.appjam.comman.ui.quiz.QuizActivity
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

}
