package org.appjam.comman.youtube

import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_youtube_practice.*
import org.appjam.comman.R
import org.appjam.comman.custom.CustomSeekBar
import java.util.*


class YoutubePracticeActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    companion object {
        const val TAG = "YoutubePracticeActivity"
    }
    private val videoId = "wKJ9KzGQq0w"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_practice)
        practice_lectureVideo_youtube_playerView?.initialize(YouTubeConfigs.API_KEY, this)
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
        if (!wasRestored) {
            player!!.loadVideo(videoId)
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
                                    youtube_current_time_text.text = player.currentTimeMillis.toString()
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

}
