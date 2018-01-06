package org.appjam.comman.youtube

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_youtube_practice.*
import org.appjam.comman.R
import java.util.*


class YoutubePracticeActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener,
                                    YouTubePlayer.PlaybackEventListener {
    override fun onSeekTo(p0: Int) {
        mHandler?.postDelayed(runnable, 1000)
    }

    override fun onBuffering(p0: Boolean) {
    }

    override fun onPlaying() {
        mHandler?.postDelayed(runnable, 1000)
        displayCurrentTime()
    }

    override fun onStopped() {
        mHandler?.removeCallbacks(runnable)
    }

    override fun onPaused() {
        mHandler?.removeCallbacks(runnable)
    }

    private var player: YouTubePlayer? = null
    private val videoId = "wKJ9KzGQq0w"
    private var mHandler : Handler? = null
    private lateinit var video_seek_bar : SeekBar
    private lateinit var current_time_text : TextView
    private var current_time : Int = 0
    private var duration_time : Int = 0
    lateinit var timer : Timer
    lateinit var timerTask : TimerTask

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
    }

    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, p1: YouTubePlayer?, wasRestored: Boolean) {
        if (!wasRestored) {
            this.player = p1
            p1!!.loadVideo(videoId)
            Toast.makeText(this@YoutubePracticeActivity, "onInitializationSuccess() 호출", Toast.LENGTH_SHORT).show()
//            player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS)

            player!!.setPlayerStateChangeListener(object : YouTubePlayer.PlayerStateChangeListener {
                override fun onAdStarted() {
                }

                override fun onLoading() {
                }

                override fun onVideoStarted() {
                    duration_time = player!!.durationMillis
//                    video_seek_bar.max = duration_time
                    mHandler?.postDelayed(runnable, 1000)
//                    timerTask = object : TimerTask() { //timerTask는 timer가 일할 내용을 기록하는 객체
//                        override fun run() {
//                            increaseBar() //timer가 동작할 내용을 갖는 함수 호출
//                        }
//                    }
//                    timer.schedule(timerTask, 0, 1000)
                }

                override fun onLoaded(p0: String?) {
                }

                override fun onVideoEnded() {
                }

                override fun onError(p0: YouTubePlayer.ErrorReason?) {
                }

            })
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_practice)

        practice_lectureVideo_youtube_playerView?.initialize(YouTubeConfigs.API_KEY, this)

        video_seek_bar = compat_seek_bar
        current_time_text = youtube_current_time_text

        compat_seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                val lengthPlayed = (player?.durationMillis?.times(p0!!.progress) ?: 0) / 100
                player?.seekToMillis(lengthPlayed)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    p0!!.thumb.setVisible(true, false)
                }
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    p0!!.thumb.setVisible(false, false)
                }
            }

        })
        mHandler = Handler()
//        timer = Timer()

    }

//     fun increaseBar() {
//         runOnUiThread {   //run을 해준다. 그러나 일반 thread처럼 .start()를 해줄 필요는 없다
//             current_time = video_seek_bar.progress
//             if (current_time < duration_time) {
//                 current_time += 1000
//             } else if (current_time >= duration_time) {
//                 timer.cancel()
//                 Thread.interrupted()  // Thread 강제 종료
//             }
//
//             video_seek_bar.progress = current_time
//         }
//     }

    private fun formatTime(millis: Int): String {
        val seconds = millis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        return (if (hours == 0) "--:" else hours.toString() + ":") + String.format("%02d:%02d", minutes % 60, seconds % 60)
    }

    private fun displayCurrentTime() {
        current_time = player!!.currentTimeMillis
        val formattedTime = formatTime(duration_time - current_time)
        val lengthPlayed = 100 - (duration_time - current_time) * 100 / duration_time
        video_seek_bar.progress = lengthPlayed
        current_time_text.text = formattedTime
    }

    private val runnable = object : Runnable {
        override fun run() {
            displayCurrentTime()
            mHandler!!.postDelayed(this, 1000)
        }
    }
}
